package com.fredyhg.psicocare.controllers.interfaces;

import com.fredyhg.psicocare.exceptions.ResponseMessage;
import com.fredyhg.psicocare.models.dtos.PatientPostRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface PatientController {

    @Operation(summary = "Register a patient", description = "Register a new patient based on the information provided in the PatientPostRequest object.", tags = "Employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient registered successfully"),
            @ApiResponse(responseCode = "409", description = "Patient already registered")
    })
    ResponseEntity<ResponseMessage> createPatient(PatientPostRequest patientPostRequest);

}
