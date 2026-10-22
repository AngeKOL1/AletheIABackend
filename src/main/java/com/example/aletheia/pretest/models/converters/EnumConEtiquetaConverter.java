package com.example.aletheia.pretest.models.converters;

import com.example.aletheia.pretest.models.enums.EnumConEtiqueta;

import jakarta.persistence.AttributeConverter;

/** Guarda en la base de datos la etiqueta del enum (compatible con los datos ya existentes). */
public abstract class EnumConEtiquetaConverter<E extends Enum<E> & EnumConEtiqueta>
        implements AttributeConverter<E, String> {

    private final Class<E> tipo;

    protected EnumConEtiquetaConverter(Class<E> tipo) {
        this.tipo = tipo;
    }

    @Override
    public String convertToDatabaseColumn(E valor) {
        return valor == null ? null : valor.getEtiqueta();
    }

    @Override
    public E convertToEntityAttribute(String columna) {
        return EnumConEtiqueta.desde(tipo, columna);
    }
}
