package com.fredyhg.psicocare.repositories;

import com.fredyhg.psicocare.models.PatientModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<PatientModel, UUID> {

    Optional<PatientModel>findByCpf(String cpf);

    Optional<PatientModel>findByEmail(String email);

}
