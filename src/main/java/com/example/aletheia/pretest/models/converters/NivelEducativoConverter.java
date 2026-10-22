package com.example.aletheia.pretest.models.converters;

import com.example.aletheia.pretest.models.enums.NivelEducativo;

import jakarta.persistence.Converter;

@Converter
public class NivelEducativoConverter extends EnumConEtiquetaConverter<NivelEducativo> {
    public NivelEducativoConverter() {
        super(NivelEducativo.class);
    }
}
