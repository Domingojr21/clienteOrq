package com.banreservas.integration.model.outbound.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO (Data Transfer Object) que representa una solicitud de búsqueda de cliente.
 * Se utiliza para transportar la información de identificación del cliente dentro del sistema.
 * Incluye validaciones para asegurar que el cliente esté presente y válido.
 * 
 * <p>
 * Esta clase está marcada con {@link RegisterForReflection} para permitir su
 * uso en entornos nativos de Quarkus.
 * </p>
 * 
 * @author Domingo Junior Ruiz - c-djruiz@banreservas.com
 * @since 31-03-2025
 * @version 1.0
 */
@RegisterForReflection
public record RequestDto(
        RequestClientDto client) implements Serializable {
}