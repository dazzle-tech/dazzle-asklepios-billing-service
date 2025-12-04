package com.dazzle.asklepios.service;

import com.dazzle.asklepios.domain.PatientPayment;
import com.dazzle.asklepios.repository.PatientPaymentRepository;
import com.dazzle.asklepios.web.rest.errors.BadRequestAlertException;
import com.dazzle.asklepios.web.rest.vm.PatientPaymentCreateVM;
import com.dazzle.asklepios.web.rest.vm.PatientPaymentUpdateVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PatientPaymentService {

    private static final Logger LOG = LoggerFactory.getLogger(PatientPaymentService.class);

    private final PatientPaymentRepository patientPaymentRepository;

    public PatientPaymentService(PatientPaymentRepository patientPaymentRepository) {
        this.patientPaymentRepository = patientPaymentRepository;
    }

    public PatientPayment create(PatientPaymentCreateVM vm) {
        LOG.debug("Request to create PatientPayment : {}", vm);

        PatientPayment payment = PatientPayment.builder()
                .patientKey(vm.patientKey())
                .facilityId(vm.facilityId())
                .paymentType(vm.paymentType())
                .paymentMethod(vm.paymentMethod())
                .paymentDate(vm.paymentDate())
                .amount(vm.amount())
                .currency(vm.currency())
                .reference(vm.reference())
                .notes(vm.notes())
                .build();

        PatientPayment saved = patientPaymentRepository.save(payment);
        LOG.debug("Created PatientPayment: {}", saved);
        return saved;
    }

    public Optional<PatientPayment> update(Long id, PatientPaymentUpdateVM vm) {
        LOG.debug("Request to update PatientPayment id={} with {}", id, vm);

        PatientPayment payment = patientPaymentRepository.findById(id)
                .orElseThrow(() -> new BadRequestAlertException(
                        "PatientPayment not found with id " + id,
                        "patientPayment",
                        "notfound"
                ));

        if (vm.patientKey() != null) payment.setPatientKey(vm.patientKey());
        if (vm.facilityId() != null) payment.setFacilityId(vm.facilityId());
        if (vm.paymentType() != null) payment.setPaymentType(vm.paymentType());
        if (vm.paymentMethod() != null) payment.setPaymentMethod(vm.paymentMethod());
        if (vm.paymentDate() != null) payment.setPaymentDate(vm.paymentDate());
        if (vm.amount() != null) payment.setAmount(vm.amount());
        if (vm.currency() != null) payment.setCurrency(vm.currency());
        if (vm.reference() != null) payment.setReference(vm.reference());
        if (vm.notes() != null) payment.setNotes(vm.notes());

        PatientPayment updated = patientPaymentRepository.save(payment);
        LOG.debug("Updated PatientPayment: {}", updated);

        return Optional.of(updated);
    }

    @Transactional(readOnly = true)
    public Page<PatientPayment> findAll(Pageable pageable) {
        LOG.debug("Request to get PatientPayments: {}", pageable);
        return patientPaymentRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<PatientPayment> findByPatientKey(String patientKey, Pageable pageable) {
        LOG.debug("Request to get PatientPayments by patientKey={} pageable={}", patientKey, pageable);
        return patientPaymentRepository.findByPatientKey(patientKey, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<PatientPayment> findOne(Long id) {
        LOG.debug("Request to get PatientPayment : {}", id);
        return patientPaymentRepository.findById(id);
    }
}
