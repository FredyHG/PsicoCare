package com.fredyhg.psicocare.exceptions.security;

public class InvalidTokenException extends RuntimeException{

    public InvalidTokenException(String msg){
        super(msg);
    }

}
