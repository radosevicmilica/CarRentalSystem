package com.rzk.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillDto {
    private Long id;
    private Long rentalId;
    private Double totalAmount;
}
