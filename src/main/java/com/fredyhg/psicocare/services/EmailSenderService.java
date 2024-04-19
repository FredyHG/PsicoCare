package com.fredyhg.psicocare.services;

import com.fredyhg.psicocare.exceptions.utils.ParseEmailInfosException;
import com.fredyhg.psicocare.models.PsychologistModel;
import com.fredyhg.psicocare.models.TherapyModel;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from-username}")
    private String emailFrom;

    public void sendEmail(TherapyModel therapyModel) {
        MimeMessage message = mailSender.createMimeMessage();

        String dateFormatted = extractDataFromLocalDateTime(therapyModel.getDateTime());
        String hourFormatted = extractHourFromLocalDateTime(therapyModel.getDateTime());

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(therapyModel.getPatient().getEmail());
            helper.setSubject("Appointment Confirmed");

            ClassPathResource resource = new ClassPathResource("static/email-template.html");
            String emailContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

            emailContent = emailContent.replace("{patientName}", therapyModel.getPatient().getName());
            emailContent = emailContent.replace("{psychologistName}", therapyModel.getPsychologist().getName());
            emailContent = emailContent.replace("{dateFormatted}", dateFormatted);
            emailContent = emailContent.replace("{hourFormatted}", hourFormatted);

            helper.setText(emailContent, true);
            helper.setFrom("PsicoCare <" + emailFrom + ">");
        } catch (IOException | MessagingException ex) {
            throw new ParseEmailInfosException("Error to parse email infos");
        }

        mailSender.send(message);
    }

    public void sendAccessDetailsEmail(PsychologistModel psychologistModel, String password) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(psychologistModel.getEmail());
            helper.setSubject("Access Details");

            ClassPathResource resource = new ClassPathResource("static/access-details-template.html");
            String emailContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

            emailContent = emailContent.replace("{psychologistName}", psychologistModel.getName());
            emailContent = emailContent.replace("{username}", psychologistModel.getEmail());
            emailContent = emailContent.replace("{password}", password);

            helper.setText(emailContent, true);
            helper.setFrom("PsicoCare <" + emailFrom + ">");
        } catch (IOException | MessagingException ex) {
            throw new ParseEmailInfosException("Error to parse email infos");
        }

        mailSender.send(message);
    }

    public void schedulesDates(PsychologistModel psychologistModel, List<TherapyModel> sessions){

        StringBuilder therapySessionsHtml = new StringBuilder();

        for(TherapyModel session : sessions){
            therapySessionsHtml.append("<tr>")
                    .append("<td>").append(extractHourFromLocalDateTime(session.getDateTime())).append("</td>")
                    .append("<td>").append(session.getPatient().getName()).append("</td>")
                    .append("</tr>");
        }


        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(psychologistModel.getEmail());
            helper.setSubject("Today Schedules");

            ClassPathResource resource = new ClassPathResource("static/schedules-template.html");
            String emailContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

            emailContent = emailContent.replace("${therapySessions}", therapySessionsHtml.toString());

            helper.setText(emailContent, true);
            helper.setFrom("PsicoCare <" + emailFrom + ">");
        } catch (IOException | MessagingException ex) {
            throw new ParseEmailInfosException("Error to parse email infos");
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
