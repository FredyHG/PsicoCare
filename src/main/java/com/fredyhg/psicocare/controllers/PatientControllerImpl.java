package com.fredyhg.psicocare.controllers;

import com.fredyhg.psicocare.controllers.interfaces.PatientController;
import com.fredyhg.psicocare.exceptions.utils.ResponseMessage;
import com.fredyhg.psicocare.models.dtos.patient.PatientGetRequest;
import com.fredyhg.psicocare.models.dtos.patient.PatientPostRequest;
import com.fredyhg.psicocare.models.dtos.patient.PatientPutRequest;
import com.fredyhg.psicocare.services.PatientService;
import com.fredyhg.psicocare.utils.Utils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/patient")
@RequiredArgsConstructor
public class PatientControllerImpl implements PatientController {

    private final PatientService patientService;


    @PostMapping
    @Override
    public ResponseEntity<ResponseMessage> createPatient(@RequestBody @Valid PatientPostRequest patientPostRequest){
        patientService.createPatient(patientPostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(Utils.createResponseMessage(
                201,
                "Patient registered successfully"));
    }

    @GetMapping
    @Override
    public ResponseEntity<Page<PatientGetRequest>> getAllPatient(Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(patientService.getPatients(pageable));
    }

    @PutMapping
    @Override
    public ResponseEntity<ResponseMessage> editPatientInfos(@RequestBody @Valid PatientPutRequest patientPutRequest){
        patientService.editPatientInfos(patientPutRequest);

        return ResponseEntity.status(HttpStatus.OK).body(Utils.createResponseMessage(
                200,
                "Patient info updated successfully"
        ));
    }

    @GetMapping("/filter")
    @Override
    public ResponseEntity<Page<PatientGetRequest>> getPatientsFiltered(@RequestParam Optional<String> name,
                                                                  @RequestParam Optional<String> lastName,
                                                                  @RequestParam Optional<String> cpf,
                                                                  @RequestParam Optional<String> email, Pageable pageable){

        return ResponseEntity.status(HttpStatus.OK).body(patientService.getPatientsFiltered(name, lastName, cpf, email, pageable));

    }

}
