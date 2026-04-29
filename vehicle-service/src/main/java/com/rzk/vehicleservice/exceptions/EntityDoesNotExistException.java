package com.rzk.vehicleservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EntityDoesNotExistException extends RuntimeException{
    private String message;
}
