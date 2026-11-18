package com.example.aletheia.pretest.dto.aletheia;

import java.time.LocalDate;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

/**
 * Cuerpo de POST /v1/analisis: exactamente los 8 campos del contrato (AletheIA rechaza
 * campos extra con 422). Los opcionales van como null, nunca como cadena vacía.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record NoticiaRequest(
        String texto,
        String titular,
        String subtitulo,
        LocalDate fechaPublicacion,
        String autor,
        String medio,
        String url,
        String idioma) {
}
