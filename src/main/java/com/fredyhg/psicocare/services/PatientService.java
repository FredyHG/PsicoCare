package com.fredyhg.psicocare.services;

import com.fredyhg.psicocare.exceptions.patient.PatientAlreadyRegisteredException;
import com.fredyhg.psicocare.exceptions.patient.PatientNotFoundException;
import com.fredyhg.psicocare.exceptions.utils.AgeException;
import com.fredyhg.psicocare.models.PatientModel;
import com.fredyhg.psicocare.models.dtos.patient.PatientGetRequest;
import com.fredyhg.psicocare.models.dtos.patient.PatientPostRequest;
import com.fredyhg.psicocare.models.dtos.patient.PatientPutRequest;
import com.fredyhg.psicocare.repositories.PatientRepository;
import com.fredyhg.psicocare.utils.ModelMappers;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    @Transactional
    public void createPatient(PatientPostRequest patientToBeCreate){

        this.ensurePatientByEmailOrCpfNonExists(patientToBeCreate.getEmail(), patientToBeCreate.getCpf());
        this.ensurePatientHasValidAge(patientToBeCreate.getBirthDate());

        patientRepository.save(ModelMappers.patientPostRequestToPatientModel(patientToBeCreate));
    }

    public Page<PatientGetRequest> getPatients(Pageable pageable){
        Page<PatientModel> patientsPageable = patientRepository.findAll(pageable);

        List<PatientGetRequest> listOfPatient = patientsPageable.stream()
                .map(ModelMappers::patientModelToPatientGetRequest)
                .toList();

        return new PageImpl<>(listOfPatient);
    }

    @Transactional
    public void editPatientInfos(PatientPutRequest patientPutRequest){

        PatientModel patientModelToUpdate = this.ensurePatientByCPFExists(patientPutRequest.getCpf());

        PatientModel patientModel = ModelMappers.patientPutRequestToPatientModel(patientPutRequest, patientModelToUpdate);

        patientRepository.save(patientModel);
    }

    public PatientModel ensurePatientByCPFExists(String cpf){
        return patientRepository.findByCpf(cpf).orElseThrow(() -> new PatientNotFoundException("Patient not found"));
    }

    public void ensurePatientByEmailOrCpfNonExists(String email, String cpf){

        patientRepository.findByCpf(cpf).ifPresent(pat -> {
            throw new PatientAlreadyRegisteredException("Patient already registered with this cpf: " + cpf);
        });

        patientRepository.findByEmail(email).ifPresent(pat -> {
            throw new PatientAlreadyRegisteredException("Patient already registered with this email: " + email);
        });
    }

    public void ensurePatientHasValidAge(LocalDate birth){
        if(birth.isAfter(LocalDate.now().minusYears(3))){
            throw new AgeException("The patient must be at least 3 years old.");
        }
    }

    public Page<PatientGetRequest> getPatientsFiltered(Optional<String> name, Optional<String> lastName, Optional<String> cpf, Optional<String> email, Pageable pageable) {

        Page<PatientModel> pageListOfPatientModel = patientRepository.findAllFiltered(name.orElse(null), lastName.orElse(null), cpf.orElse(null), email.orElse(null), pageable);

        List<PatientGetRequest> listOfPatient = pageListOfPatientModel.stream()
                .map(ModelMappers::patientModelToPatientGetRequest)
                .toList();

        return new PageImpl<>(listOfPatient);
    }

}
