package com.banreservas.integration.model.outbound;

import java.io.Serializable;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * Response DTO para el servicio de búsqueda general de avisos MICM.
 * 
 * @author Consultor Domingo Ruiz
 * @since 05/06/2025
 * @version 1.0.0
 */
@RegisterForReflection
public record BackendResponse(
        HeaderDto header,
        BackendResponseBody body) implements Serializable {
    }
