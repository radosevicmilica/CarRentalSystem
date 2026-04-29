package com.rzk.billingservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorEntity> handleEntityAlreadyExists(EntityAlreadyExistsException ex){
        return new ResponseEntity<>(new ErrorEntity(ex.getMessage(), LocalDateTime.now()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityDoesNotExistException.class)
    public ResponseEntity<ErrorEntity> handleBillAlreadyPaidException(EntityDoesNotExistException ex){
        return new ResponseEntity<>(new ErrorEntity(ex.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BillAlreadyPaidException.class)
    public ResponseEntity<ErrorEntity> handleBillAlreadyPaidException(BillAlreadyPaidException ex){
        return new ResponseEntity<>(new ErrorEntity(ex.getMessage(), LocalDateTime.now()), HttpStatus.CONFLICT);
    }
}
