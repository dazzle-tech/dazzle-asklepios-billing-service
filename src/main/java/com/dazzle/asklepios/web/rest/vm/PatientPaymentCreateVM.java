package com.dazzle.asklepios.web.rest.vm;

import com.dazzle.asklepios.domain.enumeration.Currency;
import com.dazzle.asklepios.domain.enumeration.PaymentMethod;
import com.dazzle.asklepios.domain.enumeration.PaymentType;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * View Model for creating a PatientPayment via REST.
 */
public record PatientPaymentCreateVM(
        String patientKey,
        @NotNull Long facilityId,
        @NotNull PaymentType paymentType,
        @NotNull PaymentMethod paymentMethod,
        @NotNull Instant paymentDate,
        @NotNull BigDecimal amount,
        Currency currency,
        String reference,
        String notes
) implements Serializable {
}
