package com.banreservas.integration.model.outbound.backend;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;


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
 public record EmploymentDto(
    @JsonProperty("employeeCode") String employeeCode,
    @JsonProperty("isGroupEmployee") boolean isGroupEmployee,
    @JsonProperty("groupEmployee") String groupEmployee,
    @JsonProperty("economicActivityCode") String economicActivityCode,
    @JsonProperty("economicActivity") String economicActivity,
    @JsonProperty("annualIncome") String annualIncome,
    @JsonProperty("monthlySalary") String monthlySalary,
    @JsonProperty("totalNetIncome") String totalNetIncome,
    @JsonProperty("incomeCurrency") String incomeCurrency,
    @JsonProperty("subjectToISR") String subjectToISR,
    @JsonProperty("openingCompany") String openingCompany,
    @JsonProperty("debtLimit") String debtLimit,
    @JsonProperty("employmentStartDate") String employmentStartDate
 ) implements Serializable {
 }