package com.rzk.rentalservice.proxy;

import com.rzk.rentalservice.dto.VehicleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "vehicle-service")
public interface VehicleClient {

    @GetMapping("/vehicles/{id}")
    VehicleDto getVehicleById(@PathVariable  Long id);

    @PutMapping("/vehicles/{id}/status/{status}")
    VehicleDto updateStatus(@PathVariable Long id, @PathVariable String status);
}
