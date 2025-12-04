package com.dazzle.asklepios.web.rest;

import com.dazzle.asklepios.domain.BillingInvoice;
import com.dazzle.asklepios.service.BillingInvoiceService;
import com.dazzle.asklepios.web.rest.Helper.PaginationUtil;
import com.dazzle.asklepios.web.rest.vm.BillingInvoiceCreateVM;
import com.dazzle.asklepios.web.rest.vm.BillingInvoiceResponseVM;
import com.dazzle.asklepios.web.rest.vm.BillingInvoiceUpdateVM;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/billing")
public class BillingInvoiceController {

    private static final Logger LOG = LoggerFactory.getLogger(BillingInvoiceController.class);

    private final BillingInvoiceService billingInvoiceService;

    public BillingInvoiceController(BillingInvoiceService billingInvoiceService) {
        this.billingInvoiceService = billingInvoiceService;
    }

    @PostMapping("/invoice")
    public ResponseEntity<BillingInvoiceResponseVM> createInvoice(
            @Valid @RequestBody BillingInvoiceCreateVM vm
    ) {
        LOG.debug("REST create BillingInvoice payload={}", vm);
        BillingInvoice invoice = billingInvoiceService.create(vm);
        BillingInvoiceResponseVM responseVM = BillingInvoiceResponseVM.ofEntity(invoice);
        LOG.debug("REST created BillingInvoice response={}", responseVM);

        return ResponseEntity
                .created(URI.create("/api/billing/invoice/" + invoice.getId()))
                .body(responseVM);
    }

    @PutMapping("/invoice/{id}")
    public ResponseEntity<BillingInvoiceResponseVM> updateInvoice(
            @PathVariable Long id,
            @Valid @RequestBody BillingInvoiceUpdateVM vm
    ) {
        LOG.debug("REST update BillingInvoice id={} payload={}", id, vm);
        return billingInvoiceService.update(id, vm)
                .map(BillingInvoiceResponseVM::ofEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/invoice")
    public ResponseEntity<List<BillingInvoiceResponseVM>> getAllInvoices(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false) String patientKey
    ) {
        LOG.debug("REST list BillingInvoices pageable={} patientKey={}", pageable, patientKey);

        Page<BillingInvoice> page = (patientKey == null || patientKey.isBlank())
                ? billingInvoiceService.findAll(pageable)
                : billingInvoiceService.findByPatientKey(patientKey, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
                ServletUriComponentsBuilder.fromCurrentRequest(),
                page
        );

        List<BillingInvoiceResponseVM> body = page.getContent().stream()
                .map(BillingInvoiceResponseVM::ofEntity)
                .toList();

        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    @GetMapping("/invoice/{id}")
    public ResponseEntity<BillingInvoiceResponseVM> getInvoice(@PathVariable Long id) {
        LOG.debug("REST get BillingInvoice id={}", id);
        return billingInvoiceService.findOne(id)
                .map(BillingInvoiceResponseVM::ofEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
