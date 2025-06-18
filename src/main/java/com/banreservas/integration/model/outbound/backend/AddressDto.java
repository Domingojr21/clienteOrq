package com.banreservas.integration.model.outbound.backend;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

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
   @JsonProperty("street") String street,
    @JsonProperty("building") String building,
    @JsonProperty("apartment") String apartment,
    @JsonProperty("sector") String sector,
    @JsonProperty("city") String city,
    @JsonProperty("province") String province,
    @JsonProperty("country") String country,
    @JsonProperty("postalCode") String postalCode,
    @JsonProperty("isPrimary") boolean isPrimary,
    @JsonProperty("type") String type,
    @JsonProperty("house") String house,
    @JsonProperty("localityId") String localityId,
    @JsonProperty("countryCode") String countryCode,
    @JsonProperty("format") String format,
    @JsonProperty("localType") String localType,
    @JsonProperty("receivesStatements") boolean receivesStatements
) implements Serializable {
}