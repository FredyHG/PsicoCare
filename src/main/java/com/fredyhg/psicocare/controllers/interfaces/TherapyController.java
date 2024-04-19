package com.fredyhg.psicocare.controllers.interfaces;

import com.fredyhg.psicocare.exceptions.utils.ResponseMessage;
import com.fredyhg.psicocare.models.dtos.code.Code;
import com.fredyhg.psicocare.models.dtos.therapy.ReschedulePutRequest;
import com.fredyhg.psicocare.models.dtos.therapy.SchedulePutRequest;
import com.fredyhg.psicocare.models.dtos.therapy.TherapyPostRequest;
import com.fredyhg.psicocare.models.dtos.therapy.TherapyGetRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface TherapyController {

    @Operation(summary = "Schedule a therapy", description = "Schedule a new therapy based on the information provided in the TherapyCreateRequest object.", tags = "PSYCHOLOGIST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Therapy scheduled successfully"),
            @ApiResponse(responseCode = "409", description = "Therapy already scheduled")
    })
    ResponseEntity<ResponseMessage> scheduleTherapy(TherapyPostRequest therapyPostRequest);

    @Operation(summary = "Reschedule therapy based on the information provided in the ReschedulePutRequest object.", tags = "PSYCHOLOGIST")
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

    @Operation(summary = "Pageable list of therapies pending", tags = "PSYCHOLOGIST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Therapies listed successfully")
    })
    ResponseEntity<Page<TherapyGetRequest>> getAllPendingTherapies(Pageable pageable);

    @Operation(summary = "Pageable list of all therapies", tags = {"PSYCHOLOGIST", "ROOT"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Therapies listed successfully")
    })
    ResponseEntity<Page<TherapyGetRequest>> getAllTherapies(Pageable pageable);

    @Operation(summary = "Pageable list of all therapies", tags = {"PSYCHOLOGIST", "ROOT"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Therapies listed successfully")
    })
    ResponseEntity<Page<TherapyGetRequest>> getAllTherapiesFiltered(@RequestParam Optional<String> patientCPF,
                                                                    @RequestParam Optional<String> psychologistCRP,
                                                                    @RequestParam Optional<String> status,
                                                                    @RequestParam LocalDateTime startDate,
                                                                    @RequestParam LocalDateTime endDate,
                                                                    Pageable pageable);
}
