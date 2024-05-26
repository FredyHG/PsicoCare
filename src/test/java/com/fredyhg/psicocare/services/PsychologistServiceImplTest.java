package com.fredyhg.psicocare.services;

import com.fredyhg.psicocare.exceptions.psychologist.PsychologistAlreadyRegisteredException;
import com.fredyhg.psicocare.exceptions.psychologist.PsychologistNotFoundException;
import com.fredyhg.psicocare.models.PsychologistModel;
import com.fredyhg.psicocare.models.dtos.psychologist.PsychologistGetRequest;
import com.fredyhg.psicocare.repositories.PsychologistRepository;
import com.fredyhg.psicocare.security.services.impl.UserServiceImpl;
import com.fredyhg.psicocare.services.impl.PsychologistServiceImpl;
import com.fredyhg.psicocare.utils.PsychologistCreator;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PsychologistServiceImplTest {

    @Mock
    private PsychologistRepository psychologistRepository;

    @Mock
    private UserServiceImpl userServiceImpl;

    @InjectMocks
    private PsychologistServiceImpl psychologistServiceImpl;


    @Test
    void testCreatePsychologist() {
        var psychologistPostRequest = PsychologistCreator.createValidPsychologistPostRequest();

        when(psychologistRepository.findByCrp(Mockito.anyString())).thenReturn(Optional.empty());

        psychologistServiceImpl.createPsychologist(psychologistPostRequest);

        verify(psychologistRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void testEnsurePsychologistByCrpExists() {

        String crp = "123456";

        when(psychologistRepository.findByCrp(crp)).thenReturn(Optional.empty());

        assertThrows(PsychologistNotFoundException.class, () -> psychologistServiceImpl.ensurePsychologistByCrpExists(crp));
    }

    @Test
    void testEnsurePsychologistNonExistsByCrp() {
        String crp = "123456";

        when(psychologistRepository.findByCrp(crp)).thenReturn(Optional.of(new PsychologistModel()));

        assertThrows(PsychologistAlreadyRegisteredException.class, () -> psychologistServiceImpl.ensurePsychologistNonExistsByCrp(crp));
    }

    @Test
    void testGetAllPsychologists(){
        Pageable pageable = PageRequest.of(0, 10);
        List<PsychologistModel> psychologistModelList = PsychologistCreator.createValidListOfPsychologist();
        Page<PsychologistModel> psychologistModelPage = new PageImpl<>(psychologistModelList, pageable, psychologistModelList.size());

        when(psychologistRepository.findAll(pageable)).thenReturn(psychologistModelPage);

        Page<PsychologistGetRequest> result = psychologistServiceImpl.getAllPsychologistsPageable(pageable);

        assertNotNull(result);
        assertEquals(psychologistModelList.size(), result.getContent().size());

        verify(psychologistRepository).findAll(pageable);
    }


}