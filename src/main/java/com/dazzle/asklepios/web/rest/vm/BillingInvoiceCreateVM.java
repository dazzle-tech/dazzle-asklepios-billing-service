package com.dazzle.asklepios.web.rest.vm;

import com.dazzle.asklepios.domain.enumeration.BillingInvoiceStatus;
import com.dazzle.asklepios.domain.enumeration.Currency;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * View Model for creating a BillingInvoice via REST.
 */
public record BillingInvoiceCreateVM(
        String patientKey,
        String encounterKey,
        @NotNull Long facilityId,
        BillingInvoiceStatus status,
        @NotNull BigDecimal totalAmount,
        BigDecimal paidAmount,
        BigDecimal balanceAmount,
        Currency currency
) implements Serializable {
}
