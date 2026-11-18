package com.example.aletheia.pretest.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FrecuenciaConsumoNoticias implements EnumConEtiqueta {
    VARIAS_VECES_AL_DIA("Varias veces al día"),
    UNA_VEZ_AL_DIA("Una vez al día"),
    VARIAS_VECES_POR_SEMANA("Varias veces por semana"),
    UNA_VEZ_POR_SEMANA("Una vez por semana"),
    CASI_NUNCA("Casi nunca");

    private final String etiqueta;

    FrecuenciaConsumoNoticias(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @JsonValue
    @Override
    public String getEtiqueta() {
        return etiqueta;
    }

    @JsonCreator
    public static FrecuenciaConsumoNoticias desde(String valor) {
        return EnumConEtiqueta.desde(FrecuenciaConsumoNoticias.class, valor);
    }
}
