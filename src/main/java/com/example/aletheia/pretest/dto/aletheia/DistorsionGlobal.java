package com.example.aletheia.pretest.dto.aletheia;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

/** tipo: contradiccion_titular_cuerpo | titular_no_sustentado | omision_contextual | ninguna. */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DistorsionGlobal(
        Boolean detectada,
        String tipo,
        List<String> afirmacionesImplicadas,
        List<String> evidenciasImplicadas,
        String justificacion) {
}
