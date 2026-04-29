package com.rzk.billingservice.repository;

import com.rzk.billingservice.model.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BillItemRepository extends JpaRepository<BillItem, Long> {
}
