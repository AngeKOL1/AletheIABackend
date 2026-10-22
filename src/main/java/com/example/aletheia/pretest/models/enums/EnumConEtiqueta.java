package com.example.aletheia.pretest.models.enums;

/**
 * Enum cuyo valor "público" (JSON y base de datos) es una etiqueta legible,
 * p. ej. "Femenino" o "likert_5", en lugar del nombre de la constante.
 */
public interface EnumConEtiqueta {

    String getEtiqueta();

    /** Busca por etiqueta o por nombre de constante (sin distinguir mayúsculas). */
    static <E extends Enum<E> & EnumConEtiqueta> E desde(Class<E> tipo, String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim();
        for (E constante : tipo.getEnumConstants()) {
            if (constante.getEtiqueta().equalsIgnoreCase(limpio) || constante.name().equalsIgnoreCase(limpio)) {
                return constante;
            }
        }
        throw new IllegalArgumentException("Valor no permitido para " + tipo.getSimpleName() + ": " + valor);
    }
}
