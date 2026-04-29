package com.rzk.vehicleservice.repository;

import com.rzk.vehicleservice.model.VehicleStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleStatusHistoryRepository extends JpaRepository<VehicleStatusHistory, Integer> {

    List<VehicleStatusHistory> findByVehicleIdOrderByChangedAtDesc(Long vehicleId);
}
