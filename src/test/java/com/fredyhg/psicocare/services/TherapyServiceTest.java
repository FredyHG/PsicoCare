package com.fredyhg.psicocare.services;


import com.fredyhg.psicocare.exceptions.therapy.TherapyNotFound;
import com.fredyhg.psicocare.models.dtos.TherapyCreateRequest;
import com.fredyhg.psicocare.repositories.TherapyRepository;
import com.fredyhg.psicocare.utils.PatientCreator;
import com.fredyhg.psicocare.utils.PsychologistCreator;
import com.fredyhg.psicocare.utils.TherapyCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TherapyServiceTest {

    @Mock
    private TherapyRepository therapyRepository;

    @Mock
    private PatientService patientService;

    @Mock
    private PsychologistService psychologistService;

    @InjectMocks
    private TherapyService therapyService;

    @Test
    public void testCreateTherapy() {

        var therapyCreateRequest = TherapyCreator.createValidTherapyCreateRequest();

        var patientModel = PatientCreator.createValidPatient();
        when(patientService.ensurePatientByCPFExists(therapyCreateRequest.getCpfPatient())).thenReturn(patientModel);

        var psychologistModel = PsychologistCreator.createValidPsychologist();
        when(psychologistService.ensurePsychologistByCrpExists(therapyCreateRequest.getCrpPsychologist())).thenReturn(psychologistModel);

        when(therapyRepository.findByPatientAndPsychologist(patientModel, psychologistModel)).thenReturn(Optional.empty());

        therapyService.createTherapy(therapyCreateRequest);

        verify(therapyRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void testEnsureTherapyNonExistsWithStatusWaitDate() {
        var patientModel = PatientCreator.createValidPatient();
        var psychologistModel = PsychologistCreator.createValidPsychologist();

        var existingTherapy = TherapyCreator.createValidTherapy();

        when(therapyRepository.findByPatientAndPsychologist(patientModel, psychologistModel)).thenReturn(java.util.Optional.of(existingTherapy));

        assertThrows(TherapyNotFound.class, () -> therapyService.ensureTherapyNonExistsWithStatusWaitDate(patientModel, psychologistModel));
    }

    @Test
    public void testEnsureTherapyNonExistsWithStatusWaitDate_WithNoExistingTherapy() {

        var patientModel = PatientCreator.createValidPatient();
        var psychologistModel = PsychologistCreator.createValidPsychologist();

        when(therapyRepository.findByPatientAndPsychologist(patientModel, psychologistModel)).thenReturn(java.util.Optional.empty());

        therapyService.ensureTherapyNonExistsWithStatusWaitDate(patientModel, psychologistModel);

    }
}