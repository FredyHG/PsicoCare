package com.fredyhg.psicocare.controllers.interfaces;

import com.fredyhg.psicocare.exceptions.utils.ResponseMessage;
import com.fredyhg.psicocare.models.dtos.psychologist.PsychologistGetRequest;
import com.fredyhg.psicocare.models.dtos.psychologist.PsychologistPostRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface PsychologistController {

    @Operation(summary = "Create a Psychologist", description = "Creates a new psychologist based on the information provided in the PsychologistPostRequest object.", tags = "ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Psychologist registered successfully"),
            @ApiResponse(responseCode = "409", description = "Psychologist already registered")
    })
    ResponseEntity<ResponseMessage> createPsychologist(PsychologistPostRequest psychologistPostRequest);

    @Operation(summary = "Get a pageable list of the Psychologist", tags = "Employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Psychologist listed successfully"),
    })
    ResponseEntity<Page<PsychologistGetRequest>> getAllPsychologists(Pageable pageable);
}
