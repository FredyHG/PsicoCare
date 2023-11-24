package com.fredyhg.psicocare.controllers;

import com.fredyhg.psicocare.controllers.interfaces.PatientController;
import com.fredyhg.psicocare.exceptions.ResponseMessage;
import com.fredyhg.psicocare.models.dtos.PatientPostRequest;
import com.fredyhg.psicocare.services.PatientService;
import com.fredyhg.psicocare.utils.Utils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/patient")
@RequiredArgsConstructor
public class PatientControllerImpl implements PatientController {

    private final PatientService patientService;

    @PostMapping
    @Override
    public ResponseEntity<ResponseMessage> createPatient(@RequestBody @Valid PatientPostRequest patientPostRequest){
        patientService.createPatient(patientPostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(Utils.createResponseMessage("Success",
                201,
                "Patient registered successfully"));
    }
}
