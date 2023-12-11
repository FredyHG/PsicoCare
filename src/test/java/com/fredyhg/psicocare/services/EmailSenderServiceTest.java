package com.fredyhg.psicocare.services;

import com.fredyhg.psicocare.models.PatientModel;
import com.fredyhg.psicocare.models.PsychologistModel;
import com.fredyhg.psicocare.models.TherapyModel;
import com.fredyhg.psicocare.utils.PatientCreator;
import com.fredyhg.psicocare.utils.PsychologistCreator;
import com.fredyhg.psicocare.utils.TherapyCreator;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailSenderServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailSenderService emailService;

    @Test
    void testSendEmail() {

        PatientModel patientModel = PatientCreator.createValidPatient();
        PsychologistModel psychologist = PsychologistCreator.createValidPsychologist();
        TherapyModel therapyModel = TherapyCreator.createValidTherapy();

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendEmail(patientModel, psychologist, therapyModel);

        verify(mailSender).send(mimeMessage);
    }
}