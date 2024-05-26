package com.fredyhg.psicocare.services;

import com.fredyhg.psicocare.models.PsychologistModel;
import com.fredyhg.psicocare.models.dtos.psychologist.PsychologistGetRequest;
import com.fredyhg.psicocare.models.dtos.psychologist.PsychologistPostRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PsychologistService {

    void createPsychologist(PsychologistPostRequest psychologistPostRequest);

    PsychologistModel ensurePsychologistByCrpExists(String crp);

    void ensurePsychologistNonExistsByCrp(String crp);

    Page<PsychologistGetRequest> getAllPsychologistsPageable(Pageable pageable);

    List<PsychologistModel> getAllPsychologists();

    Optional<PsychologistModel> getOptionalPsychologistByCRP(String crp);

    PsychologistGetRequest getPsychologistFromToken(String token);

    PsychologistModel getPsychologistsByEmail(String email);

    Page<PsychologistGetRequest> getAllPsychologistsFiltered(Optional<String> name,
                                                             Optional<String> lastName,
                                                             Optional<String> crp,
                                                             Optional<String> email,
                                                             Pageable pageable);
}
