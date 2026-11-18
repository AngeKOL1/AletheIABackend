package com.example.aletheia.pretest.dto;

import java.util.List;

/**
 * Afirmación para mostrar. Solo las de tipo "factual" llevan veredicto.
 * {@code solidezEvidencia} (alta | media | baja | nula) es una etiqueta, no un porcentaje ni una confianza.
 */
public record AfirmacionVistaDTO(
        String id,
        String texto,
        String ubicacion,
        String tipo,
        Boolean central,
        String estado,
        String veredicto,
        String veredictoEtiqueta,
        String tipoProblema,
        String solidezEvidencia,
        String justificacion,
        List<EvidenciaVistaDTO> evidencias) {
}
