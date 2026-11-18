package com.example.aletheia.pretest.dto.aletheia;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

/**
 * Informe de un análisis completado (esquema 0.2.0). Las etiquetas van como String para
 * tolerar valores nuevos. {@code veredictoGlobal} puede ser null: entonces vale {@code motivoSinVeredicto}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Informe(
        String versionEsquema,
        String noticiaId,
        String idioma,
        String veredictoGlobal,
        String motivoSinVeredicto,
        String justificacionGlobal,
        String reglaAgregacionAplicada,
        DistorsionGlobal distorsionGlobal,
        List<Afirmacion> afirmaciones,
        List<String> limitaciones,
        Boolean verificacionExternaRealizada,
        Metadatos metadatos) {
}
