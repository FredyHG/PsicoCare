package com.fredyhg.psicocare.exceptions.patient;

public class PatientNotFoundException extends RuntimeException{
    public PatientNotFoundException(String msg){
        super(msg);
    }
}
