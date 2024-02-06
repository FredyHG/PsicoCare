package com.fredyhg.psicocare.utils;

import com.fredyhg.psicocare.enums.StatusTherapy;
import com.fredyhg.psicocare.models.TherapyModel;
import com.fredyhg.psicocare.models.dtos.therapy.SchedulePutRequest;
import com.fredyhg.psicocare.models.dtos.therapy.TherapyCreateRequest;

import java.time.LocalDateTime;

public class TherapyCreator {

    public static TherapyModel createValidTherapy(){
        return TherapyModel.builder()
                .createAt(LocalDateTime.now())
                .status(StatusTherapy.WAIT_DATE)
                .dateTime(LocalDateTime.of(2026, 4, 15, 14, 30, 0))
                .psychologist(PsychologistCreator.createValidPsychologist())
                .patient(PatientCreator.createValidPatient())
                .build();
    }

    public static TherapyModel createInvalidTherapy(){
        return TherapyModel.builder()
                .createAt(LocalDateTime.now())
                .status(StatusTherapy.FINISH)
                .dateTime(LocalDateTime.of(2026, 4, 15, 14, 30, 0))
                .psychologist(PsychologistCreator.createValidPsychologist())
                .patient(PatientCreator.createValidPatient())
                .build();
    }

    public static TherapyModel createValidTherapyWithStatusWaitConfirmation(){
        return TherapyModel.builder()
                .createAt(LocalDateTime.now())
                .status(StatusTherapy.WAIT_CONFIRMATION)
                .dateTime(LocalDateTime.of(2026, 4, 15, 14, 30, 0))
                .psychologist(PsychologistCreator.createValidPsychologist())
                .patient(PatientCreator.createValidPatient())
                .build();
    }

    public static TherapyCreateRequest createValidTherapyCreateRequest(){
        return TherapyCreateRequest.builder()
                .crpPsychologist("123123")
                .cpfPatient("12121")
                .dateTime(LocalDateTime.of(2026, 4, 15, 14, 30, 0))
                .build();
    }

    public static TherapyModel createValidTherapyWithStatusWait_Confirmation() {
        return TherapyModel.builder()
                .createAt(LocalDateTime.now())
                .status(StatusTherapy.WAIT_CONFIRMATION)
                .dateTime(LocalDateTime.of(2026, 4, 15, 14, 30, 0))
                .psychologist(PsychologistCreator.createValidPsychologist())
                .patient(PatientCreator.createValidPatient())
                .build();
    }

    public static SchedulePutRequest createValidSchedulePutRequest(){
        return SchedulePutRequest.builder()
                .patientCPF("123456")
                .psychologistCRP("1234")
                .build();
    }
}
