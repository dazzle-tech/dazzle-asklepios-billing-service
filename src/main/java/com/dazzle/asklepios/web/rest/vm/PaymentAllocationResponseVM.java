package com.dazzle.asklepios.web.rest.vm;

import com.dazzle.asklepios.domain.PaymentAllocation;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * View Model for reading a PaymentAllocation via REST.
 */
public record PaymentAllocationResponseVM(
        Long id,
        Long paymentId,
        Long invoiceId,
        Long invoiceItemId,
        BigDecimal allocatedAmount
) implements Serializable {

    public static PaymentAllocationResponseVM ofEntity(PaymentAllocation a) {
        return new PaymentAllocationResponseVM(
                a.getId(),
                a.getPayment() != null ? a.getPayment().getId() : null,
                a.getInvoice() != null ? a.getInvoice().getId() : null,
                a.getInvoiceItem() != null ? a.getInvoiceItem().getId() : null,
                a.getAllocatedAmount()
        );
    }
}
