package com.example.aletheia.pretest.exception;

/** AletheIA respondió 503: demasiados análisis pendientes. {@code reintentarEnSegundos} viene de Retry-After (puede ser null). */
public class AletheiaOcupadoException extends AletheiaException {
    private final Integer reintentarEnSegundos;

    public AletheiaOcupadoException(String message, Integer reintentarEnSegundos) {
        super(message);
        this.reintentarEnSegundos = reintentarEnSegundos;
    }

    public Integer getReintentarEnSegundos() {
        return reintentarEnSegundos;
    }
}
