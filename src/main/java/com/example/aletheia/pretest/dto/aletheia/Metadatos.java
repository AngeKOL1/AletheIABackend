package com.example.aletheia.pretest.dto.aletheia;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Metadatos(
        OffsetDateTime fechaAnalisis,
        String reglaAgregacion,
        List<Etapa> etapas,
        List<String> proveedoresRecuperacion) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Etapa(
            String etapa,
            String modelo,
            String versionPrompt) {
    }
}
