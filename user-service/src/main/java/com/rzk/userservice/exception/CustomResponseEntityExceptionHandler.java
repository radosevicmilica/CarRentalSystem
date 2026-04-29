package com.rzk.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorEntity> handleRoleNotFoundException(RoleNotFoundException exception){
        ErrorEntity errorEntity = new ErrorEntity(exception.getMessage(), LocalDate.now());
        return new ResponseEntity<>(errorEntity, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityDoesNotExistException.class)
    public ResponseEntity<ErrorEntity> handleEntityDoesNotExist(EntityDoesNotExistException ex){
        return new ResponseEntity<>(new ErrorEntity(ex.getMessage(), LocalDate.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorEntity> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex){
        return new ResponseEntity<>(new ErrorEntity(ex.getMessage(), LocalDate.now()), HttpStatus.BAD_REQUEST);
    }

}
