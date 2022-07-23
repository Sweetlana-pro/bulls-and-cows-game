/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.guessthenumber.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.mycompany.guessthenumber.servicelayer.InvalidGuessException;
import com.mycompany.guessthenumber.servicelayer.NoGameException;

/**
 *
 * @author Sweetlana Protsenko
 */
@ControllerAdvice
@RestController
public class GuessTheNumberExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String CONSTRAINT_MESSAGE = "Could not find your request. "
            + "Please ensure it is valid and try again.";

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public final ResponseEntity<Error> handleSqlException(
            SQLIntegrityConstraintViolationException ex,
            WebRequest request) {

        Error err = new Error();
        err.setMessage(CONSTRAINT_MESSAGE);
        return new ResponseEntity<>(err, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
    @ExceptionHandler(InvalidGuessException.class)
    public final ResponseEntity<Error> handleBadGuessException (InvalidGuessException ex, WebRequest request){
        Error err = new Error();
        err.setMessage(ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(NoGameException.class)
    public final ResponseEntity<Error> handleNoGameException (NoGameException ex, WebRequest request){
        Error err = new Error();
        err.setMessage(ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.UNAUTHORIZED);
    }
}
