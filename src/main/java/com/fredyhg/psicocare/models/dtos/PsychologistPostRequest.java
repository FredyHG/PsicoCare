package com.fredyhg.psicocare.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class PsychologistPostRequest {

    @Size(min = 3, max = 50)
    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 3, max = 50)
    private String lastName;

    @NotBlank
    @Size(min = 3, max = 50)
    private String crp;

    @Email
    @NotBlank
    private String email;

    @Pattern(regexp = "^\\(\\d{2}\\)\\d{5}-\\d{4}$", message = "Invalid parameters in phone")
    @NotBlank
    private String phone;
}
