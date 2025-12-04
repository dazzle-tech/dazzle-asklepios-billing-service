package com.dazzle.asklepios.repository;

import com.dazzle.asklepios.domain.PatientPayment;
import com.dazzle.asklepios.domain.enumeration.PaymentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientPaymentRepository extends JpaRepository<PatientPayment, Long> {

    Page<PatientPayment> findByPatientKey(String patientKey, Pageable pageable);

    List<PatientPayment> findByPatientKeyAndPaymentType(String patientKey, PaymentType paymentType);
}
