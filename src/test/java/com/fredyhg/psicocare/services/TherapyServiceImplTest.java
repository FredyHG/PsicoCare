package com.fredyhg.psicocare.services;


import com.fredyhg.psicocare.enums.StatusTherapy;
import com.fredyhg.psicocare.exceptions.code.ConfirmationCodeNotFoundException;
import com.fredyhg.psicocare.exceptions.therapy.TherapyAlreadyExistsException;
import com.fredyhg.psicocare.exceptions.therapy.TherapyInvalidDatesException;
import com.fredyhg.psicocare.exceptions.therapy.TherapyInvalidStatusException;
import com.fredyhg.psicocare.models.ConfirmationCodeModel;
import com.fredyhg.psicocare.models.PatientModel;
import com.fredyhg.psicocare.models.PsychologistModel;
import com.fredyhg.psicocare.models.TherapyModel;
import com.fredyhg.psicocare.models.dtos.code.Code;
import com.fredyhg.psicocare.models.dtos.therapy.SchedulePutRequest;
import com.fredyhg.psicocare.models.dtos.therapy.TherapyGetRequest;
import com.fredyhg.psicocare.repositories.ConfirmationCodeRepository;
import com.fredyhg.psicocare.repositories.TherapyRepository;
import com.fredyhg.psicocare.services.impl.*;
import com.fredyhg.psicocare.utils.ConfirmationCodeCreator;
import com.fredyhg.psicocare.utils.PatientCreator;
import com.fredyhg.psicocare.utils.PsychologistCreator;
import com.fredyhg.psicocare.utils.TherapyCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TherapyServiceImplTest {

    @Mock
    private TherapyRepository therapyRepository;

    @Mock
    private PatientService patientService;

    @Mock
    private PsychologistService psychologistService;

    @Mock
    private EmailSenderService emailSenderService;

    @Mock
    private ConfirmationCodeRepository confirmationCodeRepository;

    @Mock
    private ConfirmationCodeServiceImpl confirmationCodeServiceImpl;

    @InjectMocks
    private TherapyService therapyService;


    @Test
    void testCreateTherapy() {

        var therapyCreateRequest = TherapyCreator.createValidTherapyCreateRequest();

        var patientModel = PatientCreator.createValidPatient();
        when(patientService.ensurePatientByCPFExists(therapyCreateRequest.getCpfPatient())).thenReturn(patientModel);

        var psychologistModel = PsychologistCreator.createValidPsychologist();
        when(psychologistService.ensurePsychologistByCrpExists(therapyCreateRequest.getCrpPsychologist())).thenReturn(psychologistModel);

        when(therapyRepository.findByPatientAndPsychologistAndStatus(patientModel, psychologistModel, StatusTherapy.WAIT_DATE)).thenReturn(Optional.empty());

        therapyService.createTherapy(therapyCreateRequest);

        verify(therapyRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void testEnsureTherapyNonExistsWithStatusWaitDate() {
        var patientModel = PatientCreator.createValidPatient();
        var psychologistModel = PsychologistCreator.createValidPsychologist();

        var existingTherapy = TherapyCreator.createValidTherapy();

        when(therapyRepository.findByPatientAndPsychologistAndStatus(patientModel, psychologistModel, StatusTherapy.WAIT_DATE)).thenReturn(java.util.Optional.of(existingTherapy));

        assertThrows(TherapyAlreadyExistsException.class, () -> therapyService.ensureTherapyNonExistsWithInvalidStatus(patientModel, psychologistModel));
    }

    @Test
    void testEnsureTherapyNonExistsWithStatusWaitDate_WithNoExistingTherapy() {

        var patientModel = PatientCreator.createValidPatient();
        var psychologistModel = PsychologistCreator.createValidPsychologist();

        when(therapyRepository.findByPatientAndPsychologistAndStatus(patientModel, psychologistModel, StatusTherapy.WAIT_DATE)).thenReturn(java.util.Optional.empty());

        assertDoesNotThrow(() -> therapyService.ensureTherapyNonExistsWithInvalidStatus(patientModel, psychologistModel));
    }

    @Test
    void testEnsureTherapyDatesIsValidWithValidDates(){
        TherapyModel mockTherapyModel = mock(TherapyModel.class);
        when(mockTherapyModel.isValidDateForSchedule(mockTherapyModel)).thenReturn(false);

        assertDoesNotThrow(() -> therapyService.ensureTherapyDatesIsValid(mockTherapyModel));
    }

    @Test( )
    void testEnsureTherapyDatesIsValidWithInvalidDates(){
        TherapyModel mockTherapyModel = mock(TherapyModel.class);
        when(mockTherapyModel.isValidDateForSchedule(mockTherapyModel)).thenReturn(true);

        assertThrows(TherapyInvalidDatesException.class, () -> therapyService.ensureTherapyDatesIsValid(mockTherapyModel));
    }

    @Test
    void testEnsureNoExceptionForNonExistingTherapy() {
        when(therapyRepository.findByPatientAndPsychologistAndStatus(any(), any(), any())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> therapyService.ensureTherapyNonExistsWithInvalidStatus(
                PatientCreator.createValidPatient(),
                PsychologistCreator.createValidPsychologist()));
    }

    @Test
    void testEnsureExceptionForExistingTherapyWithWaitDate() {
        when(therapyRepository.findByPatientAndPsychologistAndStatus(any(), any(), eq(StatusTherapy.WAIT_DATE)))
                .thenReturn(Optional.of(TherapyCreator.createValidTherapy()));

        PatientModel validPatient = PatientCreator.createValidPatient();
        PsychologistModel validPsychologist = PsychologistCreator.createValidPsychologist();

        assertThrows(TherapyAlreadyExistsException.class,
                () -> therapyService.ensureTherapyNonExistsWithInvalidStatus(validPatient, validPsychologist),
                "Cannot schedule another therapy with a pending one");
    }

    @Test
    void testEnsureExceptionForExistingTherapyWithWaitConfirmation() {
        when(therapyRepository.findByPatientAndPsychologistAndStatus(any(PatientModel.class), any(PsychologistModel.class), any(StatusTherapy.class)))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(() -> therapyService.ensureTherapyNonExistsWithInvalidStatus(new PatientModel(), new PsychologistModel()));
    }

    @Test
    void testGetAllTherapiesFinished() {
        Pageable pageable = PageRequest.of(0, 10);
        List<TherapyModel> therapyModels = Arrays.asList(TherapyCreator.createValidTherapy(), TherapyCreator.createValidTherapy());
        Page<TherapyModel> mockPage = new PageImpl<>(therapyModels);

        when(therapyRepository.findByStatus(StatusTherapy.FINISH, pageable)).thenReturn(mockPage);

        Page<TherapyGetRequest> result = therapyService.getAllTherapiesFinished(pageable);

        assertNotNull(result);
        assertEquals(therapyModels.size(), result.getContent().size());
    }

    @Test
    void testGetAllTherapiesPending() {
        Pageable pageable = PageRequest.of(0, 10);
        List<TherapyModel> therapyModels = Arrays.asList(TherapyCreator.createValidTherapy(), TherapyCreator.createValidTherapy());
        Page<TherapyModel> mockPage = new PageImpl<>(therapyModels);

        when(therapyRepository.findByStatus(StatusTherapy.WAIT_DATE, pageable)).thenReturn(mockPage);

        Page<TherapyGetRequest> result = therapyService.getAllTherapiesPending(pageable);

        assertNotNull(result);
        assertEquals(therapyModels.size(), result.getContent().size());
    }

    @Test
    void testConfirmTherapySuccess() {
        UUID id = UUID.randomUUID();
        Code code = ConfirmationCodeCreator.createValidCode();

        TherapyModel therapyModel = TherapyCreator.createValidTherapyWithStatusWaitConfirmation();
        ConfirmationCodeModel codeModel = ConfirmationCodeCreator.createValidConfirmationCode();

        when(therapyRepository.findById(id)).thenReturn(Optional.of(therapyModel));
        when(confirmationCodeServiceImpl.getByTherapy(therapyModel)).thenReturn(codeModel);

        therapyService.confirmTherapy(id, code);

        verify(confirmationCodeServiceImpl).toggleUse(codeModel);
        verify(therapyRepository).save(any(TherapyModel.class));
    }

    @Test
    void testConfirmTherapyInvalidCode() {
        UUID id = UUID.randomUUID();
        Code invalidCode = ConfirmationCodeCreator.createInvalidCode();

        TherapyModel therapyModel = TherapyCreator.createValidTherapyWithStatusWaitConfirmation();
        ConfirmationCodeModel codeModel = ConfirmationCodeCreator.createValidConfirmationCode();

        when(therapyRepository.findById(id)).thenReturn(Optional.of(therapyModel));
        when(confirmationCodeServiceImpl.getByTherapy(therapyModel)).thenReturn(codeModel);

        assertThrows(ConfirmationCodeNotFoundException.class, () -> therapyService.confirmTherapy(id, invalidCode));
    }

    @Test
    void testConfirmTherapyInvalidStatus() {

        UUID id = UUID.randomUUID();
        Code code = ConfirmationCodeCreator.createValidCode();

        TherapyModel therapyModel = TherapyCreator.createValidTherapy();
        ConfirmationCodeModel codeModel = ConfirmationCodeCreator.createValidConfirmationCode();

        when(therapyRepository.findById(id)).thenReturn(Optional.of(therapyModel));
        when(confirmationCodeServiceImpl.getByTherapy(therapyModel)).thenReturn(codeModel);

        assertThrows(TherapyInvalidStatusException.class, () -> therapyService.confirmTherapy(id, code));
    }

    @Test
    void testCancelTherapy() {

        SchedulePutRequest schedulePutRequest = TherapyCreator.createValidSchedulePutRequest();
        PatientModel validPatient = PatientCreator.createValidPatient();
        PsychologistModel validPsychologist = PsychologistCreator.createValidPsychologist();
        TherapyModel therapyModel = TherapyCreator.createValidTherapy();

        when(patientService.ensurePatientByCPFExists(anyString())).thenReturn(validPatient);
        when(psychologistService.ensurePsychologistByCrpExists(anyString())).thenReturn(validPsychologist);
        when(therapyRepository.findByPatientAndPsychologistAndStatus(validPatient, validPsychologist, StatusTherapy.WAIT_DATE)).thenReturn(Optional.of(therapyModel));

        therapyService.cancelTherapy(schedulePutRequest);

        assertEquals(StatusTherapy.CANCELED, therapyModel.getStatus());
        verify(therapyRepository).save(therapyModel);
    }

    @Test
    void testCancelTherapy_InvalidStatus() {
        SchedulePutRequest schedulePutRequest = TherapyCreator.createValidSchedulePutRequest();

        when(therapyRepository.findByPatientAndPsychologistAndStatus(any(), any(), any())).thenReturn(Optional.of(TherapyCreator.createInvalidTherapy()));

        assertThrows(TherapyInvalidStatusException.class, () -> {
            therapyService.cancelTherapy(schedulePutRequest);
        });
    }
}