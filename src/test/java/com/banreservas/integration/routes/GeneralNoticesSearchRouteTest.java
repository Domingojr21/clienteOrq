package com.banreservas.integration.routes;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.xml.HasXPath.hasXPath;

import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.banreservas.integration.mocks.SoapWireMock;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
@QuarkusTestResource(SoapWireMock.class)
class BanservasClientRouteTest extends CamelQuarkusTestSupport {

    private static final String AUTH_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUI...";
    private static final String URL_ENDPOINT = "/general/notices/search/api/v1/generalNoticesSearch";

    private static String requestOk;
    private static String requestError;

    @BeforeAll
static void setup() {
    requestOk = """
        <?xml version="1.0" encoding="utf-8"?>
        <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
          <soap:Body>
            <BusquedaGeneralAvisosMICM xmlns="http://tempuri.org/">
              <request>
                <Canal>Web</Canal>
                <Usuario>UsuarioPrueba</Usuario>
                <Terminal>TerminalPrueba</Terminal>
                <FechaHora>2025-06-04T14:30:00</FechaHora>
                <Version>1.0</Version>
                <IdOperacion>37544</IdOperacion>
              </request>
            </BusquedaGeneralAvisosMICM>
          </soap:Body>
        </soap:Envelope>
        """;

    requestError = """
        <?xml version="1.0" encoding="utf-8"?>
        <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
          <soap:Body>
            <BusquedaGeneralAvisosMICM xmlns="http://tempuri.org/">
              <request>
                <Canal></Canal>
                <Usuario></Usuario>
                <Terminal></Terminal>
                <FechaHora></FechaHora>
                <Version></Version>
                <IdOperacion></IdOperacion>
              </request>
            </BusquedaGeneralAvisosMICM>
          </soap:Body>
        </soap:Envelope>
        """;
}

    @Test
    void testResponseBusquedaGeneralAvisosMICMOk() {
        given()
                .contentType(ContentType.XML)
                .header("Authorization", AUTH_TOKEN)
                .header("sessionId", 123456)
                .header("SOAPAction", "http://tempuri.org/BusquedaGeneralAvisosMICM")
                .header("test", "ok")
                .body(requestOk)
                .post(URL_ENDPOINT)
                .then()
                .statusCode(200)
                .body(containsString("<Resultado>0</Resultado>"))
                .body(containsString("<Mensaje>Consulta exitosa</Mensaje>"));
    }

    @Test
    void testResponseBusquedaGeneralAvisosMICMError() {
        given()
                .contentType(ContentType.XML)
                .header("Authorization", AUTH_TOKEN)
                .header("sessionId", 123456)
                .header("SOAPAction", "http://tempuri.org/BusquedaGeneralAvisosMICM")
                .header("test", "ok")
                .body(requestError)
                .post(URL_ENDPOINT)
                .then()
                .statusCode(200)
                .body(hasXPath("//*[local-name()='Resultado']", equalTo("1")))
                .body(hasXPath("//*[local-name()='Mensaje']", containsString("El campo 'Canal' es obligatorio")));
    }

    @Test
    void testResponseBusquedaGeneralAvisosMICMUnauthorized() {
        given()
                .contentType(ContentType.XML)
                .header("sessionId", 123456)
                .header("SOAPAction", "http://tempuri.org/BusquedaGeneralAvisosMICM")
                .header("test", "ok")
                .body(requestOk)
                .post(URL_ENDPOINT)
                .then()
                .statusCode(200)
                .body(hasXPath("//*[local-name()='Resultado']", equalTo("1")))
                .body(hasXPath("//*[local-name()='Mensaje']", containsString("Unauthorized")));
    }

    @Test
    void testResponseBusquedaGeneralAvisosMICMWithoutSessionId() {
        given()
                .contentType(ContentType.XML)
                .header("Authorization", AUTH_TOKEN)
                .header("SOAPAction", "http://tempuri.org/BusquedaGeneralAvisosMICM")
                .header("test", "ok")
                .body(requestOk)
                .post(URL_ENDPOINT)
                .then()
                .statusCode(200)
                .body(hasXPath("//*[local-name()='Resultado']", equalTo("1")))
                .body(hasXPath("//*[local-name()='Mensaje']", containsString("sessionId")));
    }
}