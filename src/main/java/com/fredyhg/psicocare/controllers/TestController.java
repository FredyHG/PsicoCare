package com.fredyhg.psicocare.controllers;

import com.fredyhg.psicocare.models.TherapyModel;
import com.fredyhg.psicocare.services.TherapyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/test")
@AllArgsConstructor
public class TestController {

    private final TherapyService therapyService;



}
