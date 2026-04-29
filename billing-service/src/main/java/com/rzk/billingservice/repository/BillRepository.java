package com.rzk.billingservice.repository;

import com.rzk.billingservice.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {
    boolean existsByRentalId(Long rentalId);

    Optional<Bill> findByRentalId(Long rentalId);

}
