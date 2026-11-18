package com.example.aletheia.pretest.exception;

/** No se pudo hablar con AletheIA: error de red, timeout, error 5xx o respuesta inesperada. */
public class AletheiaNoDisponibleException extends AletheiaException {
    public AletheiaNoDisponibleException(String message) {
        super(message);
    }

    public AletheiaNoDisponibleException(String message, Throwable cause) {
        super(message, cause);
    }
}
