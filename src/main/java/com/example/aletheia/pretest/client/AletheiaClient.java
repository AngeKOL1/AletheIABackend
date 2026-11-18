package com.example.aletheia.pretest.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.example.aletheia.pretest.dto.aletheia.AnalisisResponse;
import com.example.aletheia.pretest.dto.aletheia.NoticiaRequest;
import com.example.aletheia.pretest.exception.AletheiaAnalisisNoEncontradoException;
import com.example.aletheia.pretest.exception.AletheiaConfiguracionException;
import com.example.aletheia.pretest.exception.AletheiaException;
import com.example.aletheia.pretest.exception.AletheiaNoDisponibleException;
import com.example.aletheia.pretest.exception.AletheiaNoticiaInvalidaException;
import com.example.aletheia.pretest.exception.AletheiaOcupadoException;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;

/**
 * Cliente HTTP de AletheIA. Solo lo usa el servicio de análisis; el navegador nunca habla con AletheIA.
 * Cualquier respuesta que no sea la esperada se convierte en una excepción propia, nunca en un resultado.
 */
@Slf4j
@Component
public class AletheiaClient {

    // Los ids de AletheIA son uuid4 en hexadecimal.
    private static final Pattern FORMATO_ID = Pattern.compile("^[0-9a-f]{32}$");

    private final RestClient restClient;

    public AletheiaClient(@Qualifier("aletheiaRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    /** POST /v1/analisis: encola la noticia. Espera 202 con el id del análisis. */
    public AnalisisResponse crear(NoticiaRequest noticia) {
        ResponseEntity<byte[]> respuesta = ejecutar("crear el análisis", () -> restClient.post()
                .uri("/v1/analisis")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(AletheiaJson.escribirBytes(noticia))
                .retrieve()
                .onStatus(estado -> estado.isError(), this::lanzarError)
                .toEntity(byte[].class));

        if (respuesta.getStatusCode().value() != HttpStatus.ACCEPTED.value()) {
            throw new AletheiaNoDisponibleException(
                    "AletheIA respondió " + respuesta.getStatusCode().value() + " al crear el análisis (se esperaba 202)");
        }
        AnalisisResponse analisis = leerAnalisis(respuesta.getBody());
        log.info("Análisis creado en AletheIA: {} ({})", analisis.id(), analisis.estado());
        return analisis;
    }

    /** GET /v1/analisis/{id}: estado del análisis y, si terminó, el informe o el error. */
    public AnalisisResponse consultar(String id) {
        if (id == null || !FORMATO_ID.matcher(id).matches()) {
            throw new IllegalArgumentException("Id de análisis de AletheIA inválido");
        }
        ResponseEntity<byte[]> respuesta = ejecutar("consultar el análisis", () -> restClient.get()
                .uri("/v1/analisis/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(estado -> estado.isError(), this::lanzarError)
                .toEntity(byte[].class));
        return leerAnalisis(respuesta.getBody());
    }

    /** GET /salud: true solo si AletheIA responde {"estado": "ok"}. Nunca lanza excepción. */
    public boolean salud() {
        try {
            byte[] cuerpo = restClient.get().uri("/salud").retrieve().body(byte[].class);
            return cuerpo != null && "ok".equals(AletheiaJson.leerArbol(cuerpo).path("estado").asString());
        } catch (RuntimeException e) {
            log.warn("AletheIA no responde a /salud: {}", e.getMessage());
            return false;
        }
    }

    private ResponseEntity<byte[]> ejecutar(String accion, Supplier<ResponseEntity<byte[]>> llamada) {
        try {
            return llamada.get();
        } catch (AletheiaException e) {
            throw e;
        } catch (RestClientException e) {
            // Red caída, timeout o respuesta ilegible.
            throw new AletheiaNoDisponibleException("No se pudo " + accion + ": AletheIA no está disponible", e);
        }
    }

    private AnalisisResponse leerAnalisis(byte[] cuerpo) {
        if (cuerpo == null || cuerpo.length == 0) {
            throw new AletheiaNoDisponibleException("AletheIA respondió sin cuerpo");
        }
        AnalisisResponse analisis;
        try {
            analisis = AletheiaJson.leer(cuerpo, AnalisisResponse.class);
        } catch (JacksonException e) {
            throw new AletheiaNoDisponibleException("AletheIA respondió con un JSON que no se pudo leer", e);
        }
        if (analisis.id() == null || !FORMATO_ID.matcher(analisis.id()).matches() || analisis.estado() == null) {
            throw new AletheiaNoDisponibleException("AletheIA respondió sin id o sin estado válidos");
        }
        return analisis;
    }

    /** Traduce los códigos de error de AletheIA a excepciones propias. */
    private void lanzarError(HttpRequest peticion, ClientHttpResponse respuesta) throws IOException {
        int codigo = respuesta.getStatusCode().value();
        JsonNode detalle = leerDetalle(respuesta);

        switch (codigo) {
            case 401 -> {
                log.error("AletheIA rechazó la clave de API: revisa la variable ALETHEIA_API_CLAVE");
                throw new AletheiaConfiguracionException("AletheIA rechazó la clave de API configurada");
            }
            case 404 -> throw new AletheiaAnalisisNoEncontradoException("AletheIA no encontró el análisis");
            case 422 -> throw new AletheiaNoticiaInvalidaException(describirDetalle(detalle));
            case 503 -> throw new AletheiaOcupadoException(
                    "AletheIA tiene demasiados análisis pendientes; inténtalo más tarde",
                    leerRetryAfter(respuesta.getHeaders()));
            default -> throw new AletheiaNoDisponibleException("AletheIA respondió con el código " + codigo);
        }
    }

    private static JsonNode leerDetalle(ClientHttpResponse respuesta) {
        try {
            byte[] cuerpo = respuesta.getBody().readAllBytes();
            return cuerpo.length == 0 ? null : AletheiaJson.leerArbol(cuerpo).get("detail");
        } catch (IOException | JacksonException e) {
            return null;
        }
    }

    /** "detail" puede ser un texto o una lista de errores de validación ({loc, msg, type}). */
    static String describirDetalle(JsonNode detalle) {
        if (detalle == null || detalle.isNull()) {
            return "AletheIA rechazó la noticia";
        }
        if (detalle.isString()) {
            return detalle.asString();
        }
        if (detalle.isArray()) {
            List<String> partes = new ArrayList<>();
            for (JsonNode error : detalle) {
                String mensaje = error.path("msg").asString("dato inválido");
                List<String> ruta = new ArrayList<>();
                for (JsonNode paso : error.path("loc")) {
                    if (!"body".equals(paso.asString())) {
                        ruta.add(paso.asString());
                    }
                }
                partes.add(ruta.isEmpty() ? mensaje : String.join(".", ruta) + ": " + mensaje);
            }
            return partes.isEmpty() ? "AletheIA rechazó la noticia" : String.join("; ", partes);
        }
        return detalle.toString();
    }

    private static Integer leerRetryAfter(HttpHeaders cabeceras) {
        String valor = cabeceras.getFirst(HttpHeaders.RETRY_AFTER);
        if (valor == null) {
            return null;
        }
        try {
            return Integer.valueOf(valor.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
