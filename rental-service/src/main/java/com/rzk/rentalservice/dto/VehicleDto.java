package com.rzk.rentalservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleDto {
    private String brand;
    private String model;
    private Double dailyPrice;
    private String currentStatus;
}
