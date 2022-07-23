/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.guessthenumber.servicelayer;

/**
 *
 * @author Sweetlana Protsenko
 */
public class NoGameException extends Exception {

    public NoGameException() {
    }

    public NoGameException(String message) {
        super(message);
    }

    public NoGameException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoGameException(Throwable cause) {
        super(cause);
    }

    public NoGameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
