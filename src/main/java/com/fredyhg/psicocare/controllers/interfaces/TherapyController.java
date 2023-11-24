package com.fredyhg.psicocare.controllers.interfaces;

import com.fredyhg.psicocare.exceptions.ResponseMessage;
import com.fredyhg.psicocare.models.dtos.TherapyCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface TherapyController {

    @Operation(summary = "Schedule a therapy", description = "Schedule a new therapy based on the information provided in the TherapyCreateRequest object.", tags = "Employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Therapy scheduled successfully"),
            @ApiResponse(responseCode = "409", description = "Therapy already scheduled")
    })
    ResponseEntity<ResponseMessage> createTherapy(TherapyCreateRequest therapyCreateRequest);

}
