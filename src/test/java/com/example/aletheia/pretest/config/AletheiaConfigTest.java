package com.example.aletheia.pretest.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.example.aletheia.pretest.scheduler.AnalisisPendientesTarea;
import com.example.aletheia.pretest.service.IAnalisisService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.web.client.RestClient;

/** Arranque de la configuración de AletheIA, sin BD ni servicio real. */
class AletheiaConfigTest {

    private final ApplicationContextRunner contexto = new ApplicationContextRunner()
            .withUserConfiguration(AletheiaConfig.class);

    @Test
    void sinClaveElArranqueFallaConMensajeClaro() {
        contexto.withPropertyValues("aletheia.url=http://127.0.0.1:8000", "aletheia.api-key=")
                .run(ctx -> {
                    assertThat(ctx).hasFailed();
                    assertThat(ctx.getStartupFailure())
                            .rootCause()
                            .hasMessageContaining("ALETHEIA_API_CLAVE");
                });
    }

    @Test
    void laTareaProgramadaAceptaElIntervaloConfigurado() {
        IAnalisisService servicio = mock(IAnalisisService.class);
        contexto.withPropertyValues("aletheia.url=http://127.0.0.1:8000", "aletheia.api-key=clave",
                        "aletheia.intervalo-consulta=10s")
                .withBean(IAnalisisService.class, () -> servicio)
                .withBean(AnalisisPendientesTarea.class)
                .run(ctx -> assertThat(ctx).hasNotFailed().hasSingleBean(AnalisisPendientesTarea.class));
    }

    @Test
    void conClaveArrancaYNoLaMuestraEnToString() {
        contexto.withPropertyValues("aletheia.url=http://127.0.0.1:8000", "aletheia.api-key=clave-secreta-de-prueba")
                .run(ctx -> {
                    assertThat(ctx).hasNotFailed();
                    assertThat(ctx).hasSingleBean(RestClient.class);
                    AletheiaProperties propiedades = ctx.getBean(AletheiaProperties.class);
                    assertThat(propiedades.timeoutConexion()).hasSeconds(5);
                    assertThat(propiedades.tiempoMaximoPendiente()).hasMinutes(10);
                    assertThat(propiedades.toString()).doesNotContain("clave-secreta-de-prueba");
                });
    }
}
