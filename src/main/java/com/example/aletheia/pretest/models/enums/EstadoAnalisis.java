package com.example.aletheia.pretest.models.enums;

/** Estado de un análisis de AletheIA en este backend. */
public enum EstadoAnalisis {
    PENDIENTE, // enviado a AletheIA, esperando resultado (en cola o en proceso)
    COMPLETADO, // informe guardado
    ERROR, // AletheIA informó un error, o se superó el tiempo máximo
    PERDIDO; // AletheIA ya no conoce el análisis (404)

    /** Un análisis PENDIENTE o COMPLETADO se reutiliza para la misma noticia en lugar de pagar otro. */
    public boolean esReutilizable() {
        return this == PENDIENTE || this == COMPLETADO;
    }
}
