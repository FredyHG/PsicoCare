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

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("api/psychologist")
@RequiredArgsConstructor
public class PsychologistControllerImpl implements PsychologistController {

    private final PsychologistService psychologistService;

    @PostMapping("/create")
    @Override
    public ResponseEntity<ResponseMessage> createPsychologist(@RequestBody @Valid PsychologistPostRequest psychologistPostRequest){
        psychologistService.createPsychologist(psychologistPostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(Utils.createResponseMessage(
                201,
                "Psychologist created successfully"));
    }

    @GetMapping("/all")
    @Override
    public ResponseEntity<Page<PsychologistGetRequest>> getAllPsychologists(Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(psychologistService.getAllPsychologistsPageable(pageable));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<PsychologistGetRequest>> getPsychologistFiltered(@RequestParam Optional<String> name,
                                                                                @RequestParam Optional<String> lastName,
                                                                                @RequestParam Optional<String> crp,
                                                                                @RequestParam Optional<String> email,
                                                                                Pageable pageable){

        return ResponseEntity.status(HttpStatus.OK).body(psychologistService.getAllPsychologistsFiltered(name, lastName, crp, email, pageable));
    }

    @GetMapping("/byToken")
    public ResponseEntity<PsychologistGetRequest> getPsychologistFromToken(@RequestParam String token){
        return ResponseEntity.status(HttpStatus.OK).body(psychologistService.getPsychologistFromToken(token));
    }
}
