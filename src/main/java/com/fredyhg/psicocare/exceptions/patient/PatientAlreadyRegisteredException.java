package com.fredyhg.psicocare.exceptions.patient;

public class PatientAlreadyRegisteredException extends RuntimeException{
    public PatientAlreadyRegisteredException(String msg){
        super(msg);
    }
}
