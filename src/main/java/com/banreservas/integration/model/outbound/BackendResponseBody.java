package com.banreservas.integration.model.outbound;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import com.banreservas.integration.model.outbound.backend.ClientDto;

import java.io.Serializable;

/**
* La clase está registrada para reflexión a través de la anotación
 * {@link RegisterForReflection}
 * para permitir la serialización y deserialización con Quarkus.
 * 
 * @author Domingo Junior Ruiz - c-djruiz@banreservas.com
 * @since 31-03-2025
 * @version 1.0
 */

@RegisterForReflection
public record BackendResponseBody(
    @JsonProperty("client") ClientDto client
) implements Serializable {
}
