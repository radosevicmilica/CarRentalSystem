package com.rzk.billingservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BillAlreadyPaidException extends RuntimeException{
    private String message;
}
