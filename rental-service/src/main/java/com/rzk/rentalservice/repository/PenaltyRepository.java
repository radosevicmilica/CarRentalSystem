package com.rzk.rentalservice.repository;

import com.rzk.rentalservice.model.Penalty;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PenaltyRepository extends JpaRepository<Penalty, Integer> {

    List<Penalty> findByRentalId(Long rentalId);

    boolean existsByRentalIdAndType(Long rentalId, String type);
}
