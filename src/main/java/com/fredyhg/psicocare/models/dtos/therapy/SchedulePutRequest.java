package com.fredyhg.psicocare.models.dtos.therapy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

@Data
@Builder
public class SchedulePutRequest {
    @CPF
    private String patientCPF;

    @NotBlank
    @Size(min = 3, max = 50)
    private String psychologistCRP;
}
