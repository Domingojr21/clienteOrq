package com.banreservas.integration.processors;

import java.time.LocalDateTime;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.banreservas.integration.model.outbound.ErrorResponse;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Processor para generar respuestas de error en formato SOAP según el WSDL.
 * Se utiliza cuando ocurren excepciones durante el procesamiento.
 *
 * @author Consultor Domingo Ruiz
 * @since 04/06/2025
 * @version 1.0.0
 */

@ApplicationScoped
public class ErrorResponseProcessor implements Processor {

    /**
     * Genera una respuesta SOAP de error basada en la excepción ocurrida,
     * siguiendo la estructura del WSDL BusquedaGeneralAvisosMICMResponse.
     * 
     * @param exchange el intercambio de Camel que contiene la información del error
     * @throws Exception si ocurre un error durante el procesamiento
     */
    @Override
    public void process(Exchange exchange) throws Exception {

        String canal = getExchangeProperty(exchange, "canalRq");
        String usuario = getExchangeProperty(exchange, "usuarioRq");
        String terminal = getExchangeProperty(exchange, "terminalRq");

        String fechaHora;
        if (exchange.getProperty("fechaHoraRq") != null) {
            fechaHora = String.valueOf(exchange.getProperty("fechaHoraRq"));
        } else {
            fechaHora = LocalDateTime.now().toString();
        }

        String version = getExchangeProperty(exchange, "versionRq");
        String sessionId = exchange.getIn().getHeader("sessionId", String.class);
        String trnId = sessionId != null ? sessionId : "unknown";

        String mensaje = exchange.getProperty("Mensaje") != null ? String.valueOf(exchange.getProperty("Mensaje"))
                : "Error interno del servidor";

        ErrorResponse.ErrorResult result = new ErrorResponse.ErrorResult(
                canal, usuario, terminal, fechaHora, trnId, (short) 1, mensaje, version);

        ErrorResponse response = new ErrorResponse(result);

        exchange.getIn().setBody(response);

        if (sessionId != null) {
            exchange.getIn().setHeader("sessionId", sessionId);
        }
    }

    private String getExchangeProperty(Exchange exchange, String propertyName) {
        Object property = exchange.getProperty(propertyName);
        return property != null ? String.valueOf(property) : null;
    }
}