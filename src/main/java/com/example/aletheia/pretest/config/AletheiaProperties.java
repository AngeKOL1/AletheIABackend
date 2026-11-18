package com.example.aletheia.pretest.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Configuración del servicio externo AletheIA (prefijo {@code aletheia}).
 * Si falta la clave, el arranque falla con el mensaje de {@code apiKey}.
 */
@Validated
@ConfigurationProperties(prefix = "aletheia")
public record AletheiaProperties(
        @NotBlank(message = "Falta aletheia.url (URL base de AletheIA)")
        String url,

        @NotBlank(message = "Falta la clave de AletheIA: define la variable de entorno ALETHEIA_API_CLAVE")
        String apiKey,

        @NotNull @DefaultValue("5s") Duration timeoutConexion,
        @NotNull @DefaultValue("30s") Duration timeoutLectura,
        // Cada cuánto la tarea programada consulta los análisis pendientes.
        @NotNull @DefaultValue("10s") Duration intervaloConsulta,
        // Pasado este tiempo, un análisis pendiente se marca como ERROR.
        @NotNull @DefaultValue("10m") Duration tiempoMaximoPendiente,
        // Tras un ERROR o PERDIDO, tiempo mínimo antes de volver a enviar la misma noticia (cada envío cuesta).
        @NotNull @DefaultValue("1m") Duration esperaReintento) {

    /** Nunca muestra la clave (por si el objeto acaba en un log). */
    @Override
    public String toString() {
        return "AletheiaProperties[url=" + url + ", apiKey=****, timeoutConexion=" + timeoutConexion
                + ", timeoutLectura=" + timeoutLectura + ", intervaloConsulta=" + intervaloConsulta
                + ", tiempoMaximoPendiente=" + tiempoMaximoPendiente + ", esperaReintento=" + esperaReintento + "]";
    }
}
