package com.dazzle.asklepios.web.rest.vm;

import com.dazzle.asklepios.domain.BillingInvoice;
import com.dazzle.asklepios.domain.enumeration.BillingInvoiceStatus;
import com.dazzle.asklepios.domain.enumeration.Currency;

import java.io.Serializable;
import java.math.BigDecimal;

public record BillingInvoiceResponseVM(
        Long id,
        String invoiceNumber,
        String patientKey,
        String encounterKey,
        Long facilityId,
        BillingInvoiceStatus status,
        BigDecimal totalAmount,
        BigDecimal paidAmount,
        BigDecimal balanceAmount,
        Currency currency
) implements Serializable {

    public static BillingInvoiceResponseVM ofEntity(BillingInvoice invoice) {
        return new BillingInvoiceResponseVM(
                invoice.getId(),
                invoice.getInvoiceNumber(),
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
