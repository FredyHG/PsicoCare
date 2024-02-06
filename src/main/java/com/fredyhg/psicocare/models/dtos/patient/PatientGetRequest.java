package com.fredyhg.psicocare.models.dtos.patient;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class PatientGetRequest {

    private String name;

    private String lastName;

    private LocalDate birthDate;

    private String phone;

    private String cpf;

    private String email;

    private LocalDateTime createAt;
}
