package com.fredyhg.psicocare.exceptions.therapy;

public class TherapyAlreadyExists extends RuntimeException{
    public TherapyAlreadyExists(String msg){
        super(msg);
    }
}
