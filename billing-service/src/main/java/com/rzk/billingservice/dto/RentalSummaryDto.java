package com.rzk.billingservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentalSummaryDto {
    private RentalDto rental;
    private double totalPrice;
}
