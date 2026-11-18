package com.example.aletheia.pretest.config;

import java.net.http.HttpClient;
import java.time.Clock;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClient;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(AletheiaProperties.class)
public class AletheiaConfig {

    public static final String CABECERA_CLAVE = "X-API-Key";

    @Bean
    public RestClient aletheiaRestClient(AletheiaProperties propiedades) {
        return configurar(RestClient.builder(), propiedades).build();
    }

    /** Reloj inyectable, para poder fijar la hora en las pruebas. */
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    /**
     * URL base, clave y timeouts del cliente de AletheIA. Es estático para que las pruebas
     * configuren el cliente exactamente igual antes de simular el servidor.
     */
    public static RestClient.Builder configurar(RestClient.Builder builder, AletheiaProperties propiedades) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(propiedades.timeoutConexion())
                // AletheIA (uvicorn) habla HTTP/1.1; evita el intento de pasar a HTTP/2.
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        JdkClientHttpRequestFactory fabrica = new JdkClientHttpRequestFactory(httpClient);
        fabrica.setReadTimeout(propiedades.timeoutLectura());

        return builder
                .baseUrl(propiedades.url())
                .defaultHeader(CABECERA_CLAVE, propiedades.apiKey())
                .requestFactory(fabrica);
    }
}
