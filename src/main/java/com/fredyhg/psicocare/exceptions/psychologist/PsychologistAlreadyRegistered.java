package com.fredyhg.psicocare.exceptions.psychologist;

public class PsychologistAlreadyRegistered extends RuntimeException{
    public PsychologistAlreadyRegistered(String msg){
        super(msg);
    }
}
