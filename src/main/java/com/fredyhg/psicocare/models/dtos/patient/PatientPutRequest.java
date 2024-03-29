package com.fredyhg.psicocare.models.dtos.patient;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientPutRequest {
    private Optional<String> name;
    private Optional<String> lastName;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Optional<LocalDate> birthDate;
    private String cpf;
    private Optional<String> email;
    private Optional<String> phone;
}