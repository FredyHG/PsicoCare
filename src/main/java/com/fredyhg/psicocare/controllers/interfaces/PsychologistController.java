package com.fredyhg.psicocare.controllers.interfaces;

import com.fredyhg.psicocare.exceptions.ResponseMessage;
import com.fredyhg.psicocare.models.dtos.PsychologistPostRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface PsychologistController {

    @Operation(summary = "Create a Psychologist", description = "Creates a new psychologist based on the information provided in the PsychologistPostRequest object.", tags = "ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Psychologist registered successfully"),
            @ApiResponse(responseCode = "409", description = "Psychologist already registered")
    })
    ResponseEntity<ResponseMessage> createPsychologist(PsychologistPostRequest psychologistPostRequest);

}
