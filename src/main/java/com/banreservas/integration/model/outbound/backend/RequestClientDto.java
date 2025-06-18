package com.banreservas.integration.model.outbound.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO que contiene la información de solicitud de un cliente.
 * Incluye validaciones para asegurar que la identificación esté presente y válida.
 * 
 * @author Domingo Junior Ruiz - c-djruiz@banreservas.com
 * @since 31-03-2025
 * @version 1.0
 */
@RegisterForReflection
public record RequestClientDto(
        @JsonProperty("identification") 
        @NotNull(message = "La identificación del cliente no puede ser nula")
        @Valid
        ClientIndentificationBanreservasDto identification) implements Serializable {
    
}