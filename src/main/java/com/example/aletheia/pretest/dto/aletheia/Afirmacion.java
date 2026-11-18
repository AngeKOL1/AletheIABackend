package com.example.aletheia.pretest.dto.aletheia;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

/**
 * Afirmación extraída de la noticia. {@code solidezEvidencia} (alta | media | baja | nula) no es
 * una probabilidad de acierto. Las de tipo opinion o prediccion no tienen veredicto.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Afirmacion(
        String id,
        String texto,
        String ubicacion,
        String posturaNoticia,
        Boolean central,
        String tipo,
        String estado,
        String recuperacion,
        String veredicto,
        String tipoProblema,
        List<Evidencia> evidencias,
        String solidezEvidencia,
        String justificacion) {
}
