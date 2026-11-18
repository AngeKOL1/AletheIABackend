package com.example.aletheia.pretest.exception;

/** AletheIA respondió 404: el análisis no existe (p. ej. AletheIA se reinició con él en cola). */
public class AletheiaAnalisisNoEncontradoException extends AletheiaException {
    public AletheiaAnalisisNoEncontradoException(String message) {
        super(message);
    }
}
