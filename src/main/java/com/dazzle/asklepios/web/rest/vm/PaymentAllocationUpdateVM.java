package com.dazzle.asklepios.web.rest.vm;

import com.dazzle.asklepios.domain.PaymentAllocation;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * View Model for updating a PaymentAllocation via REST.
 */
public record PaymentAllocationUpdateVM(
        @NotNull Long id,
        Long paymentId,
        Long invoiceId,
        Long invoiceItemId,
        BigDecimal allocatedAmount
) implements Serializable {

    public static PaymentAllocationUpdateVM ofEntity(PaymentAllocation a) {
        return new PaymentAllocationUpdateVM(
                a.getId(),
                a.getPayment() != null ? a.getPayment().getId() : null,
                a.getInvoice() != null ? a.getInvoice().getId() : null,
                a.getInvoiceItem() != null ? a.getInvoiceItem().getId() : null,
                a.getAllocatedAmount()
        );
    }
}
