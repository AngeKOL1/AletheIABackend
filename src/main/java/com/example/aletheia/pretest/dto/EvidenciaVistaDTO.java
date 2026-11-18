package com.example.aletheia.pretest.dto;

import java.time.LocalDate;

public record EvidenciaVistaDTO(
        String url,
        String dominio,
        String titulo,
        String fuente,
        LocalDate fechaPublicacion,
        String tipoFuente,
        String postura,
        String fragmentoRelevante) {
}
