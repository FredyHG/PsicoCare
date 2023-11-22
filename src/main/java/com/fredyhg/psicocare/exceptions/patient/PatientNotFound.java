package com.fredyhg.psicocare.exceptions.patient;

public class PatientNotFound extends RuntimeException{
    public PatientNotFound(String msg){
        super(msg);
    }
}
