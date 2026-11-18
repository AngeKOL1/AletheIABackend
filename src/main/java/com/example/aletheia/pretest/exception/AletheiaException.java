package com.example.aletheia.pretest.exception;

/**
 * Error al comunicarse con AletheIA. Nunca representa un resultado del análisis.
 * Los mensajes no incluyen la clave de API.
 */
public abstract class AletheiaException extends RuntimeException {
    protected AletheiaException(String message) {
        super(message);
    }

    protected AletheiaException(String message, Throwable cause) {
        super(message, cause);
    }
}
