package com.dazzle.asklepios.service;

import com.dazzle.asklepios.domain.BillingInvoice;
import com.dazzle.asklepios.domain.BillingInvoiceItem;
import com.dazzle.asklepios.domain.PatientPayment;
import com.dazzle.asklepios.domain.PaymentAllocation;
import com.dazzle.asklepios.repository.BillingInvoiceItemRepository;
import com.dazzle.asklepios.repository.BillingInvoiceRepository;
import com.dazzle.asklepios.repository.PatientPaymentRepository;
import com.dazzle.asklepios.repository.PaymentAllocationRepository;
import com.dazzle.asklepios.web.rest.errors.BadRequestAlertException;
import com.dazzle.asklepios.web.rest.vm.PaymentAllocationCreateVM;
import com.dazzle.asklepios.web.rest.vm.PaymentAllocationUpdateVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PaymentAllocationService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentAllocationService.class);

    private final PaymentAllocationRepository paymentAllocationRepository;
    private final PatientPaymentRepository patientPaymentRepository;
    private final BillingInvoiceRepository billingInvoiceRepository;
    private final BillingInvoiceItemRepository billingInvoiceItemRepository;

    public PaymentAllocationService(
            PaymentAllocationRepository paymentAllocationRepository,
            PatientPaymentRepository patientPaymentRepository,
            BillingInvoiceRepository billingInvoiceRepository,
            BillingInvoiceItemRepository billingInvoiceItemRepository
    ) {
        this.paymentAllocationRepository = paymentAllocationRepository;
        this.patientPaymentRepository = patientPaymentRepository;
        this.billingInvoiceRepository = billingInvoiceRepository;
        this.billingInvoiceItemRepository = billingInvoiceItemRepository;
    }

    public PaymentAllocation create(PaymentAllocationCreateVM vm) {
        LOG.debug("Request to create PaymentAllocation : {}", vm);

        BillingInvoice invoice = billingInvoiceRepository.findById(vm.invoiceId())
                .orElseThrow(() -> new BadRequestAlertException(
                        "BillingInvoice not found with id " + vm.invoiceId(),
                        "billingInvoice",
                        "notfound"
                ));

        BillingInvoiceItem invoiceItem = null;
        if (vm.invoiceItemId() != null) {
            invoiceItem = billingInvoiceItemRepository.findById(vm.invoiceItemId())
                    .orElseThrow(() -> new BadRequestAlertException(
                            "BillingInvoiceItem not found with id " + vm.invoiceItemId(),
                            "billingInvoiceItem",
                            "notfound"
                    ));
        }

        PaymentAllocation allocation = PaymentAllocation.builder()
                .invoice(invoice)
                .invoiceItem(invoiceItem)
                .allocatedAmount(vm.allocatedAmount())
                .build();

        PaymentAllocation saved = paymentAllocationRepository.save(allocation);
        LOG.debug("Created PaymentAllocation: {}", saved);

        return saved;
    }

    public Optional<PaymentAllocation> update(Long id, PaymentAllocationUpdateVM vm) {
        LOG.debug("Request to update PaymentAllocation id={} with {}", id, vm);

        PaymentAllocation allocation = paymentAllocationRepository.findById(id)
                .orElseThrow(() -> new BadRequestAlertException(
                        "PaymentAllocation not found with id " + id,
                        "paymentAllocation",
                        "notfound"
                ));

        if (vm.paymentId() != null) {
            PatientPayment payment = patientPaymentRepository.findById(vm.paymentId())
                    .orElseThrow(() -> new BadRequestAlertException(
                            "PatientPayment not found with id " + vm.paymentId(),
                            "patientPayment",
                            "notfound"
                    ));
            allocation.setPayment(payment);
        }

        if (vm.invoiceId() != null) {
            BillingInvoice invoice = billingInvoiceRepository.findById(vm.invoiceId())
                    .orElseThrow(() -> new BadRequestAlertException(
                            "BillingInvoice not found with id " + vm.invoiceId(),
                            "billingInvoice",
                            "notfound"
                    ));
            allocation.setInvoice(invoice);
        }

        if (vm.invoiceItemId() != null) {
            BillingInvoiceItem invoiceItem = billingInvoiceItemRepository.findById(vm.invoiceItemId())
                    .orElseThrow(() -> new BadRequestAlertException(
                            "BillingInvoiceItem not found with id " + vm.invoiceItemId(),
                            "billingInvoiceItem",
                            "notfound"
                    ));
            allocation.setInvoiceItem(invoiceItem);
        }

        if (vm.allocatedAmount() != null) allocation.setAllocatedAmount(vm.allocatedAmount());

        PaymentAllocation updated = paymentAllocationRepository.save(allocation);
        LOG.debug("Updated PaymentAllocation: {}", updated);

        return Optional.of(updated);
    }

    @Transactional(readOnly = true)
    public Page<PaymentAllocation> findAll(Pageable pageable) {
        LOG.debug("Request to get PaymentAllocations: {}", pageable);
        return paymentAllocationRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<PaymentAllocation> findOne(Long id) {
        LOG.debug("Request to get PaymentAllocation : {}", id);
        return paymentAllocationRepository.findById(id);
    }
}
