package com.fredyhg.psicocare.exceptions.psychologist;

public class PsychologistNotFoundException extends RuntimeException{
    public PsychologistNotFoundException(String msg){
        super(msg);
    }
}
