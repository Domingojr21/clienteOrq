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
   String status,
   String firstName,
   String middleName,
   String lastName,
   String maidenName,
   String fullName,
   String surnames,
   String nickname,
   String personType,
   String maritalStatus,
   String countryOfResidence,
   String gender,
   String birthDate,
   String birthPlace,
   String birthCountry,
   String birthCity,
   String originCountryCode,
   String originCountry,
   boolean isForeigner,
   String residenceType,
   String geographicLevel,
   String segment,
   String issuingAttentionPoint,
   String supervisor,
   String accountExecutive,
   boolean isOnBlacklist,
   String legalRepresentative,
   String costCenter,
   String clientType,
   boolean isTopClient,
   String website,
   boolean isDeceased,
   String deathDate,
   int dependents,
   boolean isLinkedToGroupEmployee,
   boolean isLinkedToBanreservas,
   ControlListsDto controlLists,
   EmploymentDto employment,
   List<AddressDto> addresses,
   List<ContactMethodsDto> contactMethods,
   List<EmailDto> emails,
   List<RelatedSystemsDto> relatedSystems,
   List<ApplicationHistoryDto> applicationHistory
 ) implements Serializable {
 }