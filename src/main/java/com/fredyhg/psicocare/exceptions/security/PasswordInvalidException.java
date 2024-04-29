package com.fredyhg.psicocare.exceptions.security;

public class PasswordInvalidException extends RuntimeException{
    public PasswordInvalidException(String msg){
        super(msg);
    }
}
