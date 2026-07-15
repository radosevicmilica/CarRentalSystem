package com.rzk.vehicleservice.controller;


import com.rzk.vehicleservice.model.Vehicle;
import com.rzk.vehicleservice.model.VehicleStatusHistory;
import com.rzk.vehicleservice.service.VehicleService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService service;

    @GetMapping()
    public ResponseEntity<List<Vehicle>> getAllVehicles(){
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK) ;
    }

    @DeleteMapping("/{id}/delete")
    public void deleteVehicle(@PathVariable Long id){
        service.deleteVehicle(id) ;
    }

//    {
//      "brand": "Toyota",
//      "model": "Corolla",
//      "dailyPrice": 45.0,
//      "vehicleType": {
//        "id": 1
//      },
//      "currentStatus": "AVAILABLE"
//    }
    @PostMapping
    @CircuitBreaker(name = "vehicleServiceCircuitBreaker", fallbackMethod = "vehicleServiceFallback")
    public ResponseEntity<Vehicle> create(@Valid @RequestBody Vehicle vehicle){
        return new ResponseEntity<>(service.save(vehicle), HttpStatus.CREATED);
    }

    public ResponseEntity<Vehicle> vehicleServiceFallback(RequestNotPermitted ex) {
        return new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Vehicle>> getAvailable(){
        return new ResponseEntity<>(service.getAvailableVehicles(), HttpStatus.OK) ;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable @Min(1) Long id){
        return new ResponseEntity<>(service.geVehicleById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<Vehicle> updateStatus(@PathVariable @Min(1) Long id, @PathVariable String status){
        return new ResponseEntity<>(service.updateStatus(id, status), HttpStatus.OK);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<VehicleStatusHistory>> getStatusHistory(@PathVariable @Min(1) Long id){
        return new ResponseEntity<>(service.getStatusHistory(id), HttpStatus.OK);
    }
}
