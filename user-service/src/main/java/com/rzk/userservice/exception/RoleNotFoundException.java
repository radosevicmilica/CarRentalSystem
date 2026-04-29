package com.rzk.userservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoleNotFoundException extends RuntimeException{
    private String message;
}
