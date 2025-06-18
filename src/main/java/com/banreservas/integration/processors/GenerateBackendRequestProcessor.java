package com.banreservas.integration.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tempuri.ClienteBanreservasRequest;

import com.banreservas.integration.model.outbound.backend.ClientIndentificationBanreservasDto;
import com.banreservas.integration.model.outbound.backend.RequestClientDto;
import com.banreservas.integration.model.outbound.backend.RequestDto;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import io.quarkus.runtime.annotations.RegisterForReflection;

@ApplicationScoped
@Named("generateBackendRequestProcessor")
@RegisterForReflection
public class GenerateBackendRequestProcessor implements Processor {
    
    private static final Logger log = LoggerFactory.getLogger(GenerateBackendRequestProcessor.class);
    
    @Override
    public void process(Exchange exchange) throws Exception {

        String sessionId = exchange.getProperty("sessionId", String.class);
        
        log.info("Generando request JSON para el servicio de búsqueda de avisos - SessionId: {}", sessionId);
       
        String identificationNumber = exchange.getProperty("NumeroIdentificacionRq", String.class);
        String identificationType = exchange.getProperty("TipoIdentificacionRq", String.class);
        
        if (identificationNumber == null || identificationNumber.isEmpty()) {
            throw new RuntimeException("No se encontró el número de identificación del cliente en el exchange");
        }
        
        ClientIndentificationBanreservasDto identification =
         new ClientIndentificationBanreservasDto(identificationType, identificationNumber);
        
        RequestClientDto client =
        new RequestClientDto(identification);

       RequestDto request = 
       new RequestDto(client);

        exchange.getIn().setBody(request);
        
        log.info("Request generado exitosamente para session: {}", sessionId);
    }
}