package com.rzk.rentalservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler  extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityDoesNotExistException.class)
    public ResponseEntity<ErrorEntity> handleEntityDoesNotExist(EntityDoesNotExistException ex){
        return new ResponseEntity<>(new ErrorEntity(ex.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorEntity> handleEntityAlreadyExists(EntityAlreadyExistsException ex){
        return new ResponseEntity<>(new ErrorEntity(ex.getMessage(), LocalDateTime.now()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidRentalDatesException.class)
    public ResponseEntity<ErrorEntity> handleInvalidRentalDates(InvalidRentalDatesException ex){
        return new ResponseEntity<>(new ErrorEntity(ex.getMessage(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

}
