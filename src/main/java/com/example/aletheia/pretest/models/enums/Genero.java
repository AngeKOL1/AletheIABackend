package com.example.aletheia.pretest.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Genero implements EnumConEtiqueta {
    FEMENINO("Femenino"),
    MASCULINO("Masculino"),
    OTRO("Otro"),
    PREFIERO_NO_DECIRLO("Prefiero no decirlo");

    private final String etiqueta;

    Genero(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @JsonValue
    @Override
    public String getEtiqueta() {
        return etiqueta;
    }

    @JsonCreator
    public static Genero desde(String valor) {
        return EnumConEtiqueta.desde(Genero.class, valor);
    }
}
