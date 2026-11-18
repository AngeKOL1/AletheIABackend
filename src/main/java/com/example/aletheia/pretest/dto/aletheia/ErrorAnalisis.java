package com.example.aletheia.pretest.dto.aletheia;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

/** Error de un análisis. tipo: error_etapa | error_interno; etapa: extraccion | priorizacion | consultas | null. */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ErrorAnalisis(
        String tipo,
        String etapa,
        String mensaje) {
}
