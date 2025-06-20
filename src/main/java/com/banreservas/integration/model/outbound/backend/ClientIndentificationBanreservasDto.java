package com.banreservas.integration.model.outbound.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

/**
 * DTO que contiene información de identificación de un cliente.
 * Incluye validaciones para asegurar que los campos no estén en blanco
 * y que el número de identificación no contenga guiones.
 * 
 * @author Domingo Junior Ruiz - c-djruiz@banreservas.com
 * @since 31-03-2025
 * @version 1.0
 */
@RegisterForReflection
public record ClientIndentificationBanreservasDto(
        String identificationType,
        String identificationNumber) implements Serializable {
        
}