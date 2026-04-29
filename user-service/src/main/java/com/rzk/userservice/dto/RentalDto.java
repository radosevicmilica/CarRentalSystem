package com.rzk.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentalDto {
    private Long id;
    private Long userId;
    private Double basePrice;
}
