package com.fredyhg.psicocare.controllers;

import com.fredyhg.psicocare.controllers.interfaces.TherapyController;
import com.fredyhg.psicocare.exceptions.ResponseMessage;
import com.fredyhg.psicocare.models.dtos.TherapyCreateRequest;
import com.fredyhg.psicocare.services.TherapyService;
import com.fredyhg.psicocare.utils.Utils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/therapy")
@RequiredArgsConstructor
public class TherapyControllerImpl implements TherapyController {

    private final TherapyService therapyService;

    @PostMapping("/schedule")
    public ResponseEntity<ResponseMessage> createTherapy(@RequestBody @Valid TherapyCreateRequest therapyCreateRequest){
        therapyService.createTherapy(therapyCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(Utils.createResponseMessage("Success",
                201,
                "Therapy scheduled successfully"));
    }
}
