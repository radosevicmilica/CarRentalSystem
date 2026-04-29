package com.rzk.billingservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EntityAlreadyExistsException extends RuntimeException{
    private String message;
}
