package com.dazzle.asklepios.web.rest.vm;

import com.dazzle.asklepios.domain.BillingInvoice;
import com.dazzle.asklepios.domain.enumeration.BillingInvoiceStatus;
import com.dazzle.asklepios.domain.enumeration.Currency;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * View Model for updating a BillingInvoice via REST.
 */
public record BillingInvoiceUpdateVM(
        @NotNull Long id,
        String patientKey,
        String encounterKey,
        Long facilityId,
        BillingInvoiceStatus status,
        BigDecimal totalAmount,
        BigDecimal paidAmount,
        BigDecimal balanceAmount,
        Currency currency
) implements Serializable {

    public static BillingInvoiceUpdateVM ofEntity(BillingInvoice invoice) {
        return new BillingInvoiceUpdateVM(
                invoice.getId(),
                invoice.getPatientKey(),
                invoice.getEncounterKey(),
                invoice.getFacilityId(),
                invoice.getStatus(),
                invoice.getTotalAmount(),
                invoice.getPaidAmount(),
                invoice.getBalanceAmount(),
                invoice.getCurrency()
        );
    }
}
