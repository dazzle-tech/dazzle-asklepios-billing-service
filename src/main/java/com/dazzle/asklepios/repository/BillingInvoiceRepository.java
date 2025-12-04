package com.dazzle.asklepios.repository;

import com.dazzle.asklepios.domain.BillingInvoice;
import com.dazzle.asklepios.domain.enumeration.BillingInvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingInvoiceRepository extends JpaRepository<BillingInvoice, Long> {

    Page<BillingInvoice> findByPatientKey(String patientKey, Pageable pageable);

    Page<BillingInvoice> findByFacilityId(Long facilityId, Pageable pageable);

    Page<BillingInvoice> findByPatientKeyAndStatus(
            String patientKey,
            BillingInvoiceStatus status,
            Pageable pageable
    );
}
