package com.fredyhg.psicocare.services;

import com.fredyhg.psicocare.exceptions.ErrorToParseEmailInfos;
import com.fredyhg.psicocare.models.PatientModel;
import com.fredyhg.psicocare.models.PsychologistModel;
import com.fredyhg.psicocare.models.TherapyModel;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender mailSender;

    public void sendEmail(PatientModel patientModel, PsychologistModel psychologist, TherapyModel therapyModel) {
        MimeMessage message = mailSender.createMimeMessage();

        String dateFormatted = extractDataFromLocalDateTime(therapyModel.getDate());
        String hourFormatted = extractHourFromLocalDateTime(therapyModel.getDate());

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(patientModel.getEmail());
            helper.setSubject("Appointment Confirmed");

            ClassPathResource resource = new ClassPathResource("static/email-template.html");
            String emailContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

            emailContent = emailContent.replace("{patientName}", patientModel.getName());
            emailContent = emailContent.replace("{psychologistName}", psychologist.getName());
            emailContent = emailContent.replace("{dateFormatted}", dateFormatted);
            emailContent = emailContent.replace("{hourFormatted}", hourFormatted);

            helper.setText(emailContent, true);
            helper.setFrom("PsicoCare <example@example.com>");
        } catch (IOException | MessagingException ex) {
            throw new ErrorToParseEmailInfos("Error to parse email infos");
        }

        mailSender.send(message);
    }

    public String extractDataFromLocalDateTime(LocalDateTime localDateTime){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return localDateTime.format(format);
    }

    public String extractHourFromLocalDateTime(LocalDateTime localDateTime){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");

        return localDateTime.format(format);
    }

}
