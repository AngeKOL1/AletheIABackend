package com.example.aletheia.pretest.exception;

/** AletheIA respondió 401: la clave configurada (ALETHEIA_API_CLAVE) falta o es incorrecta. No es culpa del usuario. */
public class AletheiaConfiguracionException extends AletheiaException {
    public AletheiaConfiguracionException(String message) {
        super(message);
    }
}
