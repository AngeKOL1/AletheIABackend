package com.example.aletheia.pretest.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Limit;

import com.example.aletheia.pretest.client.AletheiaClient;
import com.example.aletheia.pretest.client.AletheiaClientTest;
import com.example.aletheia.pretest.client.AletheiaJson;
import com.example.aletheia.pretest.config.AletheiaProperties;
import com.example.aletheia.pretest.dto.AfirmacionVistaDTO;
import com.example.aletheia.pretest.dto.AnalisisResponseDTO;
import com.example.aletheia.pretest.dto.aletheia.AnalisisResponse;
import com.example.aletheia.pretest.dto.aletheia.ErrorAnalisis;
import com.example.aletheia.pretest.dto.aletheia.NoticiaRequest;
import com.example.aletheia.pretest.exception.AletheiaAnalisisNoEncontradoException;
import com.example.aletheia.pretest.exception.AletheiaNoDisponibleException;
import com.example.aletheia.pretest.exception.AletheiaOcupadoException;
import com.example.aletheia.pretest.mapper.AnalisisMapperImpl;
import com.example.aletheia.pretest.models.AnalisisNoticia;
import com.example.aletheia.pretest.models.Noticia;
import com.example.aletheia.pretest.models.Participante;
import com.example.aletheia.pretest.models.SolicitudAnalisis;
import com.example.aletheia.pretest.models.enums.EstadoAnalisis;
import com.example.aletheia.pretest.models.enums.Forma;
import com.example.aletheia.pretest.repository.AnalisisNoticiaRepo;
import com.example.aletheia.pretest.repository.NoticiaRepo;
import com.example.aletheia.pretest.repository.ParticipanteRepo;
import com.example.aletheia.pretest.repository.SolicitudAnalisisRepo;

/** Pruebas del flujo asíncrono con AletheIA simulada (Mockito): sin BD ni servicio real. */
class AnalisisServiceTest {

    private static final String ID_ALETHEIA = "6c10e5e777a54d90886d8cc7d943b30e";
    private static final LocalDateTime AHORA = LocalDateTime.of(2026, 11, 16, 12, 0);
    private static final OffsetDateTime AHORA_UTC = OffsetDateTime.of(AHORA, ZoneOffset.UTC);

    private AnalisisNoticiaRepo repo;
    private SolicitudAnalisisRepo solicitudRepo;
    private NoticiaRepo noticiaRepo;
    private ParticipanteRepo participanteRepo;
    private AletheiaClient cliente;
    private AnalisisService service;

    @BeforeEach
    void preparar() {
        repo = mock(AnalisisNoticiaRepo.class);
        solicitudRepo = mock(SolicitudAnalisisRepo.class);
        noticiaRepo = mock(NoticiaRepo.class);
        participanteRepo = mock(ParticipanteRepo.class);
        cliente = mock(AletheiaClient.class);
        AletheiaProperties propiedades = new AletheiaProperties("http://aletheia.test", "clave", Duration.ofSeconds(5),
                Duration.ofSeconds(30), Duration.ofSeconds(10), Duration.ofMinutes(10), Duration.ofMinutes(1));
        Clock reloj = Clock.fixed(AHORA.toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));

        service = new AnalisisService(repo, solicitudRepo, noticiaRepo, participanteRepo, cliente,
                new AnalisisMapperImpl(), propiedades, reloj);

        when(repo.save(any())).thenAnswer(invocacion -> {
            AnalisisNoticia a = invocacion.getArgument(0);
            if (a.getId() == null) {
                a.setId(100);
            }
            return a;
        });
        when(participanteRepo.getReferenceById(anyInt())).thenReturn(new Participante());
    }

    // ---------- Tarea programada ----------

    @Test
    void pendienteACompletadoGuardaElInforme() throws IOException {
        AnalisisNoticia analisis = pendiente(AHORA.minusMinutes(1));
        AnalisisResponse completado = AletheiaJson.leer(
                AletheiaClientTest.fixtureCompleto(), AnalisisResponse.class);
        when(cliente.consultar(ID_ALETHEIA)).thenReturn(completado);

        service.actualizarEstado(1);

        assertThat(analisis.getEstado()).isEqualTo(EstadoAnalisis.COMPLETADO);
        assertThat(analisis.getVeredictoGlobal()).isEqualTo("falsa");
        assertThat(analisis.getInformeJson()).contains("\"veredicto_global\":\"falsa\"").contains("H-AG-3:R1");
        assertThat(analisis.getFinalizado()).isEqualTo(AHORA);
        assertThat(analisis.getErrorTipo()).isNull();
        verify(repo).save(analisis);
    }

    @Test
    void pendienteAErrorGuardaTipoEtapaYMensaje() {
        AnalisisNoticia analisis = pendiente(AHORA.minusMinutes(1));
        when(cliente.consultar(ID_ALETHEIA)).thenReturn(respuesta("error", null,
                new ErrorAnalisis("error_etapa", "extraccion", "el modelo no devolvió una respuesta válida")));

        service.actualizarEstado(1);

        assertThat(analisis.getEstado()).isEqualTo(EstadoAnalisis.ERROR);
        assertThat(analisis.getErrorTipo()).isEqualTo("error_etapa");
        assertThat(analisis.getErrorEtapa()).isEqualTo("extraccion");
        assertThat(analisis.getErrorMensaje()).isEqualTo("el modelo no devolvió una respuesta válida");
        assertThat(analisis.getInformeJson()).isNull();
        assertThat(analisis.getVeredictoGlobal()).isNull();
    }

    @Test
    void error404MarcaPerdido() {
        AnalisisNoticia analisis = pendiente(AHORA.minusMinutes(1));
        when(cliente.consultar(ID_ALETHEIA)).thenThrow(new AletheiaAnalisisNoEncontradoException("no encontrado"));

        service.actualizarEstado(1);

        assertThat(analisis.getEstado()).isEqualTo(EstadoAnalisis.PERDIDO);
        assertThat(analisis.getErrorTipo()).isEqualTo(AnalisisService.TIPO_ANALISIS_PERDIDO);
        verify(repo).save(analisis);
    }

    @Test
    void tiempoExcedidoMarcaError() {
        AnalisisNoticia analisis = pendiente(AHORA.minusMinutes(11));
        when(cliente.consultar(ID_ALETHEIA)).thenReturn(respuesta("en_proceso", null, null));

        service.actualizarEstado(1);

        assertThat(analisis.getEstado()).isEqualTo(EstadoAnalisis.ERROR);
        assertThat(analisis.getErrorTipo()).isEqualTo(AnalisisService.TIPO_TIEMPO_EXCEDIDO);
        assertThat(analisis.getVeredictoGlobal()).isNull();
    }

    @Test
    void tiempoExcedidoAunqueAletheiaNoResponda() {
        AnalisisNoticia analisis = pendiente(AHORA.minusMinutes(11));
        when(cliente.consultar(ID_ALETHEIA)).thenThrow(new AletheiaNoDisponibleException("caída"));

        service.actualizarEstado(1);

        assertThat(analisis.getEstado()).isEqualTo(EstadoAnalisis.ERROR);
        assertThat(analisis.getErrorTipo()).isEqualTo(AnalisisService.TIPO_TIEMPO_EXCEDIDO);
    }

    @Test
    void enProcesoSigueSiendoPendiente() {
        AnalisisNoticia analisis = pendiente(AHORA.minusMinutes(2));
        when(cliente.consultar(ID_ALETHEIA)).thenReturn(respuesta("en_proceso", null, null));

        service.actualizarEstado(1);

        assertThat(analisis.getEstado()).isEqualTo(EstadoAnalisis.PENDIENTE);
        assertThat(analisis.getEstadoAletheia()).isEqualTo("en_proceso");
    }

    @Test
    void aletheiaCaidaDentroDelTiempoSigueSiendoPendiente() {
        AnalisisNoticia analisis = pendiente(AHORA.minusMinutes(2));
        when(cliente.consultar(ID_ALETHEIA)).thenThrow(new AletheiaNoDisponibleException("caída"));

        service.actualizarEstado(1);

        assertThat(analisis.getEstado()).isEqualTo(EstadoAnalisis.PENDIENTE);
    }

    @Test
    void completadoSinInformeEsError() {
        AnalisisNoticia analisis = pendiente(AHORA.minusMinutes(1));
        when(cliente.consultar(ID_ALETHEIA)).thenReturn(respuesta("completado", null, null));

        service.actualizarEstado(1);

        assertThat(analisis.getEstado()).isEqualTo(EstadoAnalisis.ERROR);
        assertThat(analisis.getErrorTipo()).isEqualTo(AnalisisService.TIPO_INFORME_AUSENTE);
        assertThat(analisis.getVeredictoGlobal()).isNull();
    }

    @Test
    void unAnalisisYaTerminadoNoSeVuelveAConsultar() {
        AnalisisNoticia analisis = pendiente(AHORA.minusMinutes(1));
        analisis.setEstado(EstadoAnalisis.COMPLETADO);

        service.actualizarEstado(1);

        verify(cliente, never()).consultar(any());
    }

    // ---------- Solicitudes del usuario ----------

    @Test
    void solicitarEnviaLaNoticiaYGuardaPendiente() {
        when(noticiaRepo.findByIdParaAnalisis(7)).thenReturn(Optional.of(noticia()));
        when(repo.findFirstByNoticiaIdOrderByIdDesc(7)).thenReturn(Optional.empty());
        when(cliente.crear(any())).thenReturn(respuesta("en_cola", null, null));

        AnalisisResponseDTO dto = service.solicitar(3, 7);

        assertThat(dto.estado()).isEqualTo(EstadoAnalisis.PENDIENTE);
        assertThat(dto.estadoDetalle()).isEqualTo("en_cola");
        assertThat(dto.informe()).isNull();
        assertThat(dto.error()).isNull();
        verify(cliente).crear(eq(new NoticiaRequest("Entradilla.\n\nCuerpo de la noticia.", "Titular", null,
                null, null, "Medio", null, "es")));
        verify(solicitudRepo).save(any(SolicitudAnalisis.class));
    }

    @Test
    void solicitarReutilizaUnAnalisisCompletadoSinPagarOtro() throws IOException {
        AnalisisNoticia existente = completado();
        when(noticiaRepo.findByIdParaAnalisis(7)).thenReturn(Optional.of(noticia()));
        when(repo.findFirstByNoticiaIdOrderByIdDesc(7)).thenReturn(Optional.of(existente));

        AnalisisResponseDTO dto = service.solicitar(3, 7);

        verify(cliente, never()).crear(any());
        assertThat(dto.estado()).isEqualTo(EstadoAnalisis.COMPLETADO);
        assertThat(dto.informe().veredictoGlobalEtiqueta()).isEqualTo("falsa");
    }

    @Test
    void siAletheiaFallaAlCrearNoSeGuardaNada() {
        when(noticiaRepo.findByIdParaAnalisis(7)).thenReturn(Optional.of(noticia()));
        when(repo.findFirstByNoticiaIdOrderByIdDesc(7)).thenReturn(Optional.empty());
        when(cliente.crear(any())).thenThrow(new AletheiaOcupadoException("ocupado", 60));

        assertThatThrownBy(() -> service.solicitar(3, 7)).isInstanceOf(AletheiaOcupadoException.class);
        verify(repo, never()).save(any());
        verify(solicitudRepo, never()).save(any());
    }

    @Test
    void noSeReenviaEnseguidaTrasUnError() {
        AnalisisNoticia fallido = pendiente(AHORA.minusMinutes(5));
        fallido.setEstado(EstadoAnalisis.ERROR);
        fallido.setActualizado(AHORA.minusSeconds(20));
        when(noticiaRepo.findByIdParaAnalisis(7)).thenReturn(Optional.of(noticia()));
        when(repo.findFirstByNoticiaIdOrderByIdDesc(7)).thenReturn(Optional.of(fallido));

        assertThatThrownBy(() -> service.solicitar(3, 7)).isInstanceOf(IllegalStateException.class);
        verify(cliente, never()).crear(any());
    }

    @Test
    void noticiaSinTextoNoSeEnvia() {
        Noticia vacia = noticia();
        vacia.setEntradilla(" ");
        vacia.setCuerpo(null);
        when(noticiaRepo.findByIdParaAnalisis(7)).thenReturn(Optional.of(vacia));
        when(repo.findFirstByNoticiaIdOrderByIdDesc(7)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.solicitar(3, 7)).isInstanceOf(IllegalArgumentException.class);
        verify(cliente, never()).crear(any());
    }

    // ---------- Presentación ----------

    @Test
    void veredictoNullMuestraMotivoNuncaNoVerificable() throws IOException {
        AnalisisNoticia analisis = completado();
        analisis.setInformeJson(analisis.getInformeJson()
                .replace("\"veredicto_global\":\"falsa\"", "\"veredicto_global\":null")
                .replace("\"motivo_sin_veredicto\":null", "\"motivo_sin_veredicto\":\"sin_afirmaciones_factuales\""));
        when(solicitudRepo.findAnalisisDeParticipante(eq(3), eq(7), any(Limit.class))).thenReturn(List.of(analisis));

        AnalisisResponseDTO dto = service.consultar(3, 7);

        assertThat(dto.informe().veredictoGlobal()).isNull();
        assertThat(dto.informe().veredictoGlobalEtiqueta()).isNull();
        assertThat(dto.informe().motivoSinVeredicto()).isEqualTo("sin_afirmaciones_factuales");
        assertThat(dto.informe().motivoSinVeredictoEtiqueta()).doesNotContain("no verificable");
    }

    @Test
    void presentacionDelInforme() throws IOException {
        AnalisisNoticia analisis = completado();
        when(solicitudRepo.findAnalisisDeParticipante(eq(3), eq(7), any(Limit.class))).thenReturn(List.of(analisis));

        AnalisisResponseDTO dto = service.consultar(3, 7);

        assertThat(dto.informe().limitaciones()).isNotEmpty();
        AfirmacionVistaDTO factual = dto.informe().afirmaciones().get(0);
        assertThat(factual.veredictoEtiqueta()).isEqualTo("falsa");
        assertThat(factual.solidezEvidencia()).isEqualTo("alta");
        assertThat(factual.justificacion()).isNotBlank();
        assertThat(factual.evidencias().get(0).url()).isEqualTo("https://science.nasa.gov/mars/moons/");
        assertThat(factual.evidencias().get(0).fuente()).isEqualTo("NASA");
        assertThat(factual.evidencias().get(0).fragmentoRelevante()).isNotBlank();

        AfirmacionVistaDTO opinion = dto.informe().afirmaciones().get(1);
        assertThat(opinion.tipo()).isEqualTo("opinion");
        assertThat(opinion.veredicto()).isNull();
        assertThat(opinion.veredictoEtiqueta()).isNull();
    }

    @Test
    void etiquetaEnganosaLlevaEnie() {
        assertThat(new AnalisisMapperImpl().etiquetaVeredicto("enganosa")).isEqualTo("engañosa");
        assertThat(new AnalisisMapperImpl().etiquetaVeredicto("no_verificable")).isEqualTo("no verificable");
    }

    @Test
    void unAnalisisConErrorNuncaTraeInforme() {
        AnalisisNoticia analisis = pendiente(AHORA.minusMinutes(1));
        analisis.setEstado(EstadoAnalisis.ERROR);
        analisis.setErrorTipo("error_interno");
        analisis.setErrorMensaje("fallo");
        when(solicitudRepo.findAnalisisDeParticipante(eq(3), eq(7), any(Limit.class))).thenReturn(List.of(analisis));

        AnalisisResponseDTO dto = service.consultar(3, 7);

        assertThat(dto.informe()).isNull();
        assertThat(dto.error().tipo()).isEqualTo("error_interno");
    }

    // ---------- Ayudantes ----------

    private AnalisisNoticia pendiente(LocalDateTime creado) {
        AnalisisNoticia a = new AnalisisNoticia();
        a.setId(1);
        Noticia noticia = noticia();
        a.setNoticia(noticia);
        a.setAletheiaId(ID_ALETHEIA);
        a.setEstado(EstadoAnalisis.PENDIENTE);
        a.setEstadoAletheia("en_cola");
        a.setCreado(creado);
        a.setActualizado(creado);
        when(repo.findById(1)).thenReturn(Optional.of(a));
        return a;
    }

    private AnalisisNoticia completado() throws IOException {
        AnalisisNoticia a = pendiente(AHORA.minusMinutes(2));
        AnalisisResponse respuesta = AletheiaJson.leer(AletheiaClientTest.fixtureCompleto(), AnalisisResponse.class);
        a.setEstado(EstadoAnalisis.COMPLETADO);
        a.setInformeJson(AletheiaJson.escribir(respuesta.informe()));
        a.setVeredictoGlobal("falsa");
        return a;
    }

    private static Noticia noticia() {
        Noticia n = new Noticia();
        n.setId(7);
        n.setTitular("Titular");
        n.setEntradilla("Entradilla.");
        n.setCuerpo("Cuerpo de la noticia.");
        n.setFuente("Medio");
        n.setImg("/img/1.jpg");
        n.setForma(Forma.A);
        return n;
    }

    private static AnalisisResponse respuesta(String estado,
            com.example.aletheia.pretest.dto.aletheia.Informe informe, ErrorAnalisis error) {
        return new AnalisisResponse(ID_ALETHEIA, estado, AHORA_UTC, AHORA_UTC, informe, error);
    }
}
