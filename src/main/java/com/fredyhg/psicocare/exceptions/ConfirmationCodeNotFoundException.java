package com.fredyhg.psicocare.exceptions;

public class ConfirmationCodeNotFoundException extends RuntimeException {
    public ConfirmationCodeNotFoundException(String msg) {
        super(msg);
    }
}
