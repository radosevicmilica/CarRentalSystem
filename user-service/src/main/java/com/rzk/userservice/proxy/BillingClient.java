package com.rzk.userservice.proxy;

import com.rzk.userservice.dto.BillDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("billing-service")
public interface BillingClient {

    @GetMapping("/bills/byRental/{rentalId}")
    BillDto getBillByRentalId(@PathVariable Long rentalId);
}
