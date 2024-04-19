package com.fredyhg.psicocare.models.dtos.therapy;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TherapyPostRequest {

    private String crpPsychologist;

    private String cpfPatient;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateTime;
}