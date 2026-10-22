package com.example.aletheia.pretest.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NivelEducativo implements EnumConEtiqueta {
    SECUNDARIA_COMPLETA("Secundaria completa"),
    TECNICO_SUPERIOR("Técnico superior"),
    UNIVERSITARIO_EN_CURSO("Universitario en curso"),
    UNIVERSITARIO_COMPLETO("Universitario completo"),
    POSGRADO("Posgrado");

    private final String etiqueta;

    NivelEducativo(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @JsonValue
    @Override
    public String getEtiqueta() {
        return etiqueta;
    }

    @JsonCreator
    public static NivelEducativo desde(String valor) {
        return EnumConEtiqueta.desde(NivelEducativo.class, valor);
    }
}
