package com.fredyhg.psicocare.utils;

import com.fredyhg.psicocare.enums.StatusTherapy;
import com.fredyhg.psicocare.models.PatientModel;
import com.fredyhg.psicocare.models.PsychologistModel;
import com.fredyhg.psicocare.models.TherapyModel;
import com.fredyhg.psicocare.models.dtos.patient.PatientGetRequest;
import com.fredyhg.psicocare.models.dtos.patient.PatientPostRequest;
import com.fredyhg.psicocare.models.dtos.patient.PatientPutRequest;
import com.fredyhg.psicocare.models.dtos.psychologist.PsychologistGetRequest;
import com.fredyhg.psicocare.models.dtos.psychologist.PsychologistPostRequest;
import com.fredyhg.psicocare.models.dtos.therapy.TherapyCreateRequest;
import com.fredyhg.psicocare.models.dtos.therapy.TherapyGetRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class ModelMappers {

    private ModelMappers() {
    }

    public static TherapyModel therapyCreateRequestToTherapyModel(TherapyCreateRequest therapyCreateRequest,
                                                                  PatientModel patient,
                                                                  PsychologistModel psychologist){

        return TherapyModel.builder()
                .createAt(LocalDateTime.now())
                .status(StatusTherapy.WAIT_CONFIRMATION)
                .dateTime(therapyCreateRequest.getDateTime())
                .psychologist(psychologist)
                .patient(patient)
                .build();
    }

    public static PatientModel patientPostRequestToPatientModel(PatientPostRequest patientPostRequest){
        return PatientModel.builder()
                .name(patientPostRequest.getName())
                .lastName(patientPostRequest.getLastName())
                .birthDate(patientPostRequest.getBirthDate())
                .phone(patientPostRequest.getPhone())
                .cpf(patientPostRequest.getCpf())
                .email(patientPostRequest.getEmail())
                .createAt(LocalDateTime.now())
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

    public static PatientGetRequest patientModelToPatientGetRequest(PatientModel patientModel){
        return PatientGetRequest.builder()
                .name(patientModel.getName())
                .lastName(patientModel.getLastName())
                .birthDate(patientModel.getBirthDate())
                .phone(patientModel.getPhone())
                .cpf(patientModel.getCpf())
                .email(patientModel.getEmail())
                .createAt(patientModel.getCreateAt())
                .build();
    }

    public static PsychologistGetRequest psychologistModelToPsychologistGetRequest(PsychologistModel psychologistModel){
        return PsychologistGetRequest.builder()
                .name(psychologistModel.getName())
                .lastName(psychologistModel.getLastName())
                .crp(psychologistModel.getCrp())
                .email(psychologistModel.getEmail())
                .phone(psychologistModel.getPhone())
                .createAt(psychologistModel.getCreateAt())
                .build();
    }

    public static TherapyGetRequest therapyModelToTherapyGetRequest(TherapyModel therapyModel){
        return TherapyGetRequest.builder()
                .patient(therapyModel.getPatient())
                .psychologist(therapyModel.getPsychologist())
                .status(therapyModel.getStatus())
                .dateTime(therapyModel.getDateTime())
                .createAt(therapyModel.getCreateAt())
                .build();
    }

    public static PatientModel patientPutRequestToPatientModel(PatientPutRequest patientPutRequest, PatientModel patientModel){

        patientModel.setName(patientPutRequest.getName().orElse(patientModel.getName()));

        patientModel.setLastName(patientPutRequest.getLastName().orElse(patientModel.getLastName()));

        patientModel.setEmail(patientPutRequest.getEmail().orElse(patientModel.getEmail()));

        patientModel.setPhone(patientPutRequest.getPhone().orElse(patientModel.getPhone()));

        LocalDate updatedDate = patientPutRequest.getBirthDate().orElse(patientModel.getBirthDate());

        if(updatedDate.isAfter(LocalDate.now().minusYears(3)))
            patientModel.setBirthDate(updatedDate);

        return patientModel;
    }
}
