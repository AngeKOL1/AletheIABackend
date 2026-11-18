package com.example.aletheia.pretest.client;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

/**
 * Conversión JSON dedicada a AletheIA. No es un bean: así no reemplaza el JsonMapper global
 * del proyecto. El snake_case lo aportan los @JsonNaming de los DTO de AletheIA.
 */
public final class AletheiaJson {

    private static final JsonMapper MAPPER = JsonMapper.builder().build();

    private AletheiaJson() {
    }

    public static byte[] escribirBytes(Object valor) {
        return MAPPER.writeValueAsBytes(valor);
    }

    public static String escribir(Object valor) {
        return MAPPER.writeValueAsString(valor);
    }

    public static <T> T leer(byte[] json, Class<T> tipo) {
        return MAPPER.readValue(json, tipo);
    }

    public static <T> T leer(String json, Class<T> tipo) {
        return MAPPER.readValue(json, tipo);
    }

    public static JsonNode leerArbol(byte[] json) {
        return MAPPER.readTree(json);
    }
}
