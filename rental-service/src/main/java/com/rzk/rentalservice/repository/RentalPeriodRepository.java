package com.rzk.rentalservice.repository;

import com.rzk.rentalservice.model.RentalPeriod;
import jakarta.validation.constraints.Min;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalPeriodRepository extends JpaRepository<RentalPeriod, Integer> {

    List<RentalPeriod> findByRentalId(Long rentalId);

    boolean existsByRentalIdAndDayNumber(Long rentalId, int dayNumber);
}
