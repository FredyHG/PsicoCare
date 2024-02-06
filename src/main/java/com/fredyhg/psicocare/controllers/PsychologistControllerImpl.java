package com.fredyhg.psicocare.controllers;

import com.fredyhg.psicocare.controllers.interfaces.PsychologistController;
import com.fredyhg.psicocare.exceptions.utils.ResponseMessage;
import com.fredyhg.psicocare.models.dtos.psychologist.PsychologistGetRequest;
import com.fredyhg.psicocare.models.dtos.psychologist.PsychologistPostRequest;
import com.fredyhg.psicocare.services.PsychologistService;
import com.fredyhg.psicocare.utils.Utils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/psychologist")
@RequiredArgsConstructor
public class PsychologistControllerImpl implements PsychologistController {

    private final PsychologistService psychologistService;

    @PostMapping
    @Override
    public ResponseEntity<ResponseMessage> createPsychologist(@RequestBody @Valid PsychologistPostRequest psychologistPostRequest){
        psychologistService.createPsychologist(psychologistPostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(Utils.createResponseMessage(
                201,
                "Psychologist created successfully"));
    }

    @GetMapping
    @Override
    public ResponseEntity<Page<PsychologistGetRequest>> getAllPsychologists(Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(psychologistService.getAllPsychologists(pageable));
    }
}
