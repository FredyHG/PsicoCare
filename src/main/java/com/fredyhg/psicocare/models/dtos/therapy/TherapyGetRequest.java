package com.fredyhg.psicocare.models.dtos.therapy;

import com.fredyhg.psicocare.enums.StatusTherapy;
import com.fredyhg.psicocare.models.PatientModel;
import com.fredyhg.psicocare.models.PsychologistModel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class TherapyGetRequest {

    private PatientModel patient;

    private PsychologistModel psychologist;

    private StatusTherapy status;

    private LocalDateTime dateTime;

    private LocalDateTime createAt;
}
