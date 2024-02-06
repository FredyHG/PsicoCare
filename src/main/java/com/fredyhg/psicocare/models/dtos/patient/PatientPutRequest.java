package com.fredyhg.psicocare.models.dtos.patient;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Optional;

@Data
@Builder
public class PatientPutRequest {
    private Optional<String> name = Optional.empty();
    private Optional<String> lastName = Optional.empty();
    private Optional<LocalDate> birthDate = Optional.empty();
    private String cpf;
    private Optional<String> email = Optional.empty();
    private Optional<String> phone = Optional.empty();
}