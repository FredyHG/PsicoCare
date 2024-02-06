package com.fredyhg.psicocare.exceptions.security;

public class AuthenticationException extends RuntimeException{
    public AuthenticationException(String msg){
        super(msg);
    }
}
