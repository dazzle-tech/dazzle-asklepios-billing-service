package com.dazzle.asklepios.web.rest;

import com.dazzle.asklepios.domain.PatientPayment;
import com.dazzle.asklepios.service.PatientPaymentService;
import com.dazzle.asklepios.web.rest.Helper.PaginationUtil;
import com.dazzle.asklepios.web.rest.vm.PatientPaymentCreateVM;
import com.dazzle.asklepios.web.rest.vm.PatientPaymentResponseVM;
import com.dazzle.asklepios.web.rest.vm.PatientPaymentUpdateVM;
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
public class PatientPaymentController {

    private static final Logger LOG = LoggerFactory.getLogger(PatientPaymentController.class);

    private final PatientPaymentService patientPaymentService;

    public PatientPaymentController(PatientPaymentService patientPaymentService) {
        this.patientPaymentService = patientPaymentService;
    }

    @PostMapping("/payment")
    public ResponseEntity<PatientPaymentResponseVM> createPayment(
            @Valid @RequestBody PatientPaymentCreateVM vm
    ) {
        LOG.debug("REST create PatientPayment payload={}", vm);
        PatientPayment payment = patientPaymentService.create(vm);
        PatientPaymentResponseVM responseVM = PatientPaymentResponseVM.ofEntity(payment);

        return ResponseEntity
                .created(URI.create("/api/billing/payment/" + payment.getId()))
                .body(responseVM);
    }

    @PutMapping("/payment/{id}")
    public ResponseEntity<PatientPaymentResponseVM> updatePayment(
            @PathVariable Long id,
            @Valid @RequestBody PatientPaymentUpdateVM vm
    ) {
        LOG.debug("REST update PatientPayment id={} payload={}", id, vm);
        return patientPaymentService.update(id, vm)
                .map(PatientPaymentResponseVM::ofEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/payment")
    public ResponseEntity<List<PatientPaymentResponseVM>> getAllPayments(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false) String patientKey
    ) {
        LOG.debug("REST list PatientPayments pageable={} patientKey={}", pageable, patientKey);

        Page<PatientPayment> page = (patientKey == null || patientKey.isBlank())
                ? patientPaymentService.findAll(pageable)
                : patientPaymentService.findByPatientKey(patientKey, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
                ServletUriComponentsBuilder.fromCurrentRequest(),
                page
        );

        List<PatientPaymentResponseVM> body = page.getContent().stream()
                .map(PatientPaymentResponseVM::ofEntity)
                .toList();

        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    @GetMapping("/payment/{id}")
    public ResponseEntity<PatientPaymentResponseVM> getPayment(@PathVariable Long id) {
        LOG.debug("REST get PatientPayment id={}", id);
        return patientPaymentService.findOne(id)
                .map(PatientPaymentResponseVM::ofEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
