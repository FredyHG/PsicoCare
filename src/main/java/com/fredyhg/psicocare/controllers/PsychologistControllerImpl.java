package com.fredyhg.psicocare.controllers;

import com.fredyhg.psicocare.controllers.interfaces.PsychologistController;
import com.fredyhg.psicocare.exceptions.ResponseMessage;
import com.fredyhg.psicocare.models.dtos.PsychologistPostRequest;
import com.fredyhg.psicocare.services.PsychologistService;
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
@RequestMapping("api/psychologist")
@RequiredArgsConstructor
public class PsychologistControllerImpl implements PsychologistController {

    private final PsychologistService psychologistService;

    @PostMapping
    @Override
    public ResponseEntity<ResponseMessage> createPsychologist(@RequestBody @Valid PsychologistPostRequest psychologistPostRequest){
        psychologistService.createPsychologist(psychologistPostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(Utils.createResponseMessage("Success",
                201,
                "Psychologist created successfully"));
    }
}
