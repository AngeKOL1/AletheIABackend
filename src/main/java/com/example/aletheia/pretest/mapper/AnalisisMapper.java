package com.example.aletheia.pretest.mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.example.aletheia.pretest.dto.AfirmacionVistaDTO;
import com.example.aletheia.pretest.dto.AnalisisResponseDTO;
import com.example.aletheia.pretest.dto.AnalisisResumenDTO;
import com.example.aletheia.pretest.dto.DistorsionVistaDTO;
import com.example.aletheia.pretest.dto.ErrorAnalisisVistaDTO;
import com.example.aletheia.pretest.dto.EvidenciaVistaDTO;
import com.example.aletheia.pretest.dto.InformeVistaDTO;
import com.example.aletheia.pretest.dto.aletheia.Afirmacion;
import com.example.aletheia.pretest.dto.aletheia.DistorsionGlobal;
import com.example.aletheia.pretest.dto.aletheia.Evidencia;
import com.example.aletheia.pretest.dto.aletheia.Informe;
import com.example.aletheia.pretest.dto.aletheia.NoticiaRequest;
import com.example.aletheia.pretest.models.AnalisisNoticia;
import com.example.aletheia.pretest.models.Noticia;
import com.example.aletheia.pretest.models.enums.EstadoAnalisis;
import com.example.aletheia.pretest.repository.AnalisisResumenProyeccion;

@Mapper(componentModel = "spring")
public interface AnalisisMapper {

    /** Las noticias del estudio están en español (las traducidas también). */
    String IDIOMA_NOTICIAS = "es";

    Map<String, String> ETIQUETAS_VEREDICTO = Map.of(
            "verdadera", "verdadera",
            "falsa", "falsa",
            "enganosa", "engañosa",
            "no_verificable", "no verificable");

    Map<String, String> ETIQUETAS_MOTIVO = Map.of(
            "sin_afirmaciones_factuales", "la noticia no contiene afirmaciones factuales que verificar",
            "analisis_incompleto", "el análisis no pudo completarse",
            "caso_no_cubierto", "caso no cubierto por las reglas de veredicto");

    // ---------- Noticia -> petición a AletheIA ----------

    /**
     * Solo los 8 campos del contrato. El texto une entradilla, cuerpo y cierre; los campos
     * vacíos van como null (AletheIA rechaza cadenas vacías). No hay autor ni URL de la noticia.
     */
    default NoticiaRequest toNoticiaRequest(Noticia noticia) {
        String texto = Stream.of(noticia.getEntradilla(), noticia.getCuerpo(), noticia.getCierre())
                .map(AnalisisMapper::limpiar)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n\n"));
        LocalDate fecha = noticia.getFechaPublicacion();
        return new NoticiaRequest(
                texto.isEmpty() ? null : texto,
                limpiar(noticia.getTitular()),
                limpiar(noticia.getSubtitulo()),
                fecha,
                null,
                limpiar(noticia.getFuente()),
                null,
                IDIOMA_NOTICIAS);
    }

    // ---------- Entidad + informe -> vista ----------

    @Mapping(target = "id", source = "analisis.id")
    @Mapping(target = "noticiaId", source = "analisis.noticia.id")
    @Mapping(target = "estado", source = "analisis.estado")
    @Mapping(target = "estadoDetalle", source = "analisis.estadoAletheia")
    @Mapping(target = "creado", source = "analisis.creado")
    @Mapping(target = "actualizado", source = "analisis.actualizado")
    @Mapping(target = "informe", source = "informe")
    @Mapping(target = "error", source = "analisis")
    AnalisisResponseDTO toResponseDTO(AnalisisNoticia analisis, Informe informe);

    /** Solo hay error si el análisis terminó en ERROR o PERDIDO. */
    default ErrorAnalisisVistaDTO toErrorVista(AnalisisNoticia analisis) {
        if (analisis == null || (analisis.getEstado() != EstadoAnalisis.ERROR
                && analisis.getEstado() != EstadoAnalisis.PERDIDO)) {
            return null;
        }
        return new ErrorAnalisisVistaDTO(analisis.getErrorTipo(), analisis.getErrorEtapa(), analisis.getErrorMensaje());
    }

    @Mapping(target = "veredictoGlobalEtiqueta", source = "veredictoGlobal", qualifiedByName = "etiquetaVeredicto")
    @Mapping(target = "motivoSinVeredicto",
            expression = "java(informe.veredictoGlobal() == null ? informe.motivoSinVeredicto() : null)")
    @Mapping(target = "motivoSinVeredictoEtiqueta",
            expression = "java(informe.veredictoGlobal() == null ? etiquetaMotivo(informe.motivoSinVeredicto()) : null)")
    @Mapping(target = "afirmaciones",
            expression = "java(informe.afirmaciones() == null ? java.util.List.of() : toAfirmacionesVista(informe.afirmaciones()))")
    @Mapping(target = "limitaciones",
            expression = "java(informe.limitaciones() == null ? java.util.List.of() : java.util.List.copyOf(informe.limitaciones()))")
    @Mapping(target = "fechaAnalisis", source = "metadatos.fechaAnalisis")
    InformeVistaDTO toInformeVista(Informe informe);

    DistorsionVistaDTO toDistorsionVista(DistorsionGlobal distorsion);

    List<AfirmacionVistaDTO> toAfirmacionesVista(List<Afirmacion> afirmaciones);

    /** Las afirmaciones que no son factuales (opinión, predicción) no tienen veredicto. */
    @Mapping(target = "veredicto", expression = "java(esFactual(afirmacion) ? afirmacion.veredicto() : null)")
    @Mapping(target = "veredictoEtiqueta",
            expression = "java(esFactual(afirmacion) ? etiquetaVeredicto(afirmacion.veredicto()) : null)")
    @Mapping(target = "evidencias",
            expression = "java(afirmacion.evidencias() == null ? java.util.List.of() : toEvidenciasVista(afirmacion.evidencias()))")
    AfirmacionVistaDTO toAfirmacionVista(Afirmacion afirmacion);

    List<EvidenciaVistaDTO> toEvidenciasVista(List<Evidencia> evidencias);

    EvidenciaVistaDTO toEvidenciaVista(Evidencia evidencia);

    // ---------- Listado ----------

    @Mapping(target = "veredictoGlobalEtiqueta", source = "veredictoGlobal", qualifiedByName = "etiquetaVeredicto")
    @Mapping(target = "motivoSinVeredicto",
            expression = "java(fila.getVeredictoGlobal() == null ? fila.getMotivoSinVeredicto() : null)")
    @Mapping(target = "motivoSinVeredictoEtiqueta",
            expression = "java(fila.getVeredictoGlobal() == null ? etiquetaMotivo(fila.getMotivoSinVeredicto()) : null)")
    AnalisisResumenDTO toResumenDTO(AnalisisResumenProyeccion fila);

    List<AnalisisResumenDTO> toResumenDTOList(List<AnalisisResumenProyeccion> filas);

    // ---------- Etiquetas ----------

    /** Etiqueta para el usuario; un valor desconocido se muestra tal cual (no se inventa). */
    @Named("etiquetaVeredicto")
    default String etiquetaVeredicto(String veredicto) {
        return veredicto == null ? null : ETIQUETAS_VEREDICTO.getOrDefault(veredicto, veredicto);
    }

    @Named("etiquetaMotivo")
    default String etiquetaMotivo(String motivo) {
        return motivo == null ? null : ETIQUETAS_MOTIVO.getOrDefault(motivo, motivo);
    }

    @Named("esFactual")
    default boolean esFactual(Afirmacion afirmacion) {
        return "factual".equals(afirmacion.tipo());
    }

    private static String limpiar(String valor) {
        return (valor == null || valor.isBlank()) ? null : valor.trim();
    }
}
