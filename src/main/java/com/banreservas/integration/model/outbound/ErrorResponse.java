package com.banreservas.integration.model.outbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "ClienteBanreservasResponse", namespace = "http://tempuri.org/")
@RegisterForReflection
public record ErrorResponse(
    @JacksonXmlProperty(localName = "ClienteBanreservasResult")
    ErrorResult clienteBanreservasResult
) implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @RegisterForReflection
    public record ErrorResult(
        String canal,
        String usuario, 
        String terminal,
        String fechaHora,
        @JacksonXmlProperty(localName = "TRN_ID") String trnId,
        Short resultado,
        String mensaje,
        String version
    ) implements Serializable {
    }
}