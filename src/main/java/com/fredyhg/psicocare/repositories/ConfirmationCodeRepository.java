package com.fredyhg.psicocare.repositories;

import com.fredyhg.psicocare.models.ConfirmationCodeModel;
import com.fredyhg.psicocare.models.TherapyModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCodeModel, UUID> {

    Optional<ConfirmationCodeModel> findByTherapy(TherapyModel therapyModel);
}
