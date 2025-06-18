package com.banreservas.integration.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.tempuri.ClienteBanreservasResponse;
import org.tempuri.ClienteBanreservasResponse2;

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

        ClienteBanreservasResponse response = new ClienteBanreservasResponse();
        ClienteBanreservasResponse2 result = new ClienteBanreservasResponse2();

        result.setCanal(String.valueOf(exchange.getProperty("canalRq")));
        result.setUsuario(String.valueOf(exchange.getProperty("usuarioRq")));
        result.setTerminal(String.valueOf(exchange.getProperty("terminalRq")));
        result.setFechaHora(String.valueOf(exchange.getProperty("fechaHoraRq")));
        result.setTRNID(String.valueOf(exchange.getIn().getHeader("sessionId")));
        result.setResultado(Short.parseShort("1"));
        result.setVersion(String.valueOf(exchange.getProperty("versionRq")));

        // Resultado siempre 1 para indicar error
        result.setResultado((short) 1);
        
        String mensaje = exchange.getProperty("Mensaje") != null ? 
            String.valueOf(exchange.getProperty("Mensaje")) : "Error interno del servidor";
        result.setMensaje(mensaje);
        
        exchange.getIn().setBody(response);

        String sessionId = exchange.getIn().getHeader("sessionId", String.class);
        if (sessionId != null) {
            exchange.getIn().setHeader("sessionId", sessionId);
        }
    }
}