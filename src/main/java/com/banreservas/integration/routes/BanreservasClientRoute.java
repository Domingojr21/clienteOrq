package com.banreservas.integration.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.banreservas.integration.exceptions.ValidationException;
import com.banreservas.integration.model.outbound.BackendResponse;
import com.banreservas.integration.processors.ErrorResponseProcessor;
import com.banreservas.integration.processors.GenerateBackendRequestProcessor;
import com.banreservas.integration.processors.MapBackendResponseProcessor;
import com.banreservas.integration.processors.ValidateGeneralNoticesSearchRequestProcessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BanreservasClientRoute extends RouteBuilder {

    private static final Logger logger = LoggerFactory.getLogger(BanreservasClientRoute.class);

    @Inject
    ValidateGeneralNoticesSearchRequestProcessor validateRequestProcessor;

    @Inject
    GenerateBackendRequestProcessor generateBackendRequestProcessor;

    @Inject
    MapBackendResponseProcessor mapBackendResponseProcessor;

    @Inject
    ErrorResponseProcessor errorResponseProcessor;

    @Override
    public void configure() throws Exception {

        onException(ValidationException.class)
                .handled(true)
                .log(LoggingLevel.INFO, logger, "ValidationException capturada: ${exception.message}")
                .setHeader("Content-Type", constant("text/xml"))
                .process(errorResponseProcessor)
                .marshal().jacksonXml()
                .end();

        onException(Exception.class)
                .handled(true)
                .log(LoggingLevel.ERROR, logger, "Error inesperado: ${exception.message}")
                .setProperty("Mensaje", simple("${exception.message}"))
                .process(errorResponseProcessor)
                .marshal().jacksonXml()
                .setHeader("Content-Type", constant("text/xml"))
                .end();

        from("cxf:bean:banreservasClientEndpoint")
                .routeId("banreservas-client-route")
                .log(LoggingLevel.INFO, logger, "Solicitud SOAP recibida para ClienteBanreservas")

                .process(validateRequestProcessor)

                .process(generateBackendRequestProcessor)

                .marshal().json()
                .log(LoggingLevel.DEBUG, logger, "Request JSON generado: ${body}")

                .to("direct:backend-banreservas-client-service")

                .choice()
.when(header("CamelHttpResponseCode").isEqualTo(200))
    .log(LoggingLevel.INFO, logger, "Respuesta exitosa del backend")
    .process(exchange -> {
        String jsonResponse = exchange.getIn().getBody(String.class);
        logger.debug("JSON Response del backend: {}", jsonResponse);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            BackendResponse response = mapper.readValue(jsonResponse, BackendResponse.class);
            exchange.getIn().setBody(response);
        } catch (Exception e) {
            logger.error("Error deserializando JSON: {}", e.getMessage());
            exchange.setProperty("Mensaje", "Error procesando respuesta del backend: " + e.getMessage());
            throw new RuntimeException("Error procesando respuesta del backend: " + e.getMessage());
        }
    })
    .process(mapBackendResponseProcessor)
.otherwise()
    .log(LoggingLevel.ERROR, logger, "Error en servicio backend. Código HTTP: ${header.CamelHttpResponseCode}")
    .choice()
        .when(header("CamelHttpResponseCode").isEqualTo(400))
            .setProperty("Mensaje", constant("Request inválido para búsqueda de cliente"))
        .when(header("CamelHttpResponseCode").isEqualTo(401))
            .setProperty("Mensaje", constant("Token inválido para búsqueda de cliente"))
        .when(header("CamelHttpResponseCode").isEqualTo(404))
            .setProperty("Mensaje", constant("Servicio de cliente Banreservas no disponible"))
        .when(header("CamelHttpResponseCode").isEqualTo(500))
            .setProperty("Mensaje", constant("Error interno en servicio de cliente"))
        .when(header("CamelHttpResponseCode").isEqualTo(503))
            .setProperty("Mensaje", constant("Servicio de cliente temporalmente no disponible"))
        .otherwise()
            .setProperty("Mensaje", constant("Error inesperado en servicio de cliente"))
    .end()
    .process(errorResponseProcessor)
    .marshal().jacksonXml()  
    .setHeader("Content-Type", constant("text/xml"))
.end()

                .setHeader("sessionId", exchangeProperty("originalSessionId"))
                .setHeader("Content-Type", constant("text/xml"))

                .log(LoggingLevel.INFO, logger, "ClienteBanreservas procesado exitosamente")
                .end();
    }
}