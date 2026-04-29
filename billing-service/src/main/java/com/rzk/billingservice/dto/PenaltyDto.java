package com.rzk.billingservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PenaltyDto {
    private String type; // npr. "LATE", "DAMAGE"
    private Double amount;
}
