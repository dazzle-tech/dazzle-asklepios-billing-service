package com.dazzle.asklepios.web.rest.vm;

import com.dazzle.asklepios.domain.BillingInvoiceItem;
import com.dazzle.asklepios.domain.enumeration.Currency;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * View Model for updating a BillingInvoiceItem via REST.
 */
public record BillingInvoiceItemUpdateVM(
        @NotNull Long id,
        Long invoiceId,
        String nurseServiceProductKey,
        String code,
        BigDecimal quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice,
        Currency currency
) implements Serializable {

    public static BillingInvoiceItemUpdateVM ofEntity(BillingInvoiceItem item) {
        return new BillingInvoiceItemUpdateVM(
                item.getId(),
                item.getInvoice() != null ? item.getInvoice().getId() : null,
                item.getNurseServiceProductKey(),
                item.getCode(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice(),
                item.getCurrency()
        );
    }
}
