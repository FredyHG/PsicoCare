package com.fredyhg.psicocare.services;

import com.fredyhg.psicocare.exceptions.ConfirmationCodeNotFoundException;
import com.fredyhg.psicocare.exceptions.therapy.TherapyNotFoundException;
import com.fredyhg.psicocare.models.ConfirmationCodeModel;
import com.fredyhg.psicocare.models.TherapyModel;
import com.fredyhg.psicocare.repositories.ConfirmationCodeRepository;
import com.fredyhg.psicocare.utils.ConfirmationCodeCreator;
import com.fredyhg.psicocare.utils.TherapyCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfirmationCodeServiceTest {

    @Mock
    private ConfirmationCodeRepository confirmationCodeRepository;

    @InjectMocks
    private ConfirmationCodeService confirmationCodeService;

    @Captor
    private ArgumentCaptor<ConfirmationCodeModel> confirmationCodeCaptor;

    @Test
    void getByTherapyShouldReturnConfirmationCodeWhenFound() {

        TherapyModel therapyModel = TherapyCreator.createValidTherapy();
        ConfirmationCodeModel expectedConfirmationCode = ConfirmationCodeCreator.createValidConfirmationCode();

        when(confirmationCodeRepository.findByTherapy(therapyModel))
                .thenReturn(Optional.of(expectedConfirmationCode));

        ConfirmationCodeModel result = confirmationCodeService.getByTherapy(therapyModel);

        assertEquals(expectedConfirmationCode, result);
    }

    @Test
    void getByTherapyShouldThrowExceptionWhenNotFound() {

        TherapyModel therapyModel = TherapyCreator.createValidTherapy();
        when(confirmationCodeRepository.findByTherapy(therapyModel)).thenReturn(Optional.empty());

        assertThrows(ConfirmationCodeNotFoundException.class,
                () -> confirmationCodeService.getByTherapy(therapyModel));
    }

    @Test
    void createConfirmationCodeModelShouldCreateAndSaveConfirmationCode() {
        TherapyModel therapyModel = TherapyCreator.createValidTherapy();

        confirmationCodeService.createConfirmationCodeModel(therapyModel);

        verify(confirmationCodeRepository).save(confirmationCodeCaptor.capture());
        ConfirmationCodeModel capturedConfirmationCode = confirmationCodeCaptor.getValue();

        assertFalse(capturedConfirmationCode.isUsed());
        assertEquals(therapyModel, capturedConfirmationCode.getTherapy());
        assertNotNull(capturedConfirmationCode.getCode());
    }

    @Test
    void toggleUseShouldSetUsedToTrueAndSaveWhenCodeIsFound() {
        ConfirmationCodeModel confirmationCodeModel = ConfirmationCodeCreator.createValidConfirmationCode();
        confirmationCodeModel.setId(UUID.randomUUID());

        ConfirmationCodeModel foundConfirmationCode = ConfirmationCodeCreator.createValidConfirmationCode();
        foundConfirmationCode.setUsed(false);
        when(confirmationCodeRepository.findById(confirmationCodeModel.getId())).thenReturn(Optional.of(foundConfirmationCode));

        confirmationCodeService.toggleUse(confirmationCodeModel);

        assertTrue(foundConfirmationCode.isUsed());
        verify(confirmationCodeRepository).save(foundConfirmationCode);
    }

    @Test
    void toggleUseShouldThrowExceptionWhenCodeIsNotFound() {
        ConfirmationCodeModel confirmationCodeModel = ConfirmationCodeCreator.createValidConfirmationCode();
        confirmationCodeModel.setId(UUID.randomUUID());
        when(confirmationCodeRepository.findById(confirmationCodeModel.getId())).thenReturn(Optional.empty());

        assertThrows(ConfirmationCodeNotFoundException.class,
                () -> confirmationCodeService.toggleUse(confirmationCodeModel));
    }
}