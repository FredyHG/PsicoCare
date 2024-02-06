package com.fredyhg.psicocare.exceptions.therapy;

public class TherapyNotFoundException extends RuntimeException{
    public TherapyNotFoundException(String msg){
        super(msg);
    }
}
