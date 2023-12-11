package com.fredyhg.psicocare.models.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientPostRequest {

    @Size(min = 3, max = 50)
    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 3, max = 50)
    private String lastName;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;

    @CPF
    @NotBlank
    private String cpf;

    @Email
    @NotBlank
    private String email;

    @Pattern(regexp = "^\\(\\d{2}\\)\\d{5}-\\d{4}$", message = "Invalid parameters in phone")
    @NotBlank
    private String phone;

}