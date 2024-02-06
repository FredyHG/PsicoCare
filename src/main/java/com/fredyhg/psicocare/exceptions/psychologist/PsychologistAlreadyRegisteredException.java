package com.fredyhg.psicocare.exceptions.psychologist;

public class PsychologistAlreadyRegisteredException extends RuntimeException{
    public PsychologistAlreadyRegisteredException(String msg){
        super(msg);
    }
}
