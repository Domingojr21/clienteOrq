package com.banreservas.integration.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RestProducerBanreservasClient extends RouteBuilder {
    
    private static final Logger logger = LoggerFactory.getLogger(RestProducerBanreservasClient.class);
    
    @Override
    public void configure() throws Exception {
        
        from("direct:backend-banreservas-client-service")
            .routeId("backend-banreservas-client-service-route")
            .log(LoggingLevel.INFO, logger, "Iniciando consulta al servicio de cliente Banreservas")
            
            .removeHeaders("CamelHttp*")
            .removeHeader("host")
            
            .setHeader(Exchange.HTTP_METHOD, constant("POST"))
            .setHeader("Content-Type", constant("application/json"))
            .setHeader("Accept", constant("application/json"))
            
            .setHeader("id_consumidor", constant("CLIENTE_BANRESERVAS"))  
            .setHeader("usuario", simple("${exchangeProperty.usuarioRq}"))
            .setHeader("terminal", simple("${exchangeProperty.terminalRq}")) 
            .setHeader("fecha_hora", simple("${date:now:yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX}")) 
            .setHeader("operacion", constant("ClienteBanreservas"))
            
            .choice()
                .when(simple("${header.sessionId} != null"))
                    .setHeader("sessionId", simple("${header.sessionId}"))
                .otherwise()
                    .setHeader("sessionId", constant("default-session")) 
            .end()
            
            .setHeader("Authorization", simple("${header.Authorization}"))
            
            .log(LoggingLevel.INFO, logger, "Usando Authorization header original: ${header.Authorization}")
            
            .toD("{{backend.banreservas.client.url}}?bridgeEndpoint=true&throwExceptionOnFailure=false&connectTimeout={{backend.service.timeout}}&socketTimeout={{backend.service.timeout}}")
            
            .choice()
                .when(header("CamelHttpResponseCode").isEqualTo(200))
                    .log(LoggingLevel.INFO, logger, "Consulta de cliente Banreservas exitosa")
                .when(header("CamelHttpResponseCode").isEqualTo(400))
                    .log(LoggingLevel.ERROR, logger, "Request inválido para servicio de cliente Banreservas")
                    .throwException(new RuntimeException("Request inválido para búsqueda de cliente"))
                .when(header("CamelHttpResponseCode").isEqualTo(401))
                    .log(LoggingLevel.ERROR, logger, "Token inválido para servicio de cliente Banreservas")
                    .throwException(new RuntimeException("Token inválido para búsqueda de cliente"))
                .when(header("CamelHttpResponseCode").isEqualTo(404))
                    .log(LoggingLevel.ERROR, logger, "Servicio de cliente Banreservas no encontrado")
                    .throwException(new RuntimeException("Servicio de cliente Banreservas no disponible"))
                .when(header("CamelHttpResponseCode").isEqualTo(500))
                    .log(LoggingLevel.ERROR, logger, "Error interno en servicio de cliente Banreservas")
                    .throwException(new RuntimeException("Error interno en servicio de cliente"))
                .when(header("CamelHttpResponseCode").isEqualTo(503))
                    .log(LoggingLevel.ERROR, logger, "Servicio de cliente Banreservas temporalmente no disponible")
                    .throwException(new RuntimeException("Servicio de cliente temporalmente no disponible"))
                .otherwise()
                    .log(LoggingLevel.ERROR, logger, "Error inesperado en servicio de cliente. Código: ${header.CamelHttpResponseCode}")
                    .throwException(new RuntimeException("Error inesperado en servicio de cliente"))
            .end();
    }
}