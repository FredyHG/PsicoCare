package com.fredyhg.psicocare.services;

import com.fredyhg.psicocare.exceptions.patient.PatientAlreadyRegistered;
import com.fredyhg.psicocare.models.PatientModel;
import com.fredyhg.psicocare.repositories.PatientRepository;
import com.fredyhg.psicocare.utils.PatientCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {
    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    @Test
    public void testCreatePatient() {
        var patientPostRequest = PatientCreator.createValidPatientPostRequest();

        when(patientRepository.findByCpf(Mockito.anyString())).thenReturn(Optional.empty());
        when(patientRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        patientService.createPatient(patientPostRequest);

        verify(patientRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void testEnsurePatientByCPFExists() {
        String cpf = "123456789";
        var patientModel = PatientCreator.createValidPatient();

        when(patientRepository.findByCpf(cpf)).thenReturn(Optional.of(patientModel));

        PatientModel result = patientService.ensurePatientByCPFExists(cpf);
        assert(result.equals(patientModel));
    }

    @Test
    public void testEnsurePatientByEmailOrCpfNonExists() {
        String cpf = "123456789";
        String email = "john@example.com";

        when(patientRepository.findByCpf(cpf)).thenReturn(Optional.empty());
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());

        patientService.ensurePatientByEmailOrCpfNonExists(email, cpf);
    }

    @Test
    public void testEnsurePatientByEmailOrCpfNonExists_WithExistingCpf() {
        String cpf = "123456789";
        String email = "john@example.com";

        when(patientRepository.findByCpf(cpf)).thenReturn(Optional.of(new PatientModel()));

        assertThrows(PatientAlreadyRegistered.class, () -> patientService.ensurePatientByEmailOrCpfNonExists(email, cpf));
    }

    @Test
    public void testEnsurePatientByEmailOrCpfNonExists_WithExistingEmail() {
        String cpf = "123456789";
        String email = "john@example.com";

        when(patientRepository.findByCpf(cpf)).thenReturn(Optional.empty());
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(new PatientModel()));

        assertThrows(PatientAlreadyRegistered.class, () -> patientService.ensurePatientByEmailOrCpfNonExists(email, cpf));
    }
}