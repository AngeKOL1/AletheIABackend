package com.example.aletheia.pretest.dto;

import java.time.LocalDateTime;

import com.example.aletheia.pretest.models.enums.EstadoAnalisis;

/** Fila del listado "mis análisis" (sin el informe completo). */
public record AnalisisResumenDTO(
        Integer analisisId,
        Integer noticiaId,
        EstadoAnalisis estado,
        String veredictoGlobal,
        String veredictoGlobalEtiqueta,
        String motivoSinVeredicto,
        String motivoSinVeredictoEtiqueta,
        LocalDateTime actualizado) {
}
