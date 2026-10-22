package com.example.aletheia.pretest.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Tipo de respuesta de un ítem. La etiqueta es la que usa el frontend ("likert_5", "texto"). */
public enum TipoRespuesta implements EnumConEtiqueta {
    LIKERT_5("likert_5"),
    TEXTO("texto");

    private final String etiqueta;

    TipoRespuesta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @JsonValue
    @Override
    public String getEtiqueta() {
        return etiqueta;
    }

    @JsonCreator
    public static TipoRespuesta desde(String valor) {
        return EnumConEtiqueta.desde(TipoRespuesta.class, valor);
    }

    /** Indica si la respuesta recibida es válida para este tipo de ítem. */
    public boolean esRespuestaValida(String respuesta) {
        if (respuesta == null || respuesta.isBlank()) {
            return false;
        }
        return switch (this) {
            case LIKERT_5 -> respuesta.trim().matches("[1-5]");
            case TEXTO -> true;
        };
    }
}
