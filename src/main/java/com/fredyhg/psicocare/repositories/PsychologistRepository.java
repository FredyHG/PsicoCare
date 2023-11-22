package com.fredyhg.psicocare.repositories;

import com.fredyhg.psicocare.models.PsychologistModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PsychologistRepository extends JpaRepository<PsychologistModel, UUID> {

    Optional<PsychologistModel>findByCrp(String crp);

}
