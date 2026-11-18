package com.example.aletheia.pretest.dto;

/**
 * Error de un análisis. tipo: error_etapa | error_interno (de AletheIA), tiempo_excedido,
 * informe_ausente o analisis_perdido (de este backend).
 */
public record ErrorAnalisisVistaDTO(
        String tipo,
        String etapa,
        String mensaje) {
}
