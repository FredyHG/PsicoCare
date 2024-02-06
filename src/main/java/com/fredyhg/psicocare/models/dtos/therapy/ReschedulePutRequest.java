package com.fredyhg.psicocare.models.dtos.therapy;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDateTime;

@Data
@Builder
public class ReschedulePutRequest {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateTime;

    @CPF
    private String patientCPF;

    @NotBlank
    @Size(min = 3, max = 50)
    private String psychologistCRP;
}
