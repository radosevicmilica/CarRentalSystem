package com.rzk.vehicleservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorEntity {
    private String message;
    private LocalDateTime time;
}


