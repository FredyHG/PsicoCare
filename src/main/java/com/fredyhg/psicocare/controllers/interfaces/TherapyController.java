package com.fredyhg.psicocare.controllers.interfaces;

import com.fredyhg.psicocare.exceptions.utils.ResponseMessage;
import com.fredyhg.psicocare.models.dtos.code.Code;
import com.fredyhg.psicocare.models.dtos.therapy.ReschedulePutRequest;
import com.fredyhg.psicocare.models.dtos.therapy.SchedulePutRequest;
import com.fredyhg.psicocare.models.dtos.therapy.TherapyCreateRequest;
import com.fredyhg.psicocare.models.dtos.therapy.TherapyGetRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface TherapyController {

    @Operation(summary = "Schedule a therapy", description = "Schedule a new therapy based on the information provided in the TherapyCreateRequest object.", tags = "Employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Therapy scheduled successfully"),
            @ApiResponse(responseCode = "409", description = "Therapy already scheduled")
    })
    ResponseEntity<ResponseMessage> scheduleTherapy(TherapyCreateRequest therapyCreateRequest);

    @Operation(summary = "Reschedule therapy based on the information provided in the ReschedulePutRequest object.", tags = "Employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Therapy rescheduled successfully"),
            @ApiResponse(responseCode = "404", description = "Therapy not found")
    })
    ResponseEntity<ResponseMessage> rescheduleTherapy(ReschedulePutRequest reschedulePutRequest);

    @Operation(summary = "Cancel therapy based on the information provided in the SchedulePutRequest object.", tags = "All")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Therapy canceled successfully"),
            @ApiResponse(responseCode = "404", description = "Therapy not found")
    })
    ResponseEntity<ResponseMessage> cancelTherapy(SchedulePutRequest schedulePutRequest);

    @Operation(summary = "Confirm therapy based on the information provided in the SchedulePutRequest object.", tags = "All")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Therapy confirmed successfully"),
            @ApiResponse(responseCode = "404", description = "Therapy not found")
    })
    ResponseEntity<ResponseMessage> confirmTherapy(UUID id, Code code);

    @Operation(summary = "Pageable list of therapies pending", tags = "Employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Therapies listed successfully")
    })
    ResponseEntity<Page<TherapyGetRequest>> getAllPendingTherapies(Pageable pageable);
}
