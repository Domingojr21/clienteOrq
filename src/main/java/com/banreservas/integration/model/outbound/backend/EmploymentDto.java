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
   String employeeCode,
   boolean isGroupEmployee,
   String groupEmployee,
   String economicActivityCode,
   String economicActivity,
   String annualIncome,
   String monthlySalary,
   String totalNetIncome,
   String incomeCurrency,
   String subjectToISR,
   String openingCompany,
   String debtLimit,
   String employmentStartDate
 ) implements Serializable {
 }