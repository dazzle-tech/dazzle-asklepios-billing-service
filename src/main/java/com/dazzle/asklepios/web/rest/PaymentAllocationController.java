package com.dazzle.asklepios.web.rest;

import com.dazzle.asklepios.domain.PaymentAllocation;
import com.dazzle.asklepios.service.PaymentAllocationService;
import com.dazzle.asklepios.web.rest.Helper.PaginationUtil;
import com.dazzle.asklepios.web.rest.vm.PaymentAllocationCreateVM;
import com.dazzle.asklepios.web.rest.vm.PaymentAllocationResponseVM;
import com.dazzle.asklepios.web.rest.vm.PaymentAllocationUpdateVM;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/billing")
public class PaymentAllocationController {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentAllocationController.class);

    private final PaymentAllocationService paymentAllocationService;

    public PaymentAllocationController(PaymentAllocationService paymentAllocationService) {
        this.paymentAllocationService = paymentAllocationService;
    }

    @PostMapping("/payment-allocation")
    public ResponseEntity<PaymentAllocationResponseVM> createPaymentAllocation(
            @Valid @RequestBody PaymentAllocationCreateVM vm
    ) {
        LOG.debug("REST create PaymentAllocation payload={}", vm);
        PaymentAllocation allocation = paymentAllocationService.create(vm);
        PaymentAllocationResponseVM responseVM = PaymentAllocationResponseVM.ofEntity(allocation);

        return ResponseEntity
                .created(URI.create("/api/billing/payment-allocation/" + allocation.getId()))
                .body(responseVM);
    }

    @PutMapping("/payment-allocation/{id}")
    public ResponseEntity<PaymentAllocationResponseVM> updatePaymentAllocation(
            @PathVariable Long id,
            @Valid @RequestBody PaymentAllocationUpdateVM vm
    ) {
        LOG.debug("REST update PaymentAllocation id={} payload={}", id, vm);
        return paymentAllocationService.update(id, vm)
                .map(PaymentAllocationResponseVM::ofEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/payment-allocation")
    public ResponseEntity<List<PaymentAllocationResponseVM>> getAllPaymentAllocations(
            @ParameterObject Pageable pageable
    ) {
        LOG.debug("REST list PaymentAllocations pageable={}", pageable);
        Page<PaymentAllocation> page = paymentAllocationService.findAll(pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
                ServletUriComponentsBuilder.fromCurrentRequest(),
                page
        );

        List<PaymentAllocationResponseVM> body = page.getContent().stream()
                .map(PaymentAllocationResponseVM::ofEntity)
                .toList();

        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    @GetMapping("/payment-allocation/{id}")
    public ResponseEntity<PaymentAllocationResponseVM> getPaymentAllocation(@PathVariable Long id) {
        LOG.debug("REST get PaymentAllocation id={}", id);
        return paymentAllocationService.findOne(id)
                .map(PaymentAllocationResponseVM::ofEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
