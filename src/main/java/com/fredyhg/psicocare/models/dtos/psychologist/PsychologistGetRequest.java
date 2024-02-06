package com.fredyhg.psicocare.models.dtos.psychologist;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PsychologistGetRequest {
    private String name;

    private String lastName;

    private String crp;

    private String email;

    private String phone;

    private LocalDateTime createAt;
}
