package com.fredyhg.psicocare.exceptions.security;

public class PasswordMismatchException extends RuntimeException{
    public PasswordMismatchException(String msg){
        super(msg);
    }
}
