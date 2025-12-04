package com.dazzle.asklepios.web.rest.vm;

import java.io.Serializable;
import java.math.BigDecimal;

public record PatientAccountSummaryVM(
        String patientKey,
        BigDecimal freeBalance,
        BigDecimal outstandingBalance
) implements Serializable { }
