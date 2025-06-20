package com.banreservas.integration.model.outbound.backend;

import java.io.Serializable;

import io.quarkus.runtime.annotations.RegisterForReflection;

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
public record AddressDto(
    String street,
    String building,
    String apartment,
    String sector,
    String city,
    String province,
    String country,
    String postalCode,
    boolean isPrimary,
    String type,
    String house,
    String localityId,
    String countryCode,
    String format,
    String localType,
    boolean receivesStatements
) implements Serializable {
}