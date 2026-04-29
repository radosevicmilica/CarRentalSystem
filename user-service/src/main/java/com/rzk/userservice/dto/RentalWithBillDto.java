package com.rzk.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentalWithBillDto {
    private RentalDto rental;
    private BillDto bill;
}
