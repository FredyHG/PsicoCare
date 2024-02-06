package com.fredyhg.psicocare.utils;

import com.fredyhg.psicocare.models.PatientModel;
import com.fredyhg.psicocare.models.PsychologistModel;
import com.fredyhg.psicocare.models.dtos.psychologist.PsychologistPostRequest;

import java.time.LocalDateTime;
import java.util.List;

public class PsychologistCreator {

    public static PsychologistPostRequest createValidPsychologistPostRequest(){
        return PsychologistPostRequest.builder()
                .email("emailddddd@email.com")
                .phone("32323223")
                .crp("2222222")
                .name("name")
                .lastName("lastName")
                .build();
    }

    public static PsychologistModel createValidPsychologist(){
        return PsychologistModel.builder()
                .createAt(LocalDateTime.now())
                .email("email@email.com")
                .phone("32323223")
                .crp("2222222")
                .name("name")
                .lastName("lastName")
                .build();
    }

    public static List<PsychologistModel> createValidListOfPsychologist(){
        return List.of(createValidPsychologist());
    }

}
