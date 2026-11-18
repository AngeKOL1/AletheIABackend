package com.example.aletheia.pretest.dto;

import java.time.LocalDateTime;

import com.example.aletheia.pretest.models.enums.EstadoAnalisis;

/**
 * Estado de un análisis para el frontend. Si {@code estado} es COMPLETADO hay {@code informe};
 * si es ERROR o PERDIDO hay {@code error} y nunca un informe.
 *
 * @param estadoDetalle último estado informado por AletheIA mientras está pendiente (en_cola, en_proceso)
 */
public record AnalisisResponseDTO(
        Integer id,
        Integer noticiaId,
        EstadoAnalisis estado,
        String estadoDetalle,
        LocalDateTime creado,
        LocalDateTime actualizado,
        InformeVistaDTO informe,
        ErrorAnalisisVistaDTO error) {
}
