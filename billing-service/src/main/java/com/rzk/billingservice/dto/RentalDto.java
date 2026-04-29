package com.rzk.billingservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class RentalDto {
    private Long id;
    private Long userId;
    private Double basePrice;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private List<RentalPeriodDto> rentalPeriods;
    private List<PenaltyDto> penalties;
}
