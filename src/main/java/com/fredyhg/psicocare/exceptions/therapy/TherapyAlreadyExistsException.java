package com.fredyhg.psicocare.exceptions.therapy;

public class TherapyAlreadyExistsException extends RuntimeException{
    public TherapyAlreadyExistsException(String msg){
        super(msg);
    }
}
