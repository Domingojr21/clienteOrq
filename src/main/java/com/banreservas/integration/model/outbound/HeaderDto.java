package com.banreservas.integration.model.outbound;

import java.io.Serializable;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * DTO para el header de respuestas de servicios MICM.
 * 
 * @author Consultor Domingo Ruiz
 * @since 05/06/2025
 * @version 1.0.0
 */
@RegisterForReflection
public record HeaderDto(
                int responseCode,
                String responseMessage) implements Serializable {
}