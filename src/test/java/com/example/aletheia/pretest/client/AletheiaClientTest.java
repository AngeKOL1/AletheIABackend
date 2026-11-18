package com.example.aletheia.pretest.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withException;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import com.example.aletheia.pretest.config.AletheiaConfig;
import com.example.aletheia.pretest.config.AletheiaProperties;
import com.example.aletheia.pretest.dto.aletheia.Afirmacion;
import com.example.aletheia.pretest.dto.aletheia.AnalisisResponse;
import com.example.aletheia.pretest.dto.aletheia.Informe;
import com.example.aletheia.pretest.dto.aletheia.NoticiaRequest;
import com.example.aletheia.pretest.exception.AletheiaAnalisisNoEncontradoException;
import com.example.aletheia.pretest.exception.AletheiaConfiguracionException;
import com.example.aletheia.pretest.exception.AletheiaException;
import com.example.aletheia.pretest.exception.AletheiaNoDisponibleException;
import com.example.aletheia.pretest.exception.AletheiaNoticiaInvalidaException;
import com.example.aletheia.pretest.exception.AletheiaOcupadoException;

import tools.jackson.databind.JsonNode;

/** Simula AletheIA con MockRestServiceServer: ninguna prueba llama al servicio real. */
public class AletheiaClientTest {

    private static final String URL = "http://aletheia.test";
    private static final String CLAVE = "clave-de-prueba";
    private static final String ID = "6c10e5e777a54d90886d8cc7d943b30e";

    private MockRestServiceServer servidor;
    private AletheiaClient cliente;

    @BeforeEach
    void preparar() {
        AletheiaProperties propiedades = new AletheiaProperties(URL, CLAVE, Duration.ofSeconds(5),
                Duration.ofSeconds(30), Duration.ofSeconds(10), Duration.ofMinutes(10), Duration.ofMinutes(1));
        RestClient.Builder builder = AletheiaConfig.configurar(RestClient.builder(), propiedades);
        servidor = MockRestServiceServer.bindTo(builder).build();
        cliente = new AletheiaClient(builder.build());
    }

    // ---------- crear() ----------

    @Test
    void crearEnviaCabeceraYExactamenteLosOchoCamposEnSnakeCase() {
        servidor.expect(requestTo(URL + "/v1/analisis"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("X-API-Key", CLAVE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(peticion -> {
                    JsonNode cuerpo = AletheiaJson.leerArbol(
                            ((MockClientHttpRequest) peticion).getBodyAsString().getBytes(StandardCharsets.UTF_8));
                    List<String> campos = new ArrayList<>(cuerpo.propertyNames());
                    assertThat(campos).containsExactlyInAnyOrder(
                            "texto", "titular", "subtitulo", "fecha_publicacion", "autor", "medio", "url", "idioma");
                    assertThat(cuerpo.get("fecha_publicacion").asString()).isEqualTo("2026-11-10");
                    assertThat(cuerpo.get("texto").asString()).isEqualTo("Cuerpo de la noticia");
                    assertThat(cuerpo.get("autor").isNull()).isTrue();
                })
                .andRespond(withStatus(HttpStatus.ACCEPTED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.LOCATION, "/v1/analisis/" + ID)
                        .body(respuestaEnCola()));

        cliente.crear(noticia());

        servidor.verify();
    }

    @Test
    void crearCon202DevuelveIdYEstadoEnCola() {
        servidor.expect(requestTo(URL + "/v1/analisis"))
                .andRespond(withStatus(HttpStatus.ACCEPTED).contentType(MediaType.APPLICATION_JSON)
                        .body(respuestaEnCola()));

        AnalisisResponse respuesta = cliente.crear(noticia());

        assertThat(respuesta.id()).isEqualTo(ID);
        assertThat(respuesta.estado()).isEqualTo("en_cola");
        assertThat(respuesta.creado()).isEqualTo(OffsetDateTime.of(2026, 11, 16, 23, 57, 27, 92203000, ZoneOffset.UTC));
        assertThat(respuesta.informe()).isNull();
        assertThat(respuesta.error()).isNull();
    }

    @Test
    void crearConRespuestaQueNoEs202EsUnError() {
        servidor.expect(requestTo(URL + "/v1/analisis"))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(respuestaEnCola()));

        assertThatThrownBy(() -> cliente.crear(noticia())).isInstanceOf(AletheiaNoDisponibleException.class);
    }

    // ---------- consultar() ----------

    @Test
    void consultarDeserializaUnInformeCompleto() throws IOException {
        servidor.expect(requestTo(URL + "/v1/analisis/" + ID))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-API-Key", CLAVE))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                        .body(fixture("aletheia/analisis-completado.json")));

        AnalisisResponse respuesta = cliente.consultar(ID);

        assertThat(respuesta.estado()).isEqualTo("completado");
        Informe informe = respuesta.informe();
        assertThat(informe.veredictoGlobal()).isEqualTo("falsa");
        assertThat(informe.reglaAgregacionAplicada()).isEqualTo("H-AG-3:R1");
        assertThat(informe.distorsionGlobal().detectada()).isTrue();
        assertThat(informe.limitaciones()).containsExactly("Solo se consultaron fuentes en inglés y español.");
        assertThat(informe.metadatos().fechaAnalisis().getOffset()).isEqualTo(ZoneOffset.UTC);
        assertThat(informe.metadatos().etapas().get(0).versionPrompt()).isEqualTo("v5");

        Afirmacion a1 = informe.afirmaciones().get(0);
        assertThat(a1.veredicto()).isEqualTo("falsa");
        assertThat(a1.solidezEvidencia()).isEqualTo("alta");
        assertThat(a1.evidencias().get(0).fechaPublicacion()).isEqualTo(LocalDate.of(2024, 3, 1));
        assertThat(a1.evidencias().get(0).fechaConsulta()).isEqualTo(LocalDate.of(2026, 11, 16));
        assertThat(a1.evidencias().get(0).fragmentoRelevante()).contains("Phobos");
        assertThat(respuesta.error()).isNull();
    }

    @Test
    void consultarDeserializaVeredictoNullConMotivo() throws IOException {
        String json = fixture("aletheia/analisis-completado.json")
                .replace("\"veredicto_global\": \"falsa\"", "\"veredicto_global\": null")
                .replace("\"motivo_sin_veredicto\": null", "\"motivo_sin_veredicto\": \"analisis_incompleto\"");
        servidor.expect(requestTo(URL + "/v1/analisis/" + ID))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(json));

        Informe informe = cliente.consultar(ID).informe();

        assertThat(informe.veredictoGlobal()).isNull();
        assertThat(informe.motivoSinVeredicto()).isEqualTo("analisis_incompleto");
    }

    @Test
    void unCampoDesconocidoNoRompeLaDeserializacion() throws IOException {
        String json = fixture("aletheia/analisis-completado.json")
                .replace("\"estado\": \"completado\",", "\"estado\": \"completado\", \"campo_futuro\": {\"x\": 1},")
                .replace("\"idioma\": \"es\",", "\"idioma\": \"es\", \"otro_campo_nuevo\": [1, 2],");
        servidor.expect(requestTo(URL + "/v1/analisis/" + ID))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(json));

        assertThat(cliente.consultar(ID).informe().veredictoGlobal()).isEqualTo("falsa");
    }

    @Test
    void consultarConIdMalFormadoNoLlamaAAletheia() {
        assertThatThrownBy(() -> cliente.consultar("../salud")).isInstanceOf(IllegalArgumentException.class);
        servidor.verify();
    }

    // ---------- Errores HTTP: ninguno produce un resultado ----------

    @Test
    void error401EsErrorDeConfiguracion() {
        servidor.expect(requestTo(URL + "/v1/analisis/" + ID))
                .andRespond(withStatus(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON)
                        .body("{\"detail\": \"clave de API ausente o incorrecta\"}"));

        assertThatThrownBy(() -> cliente.consultar(ID))
                .isInstanceOf(AletheiaConfiguracionException.class)
                .hasMessageNotContaining(CLAVE);
    }

    @Test
    void error404EsAnalisisPerdido() {
        servidor.expect(requestTo(URL + "/v1/analisis/" + ID))
                .andRespond(withStatus(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
                        .body("{\"detail\": \"análisis no encontrado\"}"));

        assertThatThrownBy(() -> cliente.consultar(ID)).isInstanceOf(AletheiaAnalisisNoEncontradoException.class);
    }

    @Test
    void error422ConDetalleTexto() {
        servidor.expect(requestTo(URL + "/v1/analisis"))
                .andRespond(withStatus(HttpStatus.UNPROCESSABLE_CONTENT).contentType(MediaType.APPLICATION_JSON)
                        .body("{\"detail\": \"el texto tiene 90000 caracteres; el máximo configurado es 20000\"}"));

        assertThatThrownBy(() -> cliente.crear(noticia()))
                .isInstanceOf(AletheiaNoticiaInvalidaException.class)
                .hasMessage("el texto tiene 90000 caracteres; el máximo configurado es 20000");
    }

    @Test
    void error422ConDetalleLista() {
        servidor.expect(requestTo(URL + "/v1/analisis"))
                .andRespond(withStatus(HttpStatus.UNPROCESSABLE_CONTENT).contentType(MediaType.APPLICATION_JSON)
                        .body("{\"detail\": [{\"loc\": [\"body\", \"texto\"], \"msg\": \"String should have at least 1 character\", \"type\": \"string_too_short\"},"
                                + "{\"loc\": [\"body\", \"extra\"], \"msg\": \"Extra inputs are not permitted\", \"type\": \"extra_forbidden\"}]}"));

        assertThatThrownBy(() -> cliente.crear(noticia()))
                .isInstanceOf(AletheiaNoticiaInvalidaException.class)
                .hasMessage("texto: String should have at least 1 character; extra: Extra inputs are not permitted");
    }

    @Test
    void error503EsServicioOcupadoConRetryAfter() {
        servidor.expect(requestTo(URL + "/v1/analisis"))
                .andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE).contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.RETRY_AFTER, "60")
                        .body("{\"detail\": \"demasiados análisis pendientes; reintente más tarde\"}"));

        assertThatThrownBy(() -> cliente.crear(noticia()))
                .isInstanceOfSatisfying(AletheiaOcupadoException.class,
                        e -> assertThat(e.getReintentarEnSegundos()).isEqualTo(60));
    }

    @Test
    void errorDeRedEsAletheiaNoDisponible() {
        servidor.expect(requestTo(URL + "/v1/analisis/" + ID))
                .andRespond(withException(new IOException("conexión rechazada")));

        assertThatThrownBy(() -> cliente.consultar(ID)).isInstanceOf(AletheiaNoDisponibleException.class);
    }

    @Test
    void error500EsAletheiaNoDisponible() {
        servidor.expect(requestTo(URL + "/v1/analisis/" + ID))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThatThrownBy(() -> cliente.consultar(ID)).isInstanceOf(AletheiaException.class);
    }

    // ---------- salud() ----------

    @Test
    void saludDevuelveTrueSiEstaOk() {
        servidor.expect(requestTo(URL + "/salud"))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                        .body("{\"estado\": \"ok\", \"version_esquema\": \"0.2.0\"}"));

        assertThat(cliente.salud()).isTrue();
    }

    @Test
    void saludDevuelveFalseSiNoResponde() {
        servidor.expect(requestTo(URL + "/salud")).andRespond(withException(new IOException("sin conexión")));

        assertThat(cliente.salud()).isFalse();
    }

    // ---------- Ayudantes ----------

    private static NoticiaRequest noticia() {
        return new NoticiaRequest("Cuerpo de la noticia", "Titular", null, LocalDate.of(2026, 11, 10),
                null, "Medio", null, "es");
    }

    private static String respuestaEnCola() {
        return "{\"id\": \"" + ID + "\", \"estado\": \"en_cola\", \"creado\": \"2026-11-16T23:57:27.092203Z\","
                + " \"actualizado\": \"2026-11-16T23:57:27.092203Z\", \"informe\": null, \"error\": null}";
    }

    /** Respuesta "completado" con el informe completo del ejemplo del contrato. */
    public static String fixtureCompleto() throws IOException {
        return fixture("aletheia/analisis-completado.json");
    }

    static String fixture(String ruta) throws IOException {
        try (InputStream entrada = AletheiaClientTest.class.getClassLoader().getResourceAsStream(ruta)) {
            return new String(entrada.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
