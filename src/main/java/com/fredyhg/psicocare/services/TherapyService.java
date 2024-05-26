package com.fredyhg.psicocare.services;

import com.fredyhg.psicocare.enums.StatusTherapy;
import com.fredyhg.psicocare.models.PatientModel;
import com.fredyhg.psicocare.models.PsychologistModel;
import com.fredyhg.psicocare.models.TherapyModel;
import com.fredyhg.psicocare.models.dtos.code.Code;
import com.fredyhg.psicocare.models.dtos.therapy.ReschedulePutRequest;
import com.fredyhg.psicocare.models.dtos.therapy.SchedulePutRequest;
import com.fredyhg.psicocare.models.dtos.therapy.TherapyGetRequest;
import com.fredyhg.psicocare.models.dtos.therapy.TherapyPostRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TherapyService {

    void createTherapy(TherapyPostRequest therapyPostRequest);

    void rescheduleTherapy(ReschedulePutRequest reschedulePutRequest);

    void cancelTherapy(SchedulePutRequest schedulePutRequest);

    void confirmTherapy(UUID id, Code code);

    Page<TherapyGetRequest> getAllTherapiesPending(Pageable pageable);

    Page<TherapyGetRequest> getAllTherapies(Pageable pageable);

    Page<TherapyGetRequest> getAllTherapiesWithStatus(Pageable pageable, String status);

    void getTodaySchedules();

    Page<TherapyGetRequest> getAllTherapiesFinished(Pageable pageable);

    TherapyModel changeStatusByTherapyModel(TherapyModel therapyModel, StatusTherapy statusTherapy);

    TherapyModel getTherapyByCpfAndCrp(String cpfPatient, String crpPsychologist, StatusTherapy statusTherapy);

    TherapyModel ensureTherapyExistsById(UUID id);

    List<TherapyGetRequest> convertToTherapyGetRequests(Page<TherapyModel> listOfTherapyModel);

    void ensureTherapyStatusIsValidForChange(TherapyModel therapyModel);

    TherapyModel ensureTherapyExistsByPatientAndPsychologist(PatientModel patient, PsychologistModel psychologist, StatusTherapy statusTherapy);

    void ensureTherapyNonExistsWithInvalidStatus(PatientModel patient, PsychologistModel psychologist);

    void ensureTherapyDatesIsValid(TherapyModel therapyModel);

    Page<TherapyGetRequest> getAllTherapiesFiltered(Optional<String> patientCPF,
                                                    Optional<String> psychologistCRP,
                                                    Optional<String> status,
                                                    LocalDateTime startDate,
                                                    LocalDateTime endDate,
                                                    Pageable pageable);

    StatusTherapy ensureStatusTherapyExistsFromString(String status);
}
