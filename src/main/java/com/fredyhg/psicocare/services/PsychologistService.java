package com.fredyhg.psicocare.services;

import com.fredyhg.psicocare.exceptions.psychologist.PsychologistAlreadyRegisteredException;
import com.fredyhg.psicocare.exceptions.psychologist.PsychologistNotFoundException;
import com.fredyhg.psicocare.models.PsychologistModel;
import com.fredyhg.psicocare.models.dtos.psychologist.PsychologistGetRequest;
import com.fredyhg.psicocare.models.dtos.psychologist.PsychologistPostRequest;
import com.fredyhg.psicocare.repositories.PsychologistRepository;
import com.fredyhg.psicocare.security.services.UserService;
import com.fredyhg.psicocare.utils.ModelMappers;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PsychologistService {

    private final PsychologistRepository psychologistRepository;

    private final UserService userService;

    @Transactional
    public void createPsychologist(PsychologistPostRequest psychologistPostRequest) {

        var psychologist = ModelMappers.psychologistPostRequestToPsychologistModel(psychologistPostRequest);

        ensurePsychologistNonExistsByCrp(psychologist.getCrp());

        PsychologistModel psychologistSaved = psychologistRepository.save(psychologist);

        userService.createUser(psychologistSaved);
    }

    public PsychologistModel ensurePsychologistByCrpExists(String crp){
        return psychologistRepository.findByCrp(crp).orElseThrow(
                () -> new PsychologistNotFoundException("Psychologist not found"));
    }

    public void ensurePsychologistNonExistsByCrp(String crp){
        psychologistRepository.findByCrp(crp).ifPresent(psy -> {
            throw new PsychologistAlreadyRegisteredException("Psychologist already exists");
        });
    }

    public Page<PsychologistGetRequest> getAllPsychologistsPageable(Pageable pageable) {

        Page<PsychologistModel> psychologistsPageable = psychologistRepository.findAll(pageable);

        List<PsychologistGetRequest> listOfPsychologist = psychologistsPageable.stream()
                .map(ModelMappers::psychologistModelToPsychologistGetRequest)
                .toList();

        return new PageImpl<>(listOfPsychologist);
    }

    public List<PsychologistModel> getAllPsychologists(){
        return psychologistRepository.findAll();
    }


}
