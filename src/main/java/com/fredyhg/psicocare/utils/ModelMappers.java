package com.fredyhg.psicocare.utils;

import com.fredyhg.psicocare.enums.StatusTherapy;
import com.fredyhg.psicocare.models.PatientModel;
import com.fredyhg.psicocare.models.PsychologistModel;
import com.fredyhg.psicocare.models.TherapyModel;
import com.fredyhg.psicocare.models.dtos.PatientPostRequest;
import com.fredyhg.psicocare.models.dtos.PsychologistPostRequest;
import com.fredyhg.psicocare.models.dtos.TherapyCreateRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ModelMappers {



    public static TherapyModel therapyCreateRequestToTherapyModel(TherapyCreateRequest therapyCreateRequest,
                                                                  PatientModel patient,
                                                                  PsychologistModel psychologist){

        return TherapyModel.builder()
                .createAt(LocalDateTime.now())
                .status(StatusTherapy.WAIT_DATE)
                .date(therapyCreateRequest.getDate())
                .psychologist(psychologist)
                .patient(patient)
                .build();
    }

    public static PatientModel patientPostRequestToPatientModel(PatientPostRequest patientPostRequest){
        return PatientModel.builder()
                .cpf(patientPostRequest.getCpf())
                .email(patientPostRequest.getEmail())
                .phone(patientPostRequest.getPhone())
                .name(patientPostRequest.getName())
                .lastName(patientPostRequest.getLastName())
                .birthDate(patientPostRequest.getBirthDate())
                .build();
    }

    public static PsychologistModel psychologistPostRequestToPsychologistModel(PsychologistPostRequest psychologistPostRequest){
        return PsychologistModel.builder()
                .crp(psychologistPostRequest.getCrp())
                .name(psychologistPostRequest.getName())
                .lastName(psychologistPostRequest.getLastName())
                .phone(psychologistPostRequest.getPhone())
                .email(psychologistPostRequest.getEmail())
                .createAt(LocalDateTime.now())
                .build();
    }

}
