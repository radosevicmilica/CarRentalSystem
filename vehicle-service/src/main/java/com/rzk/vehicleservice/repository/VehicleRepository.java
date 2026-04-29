package com.rzk.vehicleservice.repository;

import com.rzk.vehicleservice.model.Vehicle;
import com.rzk.vehicleservice.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByCurrentStatus(String status);

    Optional<Vehicle> findByBrandAndModelAndVehicleType(String brand, String model, VehicleType vehicleType);
}
