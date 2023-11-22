package com.fredyhg.psicocare.utils;

import com.fredyhg.psicocare.models.PatientModel;
import com.fredyhg.psicocare.models.dtos.PatientPostRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PatientCreator {
    public static PatientModel createValidPatient(){
        return PatientModel.builder()
                .cpf("123456")
                .createAt(LocalDateTime.now())
                .phone("1234567")
                .birthDate(LocalDate.now())
                .lastName("lastName")
                .name("name")
                .build();
    }

    public static PatientPostRequest createValidPatientPostRequest(){
        return PatientPostRequest.builder()
                .cpf("123456")
                .phone("1234567")
                .birthDate(LocalDate.now())
                .lastName("lastName")
                .name("name")
                .build();
    }
}
