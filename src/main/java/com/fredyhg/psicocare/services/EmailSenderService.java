package com.fredyhg.psicocare.services;

import com.fredyhg.psicocare.models.PsychologistModel;
import com.fredyhg.psicocare.models.TherapyModel;

import java.time.LocalDateTime;
import java.util.List;

public interface EmailSenderService {

    void sendEmail(TherapyModel therapyModel);

    void sendAccessDetailsEmail(PsychologistModel psychologistModel, String password);

    void schedulesDates(PsychologistModel psychologistModel, List<TherapyModel> sessions);

    String extractDataFromLocalDateTime(LocalDateTime localDateTime);

    String extractHourFromLocalDateTime(LocalDateTime localDateTime);
}
