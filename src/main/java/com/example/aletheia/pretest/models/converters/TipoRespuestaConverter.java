package com.example.aletheia.pretest.models.converters;

import com.example.aletheia.pretest.models.enums.TipoRespuesta;

import jakarta.persistence.Converter;

@Converter
public class TipoRespuestaConverter extends EnumConEtiquetaConverter<TipoRespuesta> {
    public TipoRespuestaConverter() {
        super(TipoRespuesta.class);
    }
}
