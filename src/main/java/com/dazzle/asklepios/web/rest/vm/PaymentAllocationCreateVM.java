package com.dazzle.asklepios.web.rest.vm;

import java.io.Serializable;
import java.math.BigDecimal;

public record PaymentAllocationCreateVM(
        Long invoiceId,
        Long invoiceItemId,
        BigDecimal allocatedAmount
) implements Serializable { }
