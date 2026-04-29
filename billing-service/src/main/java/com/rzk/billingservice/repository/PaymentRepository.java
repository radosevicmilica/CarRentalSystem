package com.rzk.billingservice.repository;

import com.rzk.billingservice.model.Bill;
import com.rzk.billingservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByBillId(Long billId);

    boolean existsByBillIdAndStatus(Long billId, String status);

    Long bill(Bill bill);
}
