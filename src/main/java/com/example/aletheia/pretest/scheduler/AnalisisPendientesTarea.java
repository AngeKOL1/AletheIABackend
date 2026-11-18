package com.example.aletheia.pretest.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.aletheia.pretest.service.IAnalisisService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** Consulta periódicamente los análisis pendientes. Toda la lógica está en el servicio. */
@Slf4j
@Component
@RequiredArgsConstructor
public class AnalisisPendientesTarea {

    private final IAnalisisService service;

    @Scheduled(initialDelayString = "${aletheia.intervalo-consulta:10s}",
            fixedDelayString = "${aletheia.intervalo-consulta:10s}")
    public void consultarPendientes() {
        for (Integer id : service.idsPendientes()) {
            try {
                service.actualizarEstado(id);
            } catch (RuntimeException e) {
                // Un análisis con problemas no debe frenar a los demás.
                log.error("Error al actualizar el análisis {}", id, e);
            }
        }
    }
}
