package com.fredyhg.psicocare.utils;

import com.fredyhg.psicocare.enums.StatusTherapy;
import com.fredyhg.psicocare.models.TherapyModel;
import com.fredyhg.psicocare.models.dtos.TherapyCreateRequest;

import java.time.LocalDateTime;

public class TherapyCreator {

    public static TherapyModel createValidTherapy(){
        return TherapyModel.builder()
                .createAt(LocalDateTime.now())
                .status(StatusTherapy.WAIT_DATE)
                .date(LocalDateTime.of(2026, 4, 15, 14, 30, 0))
                .psychologist(PsychologistCreator.createValidPsychologist())
                .patient(PatientCreator.createValidPatient())
                .build();
    }

    public static TherapyCreateRequest createValidTherapyCreateRequest(){
        return TherapyCreateRequest.builder()
                .crpPsychologist("123123")
                .cpfPatient("12121")
                .date(LocalDateTime.of(2026, 4, 15, 14, 30, 0))
                .build();
    }

}
