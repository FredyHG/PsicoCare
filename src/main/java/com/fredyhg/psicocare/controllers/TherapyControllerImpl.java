package com.fredyhg.psicocare.controllers;

import com.fredyhg.psicocare.controllers.interfaces.TherapyController;
import com.fredyhg.psicocare.exceptions.utils.ResponseMessage;
import com.fredyhg.psicocare.models.dtos.code.Code;
import com.fredyhg.psicocare.models.dtos.therapy.ReschedulePutRequest;
import com.fredyhg.psicocare.models.dtos.therapy.SchedulePutRequest;
import com.fredyhg.psicocare.models.dtos.therapy.TherapyPostRequest;
import com.fredyhg.psicocare.models.dtos.therapy.TherapyGetRequest;
import com.fredyhg.psicocare.services.impl.TherapyServiceImpl;
import com.fredyhg.psicocare.utils.Utils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/therapy")
@RequiredArgsConstructor
public class TherapyControllerImpl implements TherapyController {

    private final TherapyServiceImpl therapyServiceImpl;

    @PostMapping("/schedule")
    @Override
    public ResponseEntity<ResponseMessage> scheduleTherapy(@RequestBody @Valid TherapyPostRequest therapyPostRequest){
        therapyServiceImpl.createTherapy(therapyPostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(Utils.createResponseMessage(
                201,
                "Therapy scheduled successfully"));
    }

    @PutMapping("/reschedule")
    @Override
    public ResponseEntity<ResponseMessage> rescheduleTherapy(@RequestBody @Valid ReschedulePutRequest reschedulePutRequest){
        therapyServiceImpl.rescheduleTherapy(reschedulePutRequest);
        return ResponseEntity.status(HttpStatus.OK).body(Utils.createResponseMessage(
                200,
                "Therapy rescheduled successfully"));
    }

    @PostMapping("/cancel")
    @Override
    public ResponseEntity<ResponseMessage> cancelTherapy(@RequestBody @Valid SchedulePutRequest schedulePutRequest){
        therapyServiceImpl.cancelTherapy(schedulePutRequest);
        return ResponseEntity.status(HttpStatus.OK).body(Utils.createResponseMessage(
                200,
                "Therapy canceled successfully"));
    }

    @PostMapping("/confirm/{id}")
    @Override
    public ResponseEntity<ResponseMessage> confirmTherapy(@PathVariable UUID id, @RequestBody Code code){
        therapyServiceImpl.confirmTherapy(id, code);
        return ResponseEntity.status(HttpStatus.OK).body(Utils.createResponseMessage(
                200,
                "Therapy confirmed successfully"));
    }

    @GetMapping("/pending")
    @Override
    public ResponseEntity<Page<TherapyGetRequest>> getAllPendingTherapies(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(therapyServiceImpl.getAllTherapiesPending(pageable));
    }

    @GetMapping("/allWithStatus")
    public ResponseEntity<Page<TherapyGetRequest>> getAllTherapiesWithStatus(Pageable pageable, @RequestParam String status){
        return ResponseEntity.status(HttpStatus.OK) .body(therapyServiceImpl.getAllTherapiesWithStatus(pageable, status));
    }

    @GetMapping("/all")
    @Override
    public ResponseEntity<Page<TherapyGetRequest>> getAllTherapies(Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(therapyServiceImpl.getAllTherapies(pageable));
    }

    @GetMapping("/filter")
    @Override
    public ResponseEntity<Page<TherapyGetRequest>> getAllTherapiesFiltered(@RequestParam Optional<String> patientCPF,
                                                                           @RequestParam Optional<String> psychologistCRP,
                                                                           @RequestParam Optional<String> status,
                                                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                                                            Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(therapyServiceImpl.getAllTherapiesFiltered(patientCPF,
                                                                                                psychologistCRP,
                                                                                                status,
                                                                                                startDate,
                                                                                                endDate,
                                                                                                pageable));
    }
}
