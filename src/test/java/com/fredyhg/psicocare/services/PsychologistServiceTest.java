package com.fredyhg.psicocare.services;

import com.fredyhg.psicocare.exceptions.psychologist.PsychologistAlreadyRegistered;
import com.fredyhg.psicocare.exceptions.psychologist.PsychologistNotFound;
import com.fredyhg.psicocare.models.PsychologistModel;
import com.fredyhg.psicocare.repositories.PsychologistRepository;
import com.fredyhg.psicocare.utils.PsychologistCreator;
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
class PsychologistServiceTest {

    @Mock
    private PsychologistRepository psychologistRepository;

    @InjectMocks
    private PsychologistService psychologistService;

    @Test
    void testCreatePsychologist() {
        var psychologistPostRequest = PsychologistCreator.createValidPsychologistPostRequest();

        when(psychologistRepository.findByCrp(Mockito.anyString())).thenReturn(Optional.empty());

        psychologistService.createPsychologist(psychologistPostRequest);

        verify(psychologistRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void ensurePsychologistByCrpExists() {

        String crp = "123456";

        when(psychologistRepository.findByCrp(crp)).thenReturn(Optional.empty());

        assertThrows(PsychologistNotFound.class, () -> psychologistService.ensurePsychologistByCrpExists(crp));
    }

    @Test
    void ensurePsychologistNonExistsByCrp() {
        String crp = "123456";

        when(psychologistRepository.findByCrp(crp)).thenReturn(Optional.of(new PsychologistModel()));

        assertThrows(PsychologistAlreadyRegistered.class, () -> psychologistService.ensurePsychologistNonExistsByCrp(crp));
    }
}