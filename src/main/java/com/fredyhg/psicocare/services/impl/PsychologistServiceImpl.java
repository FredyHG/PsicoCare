package com.fredyhg.psicocare.services.impl;

import com.fredyhg.psicocare.exceptions.psychologist.PsychologistAlreadyRegisteredException;
import com.fredyhg.psicocare.exceptions.psychologist.PsychologistNotFoundException;
import com.fredyhg.psicocare.models.PsychologistModel;
import com.fredyhg.psicocare.models.dtos.psychologist.PsychologistGetRequest;
import com.fredyhg.psicocare.models.dtos.psychologist.PsychologistPostRequest;
import com.fredyhg.psicocare.repositories.PsychologistRepository;
import com.fredyhg.psicocare.security.services.JwtService;
import com.fredyhg.psicocare.security.services.UserService;
import com.fredyhg.psicocare.services.PsychologistService;
import com.fredyhg.psicocare.utils.ModelMappers;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PsychologistServiceImpl implements PsychologistService {

    private final PsychologistRepository psychologistRepository;

    private final UserService userService;

    private final JwtService jwtService;

    @Transactional
    public void createPsychologist(PsychologistPostRequest psychologistPostRequest) {

        var psychologist = ModelMappers.psychologistPostRequestToPsychologistModel(psychologistPostRequest);

        ensurePsychologistNonExistsByCrp(psychologist.getCrp());

        PsychologistModel psychologistSaved = psychologistRepository.save(psychologist);

        userService.createUser(psychologistSaved);
    }

    public PsychologistModel ensurePsychologistByCrpExists(String crp) {
        return psychologistRepository.findByCrp(crp).orElseThrow(
                () -> new PsychologistNotFoundException("Psychologist not found"));
    }

    public void ensurePsychologistNonExistsByCrp(String crp) {
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

    public List<PsychologistModel> getAllPsychologists() {
        return psychologistRepository.findAll();
    }


    public Optional<PsychologistModel> getOptionalPsychologistByCRP(String crp) {
        return psychologistRepository.findByCrp(crp);
    }

    public PsychologistGetRequest getPsychologistFromToken(String token) {

        String email = jwtService.extractUsername(token);

        PsychologistModel psychologistsByEmail = getPsychologistsByEmail(email);

        return ModelMappers.psychologistModelToPsychologistGetRequest(psychologistsByEmail);
    }

    public PsychologistModel getPsychologistsByEmail(String email) {

        return psychologistRepository.findByEmail(email).orElseThrow(
                () -> new PsychologistNotFoundException("Psychologist not found"));

    }

    public Page<PsychologistGetRequest> getAllPsychologistsFiltered(Optional<String> name,
                                                                    Optional<String> lastName,
                                                                    Optional<String> crp,
                                                                    Optional<String> email,
                                                                    Pageable pageable) {

        return psychologistRepository.findAllFiltered(name.orElse(null),
                lastName.orElse(null),
                crp.orElse(null),
                email.orElse(null),
                pageable).map(ModelMappers::psychologistModelToPsychologistGetRequest);

    }
}
