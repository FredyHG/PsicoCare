package com.fredyhg.psicocare.services;

import com.fredyhg.psicocare.exceptions.patient.PatientAlreadyRegisteredException;
import com.fredyhg.psicocare.exceptions.patient.PatientNotFoundException;
import com.fredyhg.psicocare.models.PatientModel;
import com.fredyhg.psicocare.models.dtos.patient.PatientGetRequest;
import com.fredyhg.psicocare.models.dtos.patient.PatientPutRequest;
import com.fredyhg.psicocare.repositories.PatientRepository;
import com.fredyhg.psicocare.services.impl.PatientServiceImpl;
import com.fredyhg.psicocare.utils.PatientCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {
    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientServiceImpl patientServiceImpl;



    @Test
    void testCreatePatient() {
        var patientPostRequest = PatientCreator.createValidPatientPostRequest();

        when(patientRepository.findByCpf(anyString())).thenReturn(Optional.empty());
        when(patientRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        patientServiceImpl.createPatient(patientPostRequest);

        verify(patientRepository, Mockito.times(1)).save(any());
    }

    @Test
    void testEnsurePatientByCPFExists() {
        String cpf = "123456789";
        var patientModel = PatientCreator.createValidPatient();

        when(patientRepository.findByCpf(cpf)).thenReturn(Optional.of(patientModel));

        PatientModel result = patientServiceImpl.ensurePatientByCPFExists(cpf);
        assertEquals(result, patientModel);
    }

    @Test
    void testEnsurePatientByEmailOrCpfNonExists() {
        String cpf = "123456789";
        String email = "john@example.com";

        when(patientRepository.findByCpf(cpf)).thenReturn(Optional.empty());
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> patientServiceImpl.ensurePatientByEmailOrCpfNonExists(email, cpf));
    }

    @Test
    void testEnsurePatientByEmailOrCpfNonExists_WithExistingCpf() {
        String cpf = "123456789";
        String email = "john@example.com";

        when(patientRepository.findByCpf(cpf)).thenReturn(Optional.of(new PatientModel()));

        assertThrows(PatientAlreadyRegisteredException.class, () -> patientServiceImpl.ensurePatientByEmailOrCpfNonExists(email, cpf));
    }

    @Test
    void testEnsurePatientByEmailOrCpfNonExists_WithExistingEmail() {
        String cpf = "123456789";
        String email = "john@example.com";

        when(patientRepository.findByCpf(cpf)).thenReturn(Optional.empty());
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(new PatientModel()));

        assertThrows(PatientAlreadyRegisteredException.class, () -> patientServiceImpl.ensurePatientByEmailOrCpfNonExists(email, cpf));
    }

    @Test
    void testGetPatients(){
        Pageable pageable = PageRequest.of(0, 10);
        List<PatientModel> patientModelList = PatientCreator.createValidListOfPatients();
        Page<PatientModel> patientModelPage = new PageImpl<>(patientModelList, pageable, patientModelList.size());

        when(patientRepository.findAll(pageable)).thenReturn(patientModelPage);

        Page<PatientGetRequest> result = patientServiceImpl.getPatients(pageable);

        assertNotNull(result);
        assertEquals(patientModelList.size(), result.getContent().size());

        verify(patientRepository).findAll(pageable);
    }

    @Test
    void testEditPatientInfos_SuccessfulUpdate() {
        when(patientRepository.findByCpf(anyString())).thenReturn(Optional.of(PatientCreator.createValidPatient()));

        patientServiceImpl.editPatientInfos(PatientCreator.createValidPatientPutRequest());

        verify(patientRepository).save(any(PatientModel.class));
    }

    @Test
    void testEditPatientInfos_PatientNotFound() {
        when(patientRepository.findByCpf(anyString())).thenReturn(Optional.empty());

        PatientPutRequest validPatientPutRequest = PatientCreator.createValidPatientPutRequest();

        assertThrows(PatientNotFoundException.class, () -> patientServiceImpl.editPatientInfos(validPatientPutRequest));
    }

    @Test
    void testEditPatientInfos_PartialUpdate() {
        ArgumentCaptor<PatientModel> patientModelCaptor = ArgumentCaptor.forClass(PatientModel.class);

        when(patientRepository.findByCpf(anyString())).thenReturn(Optional.of(PatientCreator.createValidPatient()));

        PatientPutRequest patientPutRequest = PatientCreator.createValidPatientPutRequest();
        patientPutRequest.setName(Optional.of("New Name"));

        patientServiceImpl.editPatientInfos(patientPutRequest);

        verify(patientRepository).save(patientModelCaptor.capture());
        assertEquals("New Name", patientModelCaptor.getValue().getName());}

    @Test
    void testEditPatientInfos_InvalidDate() {
        ArgumentCaptor<PatientModel> patientModelCaptor = ArgumentCaptor.forClass(PatientModel.class);

        when(patientRepository.findByCpf(anyString())).thenReturn(Optional.of(PatientCreator.createValidPatient()));

        PatientPutRequest patientPutRequest = PatientCreator.createValidPatientPutRequest();

        patientPutRequest.setBirthDate(Optional.of(LocalDate.now().minusYears(3)));

        patientServiceImpl.editPatientInfos(patientPutRequest);

        verify(patientRepository).save(patientModelCaptor.capture());
        assertNotEquals(LocalDate.now().minusYears(3), patientModelCaptor.getValue().getBirthDate());
    }

    @Test
    void testGetPatientsFiltered_AllParametersPresent() {
        Page<PatientModel> mockedPage = new PageImpl<>(Collections.singletonList(new PatientModel()));
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(patientRepository.findAllFiltered("John", "Doe", "12345678900", "john@example.com", pageRequest))
                .thenReturn(mockedPage);

        Page<PatientGetRequest> result = patientServiceImpl.getPatientsFiltered(
                Optional.of("John"), Optional.of("Doe"), Optional.of("12345678900"), Optional.of("john@example.com"), PageRequest.of(0, 10));

        assertNotNull(result);
        verify(patientRepository).findAllFiltered("John", "Doe", "12345678900", "john@example.com", PageRequest.of(0, 10));
    }

    @Test
    void testGetPatientsFiltered_SomeParametersAbsent() {
        Page<PatientModel> mockedPage = new PageImpl<>(Collections.singletonList(new PatientModel()));
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(patientRepository.findAllFiltered("John", null, "12345678900", null, pageRequest))
                .thenReturn(mockedPage);


        Page<PatientGetRequest> result = patientServiceImpl.getPatientsFiltered(
                Optional.of("John"), Optional.empty(), Optional.of("12345678900"), Optional.empty(), PageRequest.of(0, 10));

        assertNotNull(result);
        verify(patientRepository).findAllFiltered("John", null, "12345678900", null, PageRequest.of(0, 10));
    }

    @Test
    void testGetPatientsFiltered_AllParametersAbsent() {
        Page<PatientModel> mockedPage = new PageImpl<>(Collections.singletonList(new PatientModel()));
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(patientRepository.findAllFiltered(null, null, null, null, pageRequest))
                .thenReturn(mockedPage);

        Page<PatientGetRequest> result = patientServiceImpl.getPatientsFiltered(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), PageRequest.of(0, 10));

        assertNotNull(result);
        verify(patientRepository).findAllFiltered(null, null, null, null, PageRequest.of(0, 10));
    }

    @Test
    void testGetPatientsFiltered_PageableParameter() {
        Page<PatientModel> mockedPage = new PageImpl<>(Collections.singletonList(new PatientModel()));

        when(patientRepository.findAllFiltered(null, null, null, null, PageRequest.of(1, 5)))
                .thenReturn(mockedPage);

        Page<PatientGetRequest> result = patientServiceImpl.getPatientsFiltered(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), PageRequest.of(1, 5));

        assertNotNull(result);
        verify(patientRepository).findAllFiltered(null, null, null, null, PageRequest.of(1, 5));
    }


}