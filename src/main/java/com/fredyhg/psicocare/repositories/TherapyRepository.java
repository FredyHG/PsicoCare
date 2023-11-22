package com.fredyhg.psicocare.repositories;

import com.fredyhg.psicocare.models.PatientModel;
import com.fredyhg.psicocare.models.PsychologistModel;
import com.fredyhg.psicocare.models.TherapyModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TherapyRepository extends JpaRepository<TherapyModel, UUID> {

    Optional<TherapyModel>findByPatientAndPsychologist(PatientModel patient, PsychologistModel psychologist);

}
