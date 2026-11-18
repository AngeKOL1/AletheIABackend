package com.example.aletheia.pretest.service.impl;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.aletheia.pretest.client.AletheiaClient;
import com.example.aletheia.pretest.client.AletheiaJson;
import com.example.aletheia.pretest.config.AletheiaProperties;
import com.example.aletheia.pretest.dto.AnalisisResponseDTO;
import com.example.aletheia.pretest.dto.AnalisisResumenDTO;
import com.example.aletheia.pretest.dto.aletheia.AnalisisResponse;
import com.example.aletheia.pretest.dto.aletheia.ErrorAnalisis;
import com.example.aletheia.pretest.dto.aletheia.Informe;
import com.example.aletheia.pretest.dto.aletheia.NoticiaRequest;
import com.example.aletheia.pretest.exception.AletheiaAnalisisNoEncontradoException;
import com.example.aletheia.pretest.exception.AletheiaConfiguracionException;
import com.example.aletheia.pretest.exception.AletheiaException;
import com.example.aletheia.pretest.exception.ModelNotFoundException;
import com.example.aletheia.pretest.mapper.AnalisisMapper;
import com.example.aletheia.pretest.models.AnalisisNoticia;
import com.example.aletheia.pretest.models.Noticia;
import com.example.aletheia.pretest.models.SolicitudAnalisis;
import com.example.aletheia.pretest.models.enums.EstadoAnalisis;
import com.example.aletheia.pretest.repository.AnalisisNoticiaRepo;
import com.example.aletheia.pretest.repository.NoticiaRepo;
import com.example.aletheia.pretest.repository.ParticipanteRepo;
import com.example.aletheia.pretest.repository.SolicitudAnalisisRepo;
import com.example.aletheia.pretest.service.IAnalisisService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalisisService extends GenericService<AnalisisNoticia, Integer> implements IAnalisisService {

    static final String TIPO_TIEMPO_EXCEDIDO = "tiempo_excedido";
    static final String TIPO_INFORME_AUSENTE = "informe_ausente";
    static final String TIPO_ANALISIS_PERDIDO = "analisis_perdido";
    static final String TIPO_DESCONOCIDO = "error_desconocido";

    private final AnalisisNoticiaRepo repo;
    private final SolicitudAnalisisRepo solicitudRepo;
    private final NoticiaRepo noticiaRepo;
    private final ParticipanteRepo participanteRepo;
    private final AletheiaClient cliente;
    private final AnalisisMapper mapper;
    private final AletheiaProperties propiedades;
    private final Clock clock;

    @Override
    protected AnalisisNoticiaRepo getRepo() {
        return repo;
    }

    // ---------- Peticiones del usuario ----------

    @Override
    @Transactional
    public AnalisisResponseDTO solicitar(Integer participanteId, Integer noticiaId) {
        // El bloqueo de la noticia evita que dos peticiones simultáneas paguen dos análisis.
        Noticia noticia = noticiaRepo.findByIdParaAnalisis(noticiaId)
                .orElseThrow(() -> new ModelNotFoundException("Noticia no encontrada"));
        LocalDateTime ahora = LocalDateTime.now(clock);

        Optional<AnalisisNoticia> ultimo = repo.findFirstByNoticiaIdOrderByIdDesc(noticiaId);
        AnalisisNoticia analisis;
        if (ultimo.isPresent() && ultimo.get().getEstado().esReutilizable()) {
            analisis = ultimo.get();
        } else {
            ultimo.ifPresent(anterior -> comprobarEsperaReintento(anterior, ahora));
            analisis = crearEnAletheia(noticia, ahora);
        }

        if (!solicitudRepo.existsByParticipanteIdAndAnalisisId(participanteId, analisis.getId())) {
            SolicitudAnalisis solicitud = new SolicitudAnalisis();
            solicitud.setParticipante(participanteRepo.getReferenceById(participanteId));
            solicitud.setAnalisis(analisis);
            solicitud.setFechaSolicitud(ahora);
            solicitudRepo.save(solicitud);
        }
        return aVista(analisis);
    }

    @Override
    public AnalisisResponseDTO consultar(Integer participanteId, Integer noticiaId) {
        AnalisisNoticia analisis = solicitudRepo.findAnalisisDeParticipante(participanteId, noticiaId, Limit.of(1))
                .stream().findFirst()
                .orElseThrow(() -> new ModelNotFoundException("No has solicitado el análisis de esta noticia"));
        return aVista(analisis);
    }

    @Override
    public List<AnalisisResumenDTO> listarMisAnalisis(Integer participanteId) {
        return mapper.toResumenDTOList(solicitudRepo.findResumenByParticipanteId(participanteId));
    }

    // ---------- Seguimiento de pendientes (tarea programada) ----------

    @Override
    public List<Integer> idsPendientes() {
        return repo.findIdsPendientes();
    }

    /**
     * Sin transacción propia: la consulta HTTP (hasta 30 s) no retiene una conexión a la BD.
     * Cada lectura y guardado usa la transacción del repositorio.
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void actualizarEstado(Integer analisisId) {
        AnalisisNoticia analisis = repo.findById(analisisId).orElse(null);
        if (analisis == null || analisis.getEstado() != EstadoAnalisis.PENDIENTE) {
            return;
        }
        LocalDateTime ahora = LocalDateTime.now(clock);

        try {
            AnalisisResponse respuesta = cliente.consultar(analisis.getAletheiaId());
            if (aplicarRespuesta(analisis, respuesta, ahora)) {
                repo.save(analisis);
                return;
            }
            analisis.setEstadoAletheia(respuesta.estado());
        } catch (AletheiaAnalisisNoEncontradoException e) {
            marcarFinal(analisis, EstadoAnalisis.PERDIDO, TIPO_ANALISIS_PERDIDO, null,
                    "AletheIA ya no tiene este análisis (posible reinicio del servicio). Vuelve a solicitarlo.", ahora);
            log.warn("Análisis {} perdido en AletheIA", analisisId);
            repo.save(analisis);
            return;
        } catch (AletheiaConfiguracionException e) {
            log.error("No se pudo consultar el análisis {}: {}", analisisId, e.getMessage());
        } catch (AletheiaException e) {
            // AletheIA caída u ocupada: se reintenta en la siguiente pasada.
            log.warn("No se pudo consultar el análisis {}: {}", analisisId, e.getMessage());
        }

        if (analisis.getCreado().plus(propiedades.tiempoMaximoPendiente()).isBefore(ahora)) {
            marcarFinal(analisis, EstadoAnalisis.ERROR, TIPO_TIEMPO_EXCEDIDO, null,
                    "El análisis superó el tiempo máximo de espera (" + propiedades.tiempoMaximoPendiente().toMinutes()
                            + " min).", ahora);
            log.warn("Análisis {} marcado como ERROR por tiempo excedido", analisisId);
        } else {
            analisis.setActualizado(ahora);
        }
        repo.save(analisis);
    }

    // ---------- Ayudantes ----------

    private AnalisisNoticia crearEnAletheia(Noticia noticia, LocalDateTime ahora) {
        NoticiaRequest peticion = mapper.toNoticiaRequest(noticia);
        if (peticion.texto() == null) {
            throw new IllegalArgumentException("La noticia no tiene texto para analizar");
        }
        // Si AletheIA falla, la excepción deshace la transacción: no se guarda nada.
        AnalisisResponse respuesta = cliente.crear(peticion);

        AnalisisNoticia analisis = new AnalisisNoticia();
        analisis.setNoticia(noticia);
        analisis.setAletheiaId(respuesta.id());
        analisis.setEstado(EstadoAnalisis.PENDIENTE);
        analisis.setEstadoAletheia(respuesta.estado());
        analisis.setCreado(ahora);
        analisis.setActualizado(ahora);
        return repo.save(analisis);
    }

    /** Cada envío cuesta: tras un fallo reciente no se reenvía la misma noticia enseguida. */
    private void comprobarEsperaReintento(AnalisisNoticia anterior, LocalDateTime ahora) {
        LocalDateTime permitidoDesde = anterior.getActualizado().plus(propiedades.esperaReintento());
        if (ahora.isBefore(permitidoDesde)) {
            throw new IllegalStateException(
                    "El último análisis de esta noticia falló hace muy poco; inténtalo de nuevo en unos segundos");
        }
    }

    /** Aplica un estado final (completado o error). Devuelve false si el análisis sigue pendiente. */
    private boolean aplicarRespuesta(AnalisisNoticia analisis, AnalisisResponse respuesta, LocalDateTime ahora) {
        analisis.setEstadoAletheia(respuesta.estado());
        switch (respuesta.estado()) {
            case AnalisisResponse.COMPLETADO -> {
                Informe informe = respuesta.informe();
                if (informe == null) {
                    // Nunca se inventa un resultado: sin informe es un error.
                    marcarFinal(analisis, EstadoAnalisis.ERROR, TIPO_INFORME_AUSENTE, null,
                            "AletheIA marcó el análisis como completado pero no envió el informe.", ahora);
                    return true;
                }
                analisis.setInformeJson(AletheiaJson.escribir(informe));
                analisis.setVeredictoGlobal(informe.veredictoGlobal());
                analisis.setMotivoSinVeredicto(informe.motivoSinVeredicto());
                analisis.setEstado(EstadoAnalisis.COMPLETADO);
                analisis.setActualizado(ahora);
                analisis.setFinalizado(ahora);
                log.info("Análisis {} completado: {}", analisis.getId(), informe.veredictoGlobal());
                return true;
            }
            case AnalisisResponse.ERROR -> {
                ErrorAnalisis error = respuesta.error();
                if (error == null) {
                    marcarFinal(analisis, EstadoAnalisis.ERROR, TIPO_DESCONOCIDO, null,
                            "AletheIA informó un error sin detalle.", ahora);
                } else {
                    marcarFinal(analisis, EstadoAnalisis.ERROR,
                            error.tipo() != null ? error.tipo() : TIPO_DESCONOCIDO, error.etapa(), error.mensaje(), ahora);
                }
                log.warn("Análisis {} terminó con error en AletheIA ({})", analisis.getId(), analisis.getErrorTipo());
                return true;
            }
            default -> {
                // en_cola, en_proceso o un estado nuevo: sigue pendiente.
                return false;
            }
        }
    }

    private void marcarFinal(AnalisisNoticia analisis, EstadoAnalisis estado, String tipo, String etapa,
            String mensaje, LocalDateTime ahora) {
        analisis.setEstado(estado);
        analisis.setErrorTipo(tipo);
        analisis.setErrorEtapa(etapa);
        analisis.setErrorMensaje(mensaje);
        analisis.setActualizado(ahora);
        analisis.setFinalizado(ahora);
    }

    private AnalisisResponseDTO aVista(AnalisisNoticia analisis) {
        Informe informe = analisis.getEstado() == EstadoAnalisis.COMPLETADO && analisis.getInformeJson() != null
                ? AletheiaJson.leer(analisis.getInformeJson(), Informe.class)
                : null;
        return mapper.toResponseDTO(analisis, informe);
    }
}
