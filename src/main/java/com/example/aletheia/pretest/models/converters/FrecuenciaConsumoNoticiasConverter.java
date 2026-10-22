package com.example.aletheia.pretest.models.converters;

import com.example.aletheia.pretest.models.enums.FrecuenciaConsumoNoticias;

import jakarta.persistence.Converter;

@Converter
public class FrecuenciaConsumoNoticiasConverter extends EnumConEtiquetaConverter<FrecuenciaConsumoNoticias> {
    public FrecuenciaConsumoNoticiasConverter() {
        super(FrecuenciaConsumoNoticias.class);
    }
}
