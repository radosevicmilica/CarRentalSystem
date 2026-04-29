package com.rzk.userservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailAlreadyExistsException extends RuntimeException{
    private String message;
}
