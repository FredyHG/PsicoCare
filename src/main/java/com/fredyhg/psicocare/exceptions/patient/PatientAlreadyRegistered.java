package com.fredyhg.psicocare.exceptions.patient;

public class PatientAlreadyRegistered extends RuntimeException{
    public PatientAlreadyRegistered(String msg){
        super(msg);
    }
}
