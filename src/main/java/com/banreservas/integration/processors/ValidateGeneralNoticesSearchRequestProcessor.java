package com.banreservas.integration.processors;

import java.time.LocalDateTime;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tempuri.ClienteBanreservas;
import org.tempuri.ClienteBanreservasRequest;

import com.banreservas.integration.exceptions.ValidationException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import io.quarkus.runtime.annotations.RegisterForReflection;

@ApplicationScoped
@Named("validateGeneralNoticesSearchRequestProcessor")
@RegisterForReflection
public class ValidateGeneralNoticesSearchRequestProcessor implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(ValidateGeneralNoticesSearchRequestProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Iniciando validación de la solicitud ClienteBanreservas");

        ClienteBanreservas soapRequest = exchange.getIn().getBody(ClienteBanreservas.class);

        if (soapRequest == null) {
            throwValidation(exchange, "El envelope SOAP es obligatorio");
        }

        ClienteBanreservasRequest request = soapRequest.getRequest();

        if (request == null) {
            throwValidation(exchange, "El request es obligatorio");
        }

        // MAPEAR DATOS DEL REQUEST PRIMERO - ANTES DE LAS VALIDACIONES
        exchange.setProperty("canalRq", request.getCanal() != null ? request.getCanal() : "");
        exchange.setProperty("usuarioRq", request.getUsuario() != null ? request.getUsuario() : "");
        exchange.setProperty("terminalRq", request.getTerminal() != null ? request.getTerminal() : "");
        exchange.setProperty("versionRq", request.getVersion() != null ? request.getVersion() : "");

        if (request.getFechaHora() != null && !request.getFechaHora().isEmpty()) {
            exchange.setProperty("fechaHoraRq", request.getFechaHora());
        } else {
            exchange.setProperty("fechaHoraRq", LocalDateTime.now().toString());
        }

        // AHORA HACER LAS VALIDACIONES
        validate(exchange, request.getCanal(), "Canal");
        validate(exchange, request.getUsuario(), "Usuario");
        validate(exchange, request.getTerminal(), "Terminal");
        validate(exchange, request.getVersion(), "Version");

        if (request.getIdentificacion() == null) {
            throwValidation(exchange, "El campo 'Identificacion' es obligatorio");
        }

        validate(exchange, request.getIdentificacion().getNumeroIdentificacion(),
                "Identificacion.NumeroIdentificacion");
        validate(exchange, request.getIdentificacion().getTipoIdentificacion().value(),
                "Identificacion.TipoIdentificacion");

        // Mapear los datos de identificación
        exchange.setProperty("NumeroIdentificacionRq",
                request.getIdentificacion().getNumeroIdentificacion());
        exchange.setProperty("TipoIdentificacionRq",
                request.getIdentificacion().getTipoIdentificacion());

        getHeaderOrThrow(exchange, "Authorization", "Unauthorized.");
        getHeaderOrThrow(exchange, "sessionId", "La cabecera 'sessionId' es obligatoria.");
    }

    private void validate(Exchange exchange, String value, String fieldName) {
        if (value == null || value.isEmpty()) {
            throwValidation(exchange, "El campo '" + fieldName + "' es obligatorio");
        }
    }

    private String getHeaderOrThrow(Exchange exchange, String headerName, String errorMsg) {
        String value = exchange.getIn().getHeader(headerName, String.class);
        if (value == null || value.isEmpty()) {
            throwValidation(exchange, errorMsg);
        }
        return value;
    }

    private void throwValidation(Exchange exchange, String message) {
        logger.warn("Error de validación: {}", message);
        exchange.setProperty("Mensaje", message);
        throw new ValidationException(message);
    }
}