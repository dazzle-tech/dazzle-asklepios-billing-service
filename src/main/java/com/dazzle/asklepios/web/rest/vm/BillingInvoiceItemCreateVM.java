package com.dazzle.asklepios.web.rest.vm;

import com.dazzle.asklepios.domain.enumeration.Currency;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * View Model for creating a BillingInvoiceItem via REST.
 */
public record BillingInvoiceItemCreateVM(
        @NotNull Long invoiceId,
        String nurseServiceProductKey,
        String code,
        @NotNull BigDecimal quantity,
        @NotNull BigDecimal unitPrice,
        @NotNull BigDecimal totalPrice,
        Currency currency
) implements Serializable {
}
