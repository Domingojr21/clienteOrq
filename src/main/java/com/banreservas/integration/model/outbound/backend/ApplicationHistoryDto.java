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
    String applicationNumber,
    String applicationDate,
    String productFamily,
    String productType,
    String productCode,
    String status,
    String approvalDate,
    String reason1,
    String reason2,
    String currency,
    BigDecimal approvedTerm,
    BigDecimal approvedAmount,
    BigDecimal estimatedPayment,
    BigDecimal approvedRate,
    BigDecimal dollarLimit,
    BigDecimal pesoLimit,
    BigDecimal totalFlexAmount
 ) implements Serializable {
 }