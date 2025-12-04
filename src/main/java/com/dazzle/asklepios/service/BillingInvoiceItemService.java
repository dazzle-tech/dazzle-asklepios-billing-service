package com.dazzle.asklepios.service;

import com.dazzle.asklepios.domain.BillingInvoice;
import com.dazzle.asklepios.domain.BillingInvoiceItem;
import com.dazzle.asklepios.repository.BillingInvoiceItemRepository;
import com.dazzle.asklepios.repository.BillingInvoiceRepository;
import com.dazzle.asklepios.web.rest.errors.BadRequestAlertException;
import com.dazzle.asklepios.web.rest.vm.BillingInvoiceItemCreateVM;
import com.dazzle.asklepios.web.rest.vm.BillingInvoiceItemUpdateVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class BillingInvoiceItemService {

    private static final Logger LOG = LoggerFactory.getLogger(BillingInvoiceItemService.class);

    private final BillingInvoiceItemRepository billingInvoiceItemRepository;
    private final BillingInvoiceRepository billingInvoiceRepository;

    public BillingInvoiceItemService(BillingInvoiceItemRepository billingInvoiceItemRepository,
                                     BillingInvoiceRepository billingInvoiceRepository) {
        this.billingInvoiceItemRepository = billingInvoiceItemRepository;
        this.billingInvoiceRepository = billingInvoiceRepository;
    }

    public BillingInvoiceItem create(BillingInvoiceItemCreateVM vm) {
        LOG.debug("Request to create BillingInvoiceItem : {}", vm);

        BillingInvoice invoice = billingInvoiceRepository.findById(vm.invoiceId())
                .orElseThrow(() -> new BadRequestAlertException(
                        "BillingInvoice not found with id " + vm.invoiceId(),
                        "billingInvoice",
                        "notfound"
                ));

        BillingInvoiceItem item = BillingInvoiceItem.builder()
                .invoice(invoice)
                .nurseServiceProductKey(vm.nurseServiceProductKey())
                .code(vm.code())
                .quantity(vm.quantity())
                .unitPrice(vm.unitPrice())
                .totalPrice(vm.totalPrice())
                .currency(vm.currency())
                .build();

        BillingInvoiceItem saved = billingInvoiceItemRepository.save(item);
        LOG.debug("Created BillingInvoiceItem: {}", saved);
        return saved;
    }

    public Optional<BillingInvoiceItem> update(Long id, BillingInvoiceItemUpdateVM vm) {
        LOG.debug("Request to update BillingInvoiceItem id={} with {}", id, vm);

        BillingInvoiceItem item = billingInvoiceItemRepository.findById(id)
                .orElseThrow(() -> new BadRequestAlertException(
                        "BillingInvoiceItem not found with id " + id,
                        "billingInvoiceItem",
                        "notfound"
                ));

        if (vm.invoiceId() != null) {
            BillingInvoice invoice = billingInvoiceRepository.findById(vm.invoiceId())
                    .orElseThrow(() -> new BadRequestAlertException(
                            "BillingInvoice not found with id " + vm.invoiceId(),
                            "billingInvoice",
                            "notfound"
                    ));
            item.setInvoice(invoice);
        }
        if (vm.nurseServiceProductKey() != null) item.setNurseServiceProductKey(vm.nurseServiceProductKey());
        if (vm.code() != null) item.setCode(vm.code());
        if (vm.quantity() != null) item.setQuantity(vm.quantity());
        if (vm.unitPrice() != null) item.setUnitPrice(vm.unitPrice());
        if (vm.totalPrice() != null) item.setTotalPrice(vm.totalPrice());
        if (vm.currency() != null) item.setCurrency(vm.currency());

        BillingInvoiceItem updated = billingInvoiceItemRepository.save(item);
        LOG.debug("Updated BillingInvoiceItem: {}", updated);

        return Optional.of(updated);
    }

    @Transactional(readOnly = true)
    public Page<BillingInvoiceItem> findAll(Pageable pageable) {
        LOG.debug("Request to get BillingInvoiceItems: {}", pageable);
        return billingInvoiceItemRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<BillingInvoiceItem> findOne(Long id) {
        LOG.debug("Request to get BillingInvoiceItem : {}", id);
        return billingInvoiceItemRepository.findById(id);
    }
}
