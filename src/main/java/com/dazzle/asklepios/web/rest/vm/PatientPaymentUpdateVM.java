package com.dazzle.asklepios.web.rest.vm;

import com.dazzle.asklepios.domain.PatientPayment;
import com.dazzle.asklepios.domain.enumeration.PaymentMethod;
import com.dazzle.asklepios.domain.enumeration.PaymentType;
import com.dazzle.asklepios.domain.enumeration.Currency;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * View Model for updating a PatientPayment via REST.
 */
public record PatientPaymentUpdateVM(
        @NotNull Long id,
        String patientKey,
        Long facilityId,
        PaymentType paymentType,
        PaymentMethod paymentMethod,
        Instant paymentDate,
        BigDecimal amount,
        Currency currency,
        String reference,
        String notes
) implements Serializable {

    public static PatientPaymentUpdateVM ofEntity(PatientPayment p) {
        return new PatientPaymentUpdateVM(
                p.getId(),
                p.getPatientKey(),
                p.getFacilityId(),
                p.getPaymentType(),
                p.getPaymentMethod(),
                p.getPaymentDate(),
                p.getAmount(),
                p.getCurrency(),
                p.getReference(),
                p.getNotes()
        );
    }
}
