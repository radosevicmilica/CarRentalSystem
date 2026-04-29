package com.rzk.rentalservice.dto;

import com.rzk.rentalservice.model.Rental;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RentalSummary {
    private Rental rental;
    private double totalPrice;
}
