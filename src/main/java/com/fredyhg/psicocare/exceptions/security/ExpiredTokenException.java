package com.fredyhg.psicocare.exceptions.security;

public class ExpiredTokenException extends RuntimeException{
    public ExpiredTokenException(String msg){
        super(msg);
    }
}
