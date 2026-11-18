package com.example.aletheia.pretest.repository;

import java.time.LocalDateTime;

import com.example.aletheia.pretest.models.enums.EstadoAnalisis;

/** Datos mínimos para listar los análisis de un participante (sin el informe JSON). */
public interface AnalisisResumenProyeccion {
    Integer getAnalisisId();

    Integer getNoticiaId();

    EstadoAnalisis getEstado();

    String getVeredictoGlobal();

    String getMotivoSinVeredicto();

    LocalDateTime getActualizado();
}
