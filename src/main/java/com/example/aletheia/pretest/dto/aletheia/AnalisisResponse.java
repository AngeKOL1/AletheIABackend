package com.example.aletheia.pretest.dto.aletheia;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

/**
 * Respuesta de POST y GET /v1/analisis. {@code estado}: en_cola | en_proceso | completado | error
 * (como String para tolerar estados nuevos).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AnalisisResponse(
        String id,
        String estado,
        OffsetDateTime creado,
        OffsetDateTime actualizado,
        Informe informe,
        ErrorAnalisis error) {

    public static final String EN_COLA = "en_cola";
    public static final String EN_PROCESO = "en_proceso";
    public static final String COMPLETADO = "completado";
    public static final String ERROR = "error";
}
