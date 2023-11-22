package com.fredyhg.psicocare.models.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TherapyCreateRequest {


    private String crpPsychologist;

    private String cpfPatient;

    private LocalDateTime date;

}
