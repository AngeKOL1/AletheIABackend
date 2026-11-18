package com.example.aletheia.pretest.dto.aletheia;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Evidencia(
        String id,
        String url,
        String dominio,
        String titulo,
        String fuente,
        LocalDate fechaPublicacion,
        LocalDate fechaConsulta,
        String origenRecuperacion,
        String grupoOrigen,
        String tipoFuente,
        String postura,
        String fragmentoRelevante) {
}
