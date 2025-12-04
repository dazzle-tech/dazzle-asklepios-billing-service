package com.dazzle.asklepios.web.rest;

import com.dazzle.asklepios.domain.BillingInvoiceItem;
import com.dazzle.asklepios.service.BillingInvoiceItemService;
import com.dazzle.asklepios.web.rest.Helper.PaginationUtil;
import com.dazzle.asklepios.web.rest.vm.BillingInvoiceItemCreateVM;
import com.dazzle.asklepios.web.rest.vm.BillingInvoiceItemResponseVM;
import com.dazzle.asklepios.web.rest.vm.BillingInvoiceItemUpdateVM;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/billing")
public class BillingInvoiceItemController {

    private static final Logger LOG = LoggerFactory.getLogger(BillingInvoiceItemController.class);

    private final BillingInvoiceItemService billingInvoiceItemService;

    public BillingInvoiceItemController(BillingInvoiceItemService billingInvoiceItemService) {
        this.billingInvoiceItemService = billingInvoiceItemService;
    }

    @PostMapping("/invoice-item")
    public ResponseEntity<BillingInvoiceItemResponseVM> createInvoiceItem(
            @Valid @RequestBody BillingInvoiceItemCreateVM vm
    ) {
        LOG.debug("REST create BillingInvoiceItem payload={}", vm);
        BillingInvoiceItem item = billingInvoiceItemService.create(vm);
        BillingInvoiceItemResponseVM responseVM = BillingInvoiceItemResponseVM.ofEntity(item);

        return ResponseEntity
                .created(URI.create("/api/billing/invoice-item/" + item.getId()))
                .body(responseVM);
    }

    @PutMapping("/invoice-item/{id}")
    public ResponseEntity<BillingInvoiceItemResponseVM> updateInvoiceItem(
            @PathVariable Long id,
            @Valid @RequestBody BillingInvoiceItemUpdateVM vm
    ) {
        LOG.debug("REST update BillingInvoiceItem id={} payload={}", id, vm);
        return billingInvoiceItemService.update(id, vm)
                .map(BillingInvoiceItemResponseVM::ofEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/invoice-item")
    public ResponseEntity<List<BillingInvoiceItemResponseVM>> getAllInvoiceItems(
            @ParameterObject Pageable pageable
    ) {
        LOG.debug("REST list BillingInvoiceItems pageable={}", pageable);
        Page<BillingInvoiceItem> page = billingInvoiceItemService.findAll(pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
                ServletUriComponentsBuilder.fromCurrentRequest(),
                page
        );

        List<BillingInvoiceItemResponseVM> body = page.getContent().stream()
                .map(BillingInvoiceItemResponseVM::ofEntity)
                .toList();

        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    @GetMapping("/invoice-item/{id}")
    public ResponseEntity<BillingInvoiceItemResponseVM> getInvoiceItem(@PathVariable Long id) {
        LOG.debug("REST get BillingInvoiceItem id={}", id);
        return billingInvoiceItemService.findOne(id)
                .map(BillingInvoiceItemResponseVM::ofEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
