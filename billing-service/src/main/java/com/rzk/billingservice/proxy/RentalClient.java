package com.rzk.billingservice.proxy;

import com.rzk.billingservice.dto.RentalDto;
import com.rzk.billingservice.dto.RentalSummaryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("rental-service")
public interface RentalClient {

    @GetMapping("/rentals/{id}")
    RentalDto getRentalById(@PathVariable Long id);

    @GetMapping("/rentals/{rentalId}/summary")
    RentalSummaryDto getRentalSummary(@PathVariable Long rentalId);

}
