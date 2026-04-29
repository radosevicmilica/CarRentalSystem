package com.rzk.billingservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class RentalPeriodDto {
    private int dayNumber;
    private LocalDate rentalDate;
}
