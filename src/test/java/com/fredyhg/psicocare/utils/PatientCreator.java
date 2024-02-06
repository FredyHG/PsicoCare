package com.fredyhg.psicocare.utils;

import com.fredyhg.psicocare.models.PatientModel;
import com.fredyhg.psicocare.models.dtos.patient.PatientPostRequest;
import com.fredyhg.psicocare.models.dtos.patient.PatientPutRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class PatientCreator {
    public static PatientModel createValidPatient(){
        return PatientModel.builder()
                .cpf("123456")
                .createAt(LocalDateTime.now())
                .phone("1234567")
                .birthDate(LocalDate.now())
                .lastName("lastName")
                .name("name")
                .email("email@email.com")
                .build();
    }

    public static PatientPostRequest createValidPatientPostRequest(){
        return PatientPostRequest.builder()
                .cpf("123456")
                .phone("1234567")
                .birthDate(LocalDate.now().minusYears(18))
                .lastName("lastName")
                .name("name")
                .email("example@email.com")
                .build();
    }

    public static PatientPutRequest createValidPatientPutRequest(){
        return PatientPutRequest.builder()
                .cpf("123456")
                .name(Optional.of("Name"))
                .lastName(Optional.of("lastName"))
                .birthDate(Optional.of(LocalDate.now().minusYears(4)))
                .email(Optional.of("email@email.com"))
                .phone(Optional.of("1234567"))
                .build();
    }

    public static List<PatientModel> createValidListOfPatients(){
        return List.of(createValidPatient());
    }

    public static Page<PatientModel> createValidPageListPatients(){
        return new PageImpl<>(List.of(createValidPatient()));
    }
}
