package com.fredyhg.psicocare.controllers.interfaces;

import com.fredyhg.psicocare.exceptions.utils.ResponseMessage;
import com.fredyhg.psicocare.models.dtos.patient.PatientGetRequest;
import com.fredyhg.psicocare.models.dtos.patient.PatientPostRequest;
import com.fredyhg.psicocare.models.dtos.patient.PatientPutRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface PatientController {

    @Operation(summary = "Register a patient", description = "Register a new patient based on the information provided in the PatientPostRequest object.", tags = "Employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient registered successfully"),
            @ApiResponse(responseCode = "409", description = "Patient already registered")
    })
    ResponseEntity<ResponseMessage> createPatient(PatientPostRequest patientPostRequest);

    @Operation(summary = "Get a pageable list of the Patients", tags = "Employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patients listed successfully"),
    })
    ResponseEntity<Page<PatientGetRequest>> getAllPatient(Pageable pageable);

    @Operation(summary = "Edit patients info", tags = "Employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "", description = "Patient edited successfully"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    ResponseEntity<ResponseMessage> editPatientInfos(PatientPutRequest patientPutRequest);


    @Operation(summary = "Get patients by name or lastName or cpf or email")
    public ResponseEntity<Page<PatientGetRequest>> getPatientsFiltered(Optional<String> name,
                                                                  Optional<String> lastName,
                                                                  Optional<String> cpf,
                                                                  Optional<String> email, Pageable pageable);
}
