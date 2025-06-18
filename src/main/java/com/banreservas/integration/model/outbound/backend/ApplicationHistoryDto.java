package com.banreservas.integration.model.outbound.backend;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

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
 public record ApplicationHistoryDto(
      @JsonProperty("applicationNumber") String applicationNumber,
    @JsonProperty("applicationDate") String applicationDate,
    @JsonProperty("productFamily") String productFamily,
    @JsonProperty("productType") String productType,
    @JsonProperty("productCode") String productCode,
    @JsonProperty("status") String status,
    @JsonProperty("approvalDate") String approvalDate,
    @JsonProperty("reason1") String reason1,
    @JsonProperty("reason2") String reason2,
    @JsonProperty("currency") String currency,
    @JsonProperty("approvedTerm") BigDecimal approvedTerm,
    @JsonProperty("approvedAmount") BigDecimal approvedAmount,
    @JsonProperty("estimatedPayment") BigDecimal estimatedPayment,
    @JsonProperty("approvedRate") BigDecimal approvedRate,
    @JsonProperty("dollarLimit") BigDecimal dollarLimit,
    @JsonProperty("pesoLimit") BigDecimal pesoLimit,
    @JsonProperty("totalFlexAmount") BigDecimal totalFlexAmount
 ) implements Serializable {
 }