package com.example.aletheia.pretest.models.converters;

import com.example.aletheia.pretest.models.enums.Genero;

import jakarta.persistence.Converter;

@Converter
public class GeneroConverter extends EnumConEtiquetaConverter<Genero> {
    public GeneroConverter() {
        super(Genero.class);
    }
}
