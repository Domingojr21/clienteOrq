package com.banreservas.integration.model.outbound.backend;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.List;
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
 public record ClientDto(
    @JsonProperty("status") String status,
    @JsonProperty("firstName") String firstName,
    @JsonProperty("middleName") String middleName,
    @JsonProperty("lastName") String lastName,
    @JsonProperty("maidenName") String maidenName,
    @JsonProperty("fullName") String fullName,
    @JsonProperty("surnames") String surnames,
    @JsonProperty("nickname") String nickname,
    @JsonProperty("personType") String personType,
    @JsonProperty("maritalStatus") String maritalStatus,
    @JsonProperty("countryOfResidence") String countryOfResidence,
    @JsonProperty("gender") String gender,
    @JsonProperty("birthDate") String birthDate,
    @JsonProperty("birthPlace") String birthPlace,
    @JsonProperty("birthCountry") String birthCountry,
    @JsonProperty("birthCity") String birthCity,
    @JsonProperty("originCountryCode") String originCountryCode,
    @JsonProperty("originCountry") String originCountry,
    @JsonProperty("isForeigner") boolean isForeigner,
    @JsonProperty("residenceType") String residenceType,
    @JsonProperty("geographicLevel") String geographicLevel,
    @JsonProperty("segment") String segment,
    @JsonProperty("issuingAttentionPoint") String issuingAttentionPoint,
    @JsonProperty("supervisor") String supervisor,
    @JsonProperty("accountExecutive") String accountExecutive,
    @JsonProperty("isOnBlacklist") boolean isOnBlacklist,
    @JsonProperty("legalRepresentative") String legalRepresentative,
    @JsonProperty("costCenter") String costCenter,
    @JsonProperty("clientType") String clientType,
    @JsonProperty("isTopClient") boolean isTopClient,
    @JsonProperty("website") String website,
    @JsonProperty("isDeceased") boolean isDeceased,
    @JsonProperty("deathDate") String deathDate,
    @JsonProperty("dependents") int dependents,
    @JsonProperty("isLinkedToGroupEmployee") boolean isLinkedToGroupEmployee,
    @JsonProperty("isLinkedToBanreservas") boolean isLinkedToBanreservas,
    @JsonProperty("controlLists") ControlListsDto controlLists,
    @JsonProperty("employment") EmploymentDto employment,
    @JsonProperty("addresses") List<AddressDto> addresses,
    @JsonProperty("contactMethods") List<ContactMethodsDto> contactMethods,
    @JsonProperty("emails") List<EmailDto> emails,
    @JsonProperty("relatedSystems") List<RelatedSystemsDto> relatedSystems,
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("applicationHistory") List<ApplicationHistoryDto> applicationHistory
 ) implements Serializable {
 }