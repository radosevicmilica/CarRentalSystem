package com.rzk.billingservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorEntity extends RuntimeException{
    private String message;
    private LocalDateTime time;
}
