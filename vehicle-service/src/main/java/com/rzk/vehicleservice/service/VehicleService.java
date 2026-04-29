package com.rzk.vehicleservice.service;

import com.rzk.vehicleservice.exceptions.EntityAlreadyExistsException;
import com.rzk.vehicleservice.exceptions.EntityDoesNotExistException;
import com.rzk.vehicleservice.model.Vehicle;
import com.rzk.vehicleservice.model.VehicleStatusHistory;
import com.rzk.vehicleservice.repository.VehicleRepository;
import com.rzk.vehicleservice.repository.VehicleStatusHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class VehicleService {

    private final VehicleRepository vr;
    private final VehicleStatusHistoryRepository hr;

    public Vehicle save(Vehicle vehicle){
        vr.findByBrandAndModelAndVehicleType(vehicle.getBrand(), vehicle.getModel(), vehicle.getVehicleType())
                .ifPresent(v -> {
                    throw new EntityAlreadyExistsException(
                            "Vehicle with brand " + vehicle.getBrand() + ", model " + vehicle.getModel() +
                                    " and type " + vehicle.getVehicleType().getId() + " already exists"
                    );
                });
        Vehicle savedVehicle = vr.save(vehicle);

        VehicleStatusHistory history = new VehicleStatusHistory();
        history.setVehicle(savedVehicle);
        history.setStatus(savedVehicle.getCurrentStatus());
        history.setChangedAt(LocalDateTime.now());
        hr.save(history);
        return savedVehicle;
    }

    public List<Vehicle> getAvailableVehicles(){
        return vr.findByCurrentStatus("AVAILABLE");
    }

    public Vehicle geVehicleById(Long id){
        return vr.findById(id).orElseThrow(() -> new EntityDoesNotExistException("Vehicle with id " + id + " not found"));
    }

    public Vehicle updateStatus(Long id, String status){
        Vehicle vehicle = vr.findById(id).orElseThrow(()-> new EntityDoesNotExistException("Vehicle with id " + id + " not found"));
        if(!status.equals("AVAILABLE") && !status.equals("RENTED")){
            throw new IllegalArgumentException("Status must be either AVAILABLE or RENTED");
        }
        vehicle.setCurrentStatus(status);
        Vehicle updatedVehicle = vr.save(vehicle);

        VehicleStatusHistory history = new VehicleStatusHistory();
        history.setVehicle(updatedVehicle);
        history.setStatus(status);
        history.setChangedAt(LocalDateTime.now());
        hr.save(history);
        return updatedVehicle;
    }

    public List<VehicleStatusHistory> getStatusHistory(Long vehicleId){
        return hr.findByVehicleIdOrderByChangedAtDesc(vehicleId);
    }
}
