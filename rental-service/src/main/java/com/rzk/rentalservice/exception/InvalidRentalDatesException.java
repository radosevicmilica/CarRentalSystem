package com.rzk.rentalservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvalidRentalDatesException extends RuntimeException{
    private String message;
}
