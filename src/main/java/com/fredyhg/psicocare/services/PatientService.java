package com.fredyhg.psicocare.services;

import com.fredyhg.psicocare.models.PatientModel;
import com.fredyhg.psicocare.models.dtos.patient.PatientGetRequest;
import com.fredyhg.psicocare.models.dtos.patient.PatientPostRequest;
import com.fredyhg.psicocare.models.dtos.patient.PatientPutRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface PatientService {

    void createPatient(PatientPostRequest patientToBeCreate);

    Page<PatientGetRequest> getPatients(Pageable pageable);

    void editPatientInfos(PatientPutRequest patientPutRequest);

    void delete(String cpf);

    PatientModel ensurePatientByCPFExists(String cpf);

    void ensurePatientByEmailOrCpfNonExists(String email, String cpf);

    void ensurePatientHasValidAge(LocalDate birth);

    Page<PatientGetRequest> getPatientsFiltered(Optional<String> name,
                                                Optional<String> lastName,
                                                Optional<String> cpf,
                                                Optional<String> email,
                                                Pageable pageable);
}
