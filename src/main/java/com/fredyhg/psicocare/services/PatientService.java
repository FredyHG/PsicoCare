package com.fredyhg.psicocare.services;

import com.fredyhg.psicocare.exceptions.patient.PatientAlreadyRegistered;
import com.fredyhg.psicocare.exceptions.patient.PatientNotFound;
import com.fredyhg.psicocare.models.PatientModel;
import com.fredyhg.psicocare.models.dtos.PatientPostRequest;
import com.fredyhg.psicocare.repositories.PatientRepository;
import com.fredyhg.psicocare.utils.ModelMappers;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    @Transactional
    public void createPatient(PatientPostRequest patient){
        ensurePatientByEmailOrCpfNonExists(patient.getEmail(), patient.getCpf());

        patientRepository.save(ModelMappers.patientPostRequestToPatientModel(patient));
    }

    public PatientModel ensurePatientByCPFExists(String cpf){
        return patientRepository.findByCpf(cpf).orElseThrow(() -> new PatientNotFound("Patient not found"));
    }

    public void ensurePatientByEmailOrCpfNonExists(String email, String cpf){

        patientRepository.findByCpf(cpf).ifPresent(pat -> {
            throw new PatientAlreadyRegistered("Patient already registered with this cpf: " + cpf);
        });

        patientRepository.findByEmail(email).ifPresent(pat -> {
            throw new PatientAlreadyRegistered("Patient already registered with this email: " + email);
        });

    }

    public void ensurePatientHasValidAge(PatientPostRequest patientPostRequest){

    }
}
