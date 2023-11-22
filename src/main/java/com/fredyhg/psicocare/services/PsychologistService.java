package com.fredyhg.psicocare.services;

import com.fredyhg.psicocare.exceptions.psychologist.PsychologistAlreadyRegistered;
import com.fredyhg.psicocare.exceptions.psychologist.PsychologistNotFound;
import com.fredyhg.psicocare.models.PsychologistModel;
import com.fredyhg.psicocare.models.dtos.PsychologistPostRequest;
import com.fredyhg.psicocare.repositories.PsychologistRepository;
import com.fredyhg.psicocare.utils.ModelMappers;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PsychologistService {

    private final PsychologistRepository psychologistRepository;

    @Transactional
    public void createPsychologist(PsychologistPostRequest psychologistPostRequest) {

        var psychologist = ModelMappers.psychologistPostRequestToPsychologistModel(psychologistPostRequest);

        ensurePsychologistNonExistsByCrp(psychologist.getCrp());

        psychologistRepository.save(psychologist);
    }

    public PsychologistModel ensurePsychologistByCrpExists(String crp){
        return psychologistRepository.findByCrp(crp).orElseThrow(
                () -> new PsychologistNotFound("Psychologist not found"));
    }

    public void ensurePsychologistNonExistsByCrp(String crp){
        psychologistRepository.findByCrp(crp).ifPresent(psy -> {
            throw new PsychologistAlreadyRegistered("Psychologist already exists");
        });

    }
}
