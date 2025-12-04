package com.dazzle.asklepios.service;

import com.dazzle.asklepios.domain.BillingInvoice;
import com.dazzle.asklepios.domain.enumeration.BillingInvoiceStatus;
import com.dazzle.asklepios.repository.BillingInvoiceRepository;
import com.dazzle.asklepios.web.rest.errors.BadRequestAlertException;
import com.dazzle.asklepios.web.rest.vm.BillingInvoiceCreateVM;
import com.dazzle.asklepios.web.rest.vm.BillingInvoiceUpdateVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class BillingInvoiceService {

    private static final Logger LOG = LoggerFactory.getLogger(BillingInvoiceService.class);

    private final BillingInvoiceRepository billingInvoiceRepository;

    public BillingInvoiceService(BillingInvoiceRepository billingInvoiceRepository) {
        this.billingInvoiceRepository = billingInvoiceRepository;
    }

    public BillingInvoice create(BillingInvoiceCreateVM vm) {
        LOG.debug("Request to create BillingInvoice : {}", vm);

        BillingInvoice invoice = BillingInvoice.builder()
                .patientKey(vm.patientKey())
                .encounterKey(vm.encounterKey())
                .facilityId(vm.facilityId())
                .status(BillingInvoiceStatus.PENDING)
                .totalAmount(vm.totalAmount())
                .paidAmount(vm.paidAmount() != null ? vm.paidAmount() : vm.totalAmount())
                .balanceAmount(vm.balanceAmount() != null ? vm.balanceAmount() : vm.totalAmount().subtract(vm.paidAmount() != null ? vm.paidAmount() : vm.totalAmount()))
                .currency(vm.currency())
                .build();

        BillingInvoice saved = billingInvoiceRepository.save(invoice);
        LOG.debug("Created BillingInvoice: {}", saved);
        return saved;
    }

    public Optional<BillingInvoice> update(Long id, BillingInvoiceUpdateVM vm) {
        LOG.debug("Request to update BillingInvoice id={} with {}", id, vm);

        BillingInvoice invoice = billingInvoiceRepository.findById(id)
                .orElseThrow(() -> new BadRequestAlertException(
                        "BillingInvoice not found with id " + id,
                        "billingInvoice",
                        "notfound"
                ));

        if (vm.patientKey() != null) invoice.setPatientKey(vm.patientKey());
        if (vm.encounterKey() != null) invoice.setEncounterKey(vm.encounterKey());
        if (vm.facilityId() != null) invoice.setFacilityId(vm.facilityId());
        if (vm.status() != null) invoice.setStatus(vm.status());
        if (vm.totalAmount() != null) invoice.setTotalAmount(vm.totalAmount());
        if (vm.paidAmount() != null) invoice.setPaidAmount(vm.paidAmount());
        if (vm.balanceAmount() != null) invoice.setBalanceAmount(vm.balanceAmount());
        if (vm.currency() != null) invoice.setCurrency(vm.currency());

        BillingInvoice updated = billingInvoiceRepository.save(invoice);
        LOG.debug("Updated BillingInvoice: {}", updated);

        return Optional.of(updated);
    }

    @Transactional(readOnly = true)
    public Page<BillingInvoice> findAll(Pageable pageable) {
        LOG.debug("Request to get BillingInvoices: {}", pageable);
        return billingInvoiceRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<BillingInvoice> findByPatientKey(String patientKey, Pageable pageable) {
        LOG.debug("Request to get BillingInvoices by patientKey={} pageable={}", patientKey, pageable);
        return billingInvoiceRepository.findByPatientKey(patientKey, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<BillingInvoice> findOne(Long id) {
        LOG.debug("Request to get BillingInvoice : {}", id);
        return billingInvoiceRepository.findById(id);
    }
}
