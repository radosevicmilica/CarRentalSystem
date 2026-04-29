package com.rzk.userservice.proxy;

import com.rzk.userservice.dto.RentalDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("rental-service")
public interface RentalClient {

    @GetMapping("/rentals/byUser/{userId}")
    List<RentalDto> getRentalsByUserId(@PathVariable Long userId);
}
