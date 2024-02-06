package com.fredyhg.psicocare.exceptions.therapy;

public class TherapyInvalidStatusException extends RuntimeException{
    public TherapyInvalidStatusException(String msg){
        super(msg);
    }
}
