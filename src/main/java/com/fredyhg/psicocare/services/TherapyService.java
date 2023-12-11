package com.fredyhg.psicocare.services;

import com.fredyhg.psicocare.enums.StatusTherapy;
import com.fredyhg.psicocare.exceptions.therapy.TherapyInvalidDates;
import com.fredyhg.psicocare.exceptions.therapy.TherapyNotFound;
import com.fredyhg.psicocare.models.PatientModel;
import com.fredyhg.psicocare.models.PsychologistModel;
import com.fredyhg.psicocare.models.TherapyModel;
import com.fredyhg.psicocare.models.dtos.TherapyCreateRequest;
import com.fredyhg.psicocare.repositories.TherapyRepository;
import com.fredyhg.psicocare.utils.ModelMappers;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TherapyService {

    private final TherapyRepository therapyRepository;

    private final PatientService patientService;

    private final PsychologistService psychologistService;

    private final EmailSenderService emailSenderService;

    @Transactional
    public void createTherapy(TherapyCreateRequest therapyCreateRequest){

        var patientModel = patientService.ensurePatientByCPFExists(therapyCreateRequest.getCpfPatient());

        var psychologistModel = psychologistService.ensurePsychologistByCrpExists(therapyCreateRequest.getCrpPsychologist());

        ensureTherapyNonExistsWithStatusWaitDate(patientModel, psychologistModel);

        var therapyToBeSaved = ModelMappers.therapyCreateRequestToTherapyModel(therapyCreateRequest, patientModel, psychologistModel);

        ensureTherapyDatesIsValid(therapyToBeSaved);

        TherapyModel therapyModel = therapyRepository.save(therapyToBeSaved);

        emailSenderService.sendEmail(patientModel, psychologistModel, therapyModel);
    }

    private TherapyModel ensureTherapyExists(PatientModel patient, PsychologistModel psychologist){
        return therapyRepository.findByPatientAndPsychologistAndStatusIs(patient, psychologist, StatusTherapy.WAIT_DATE).orElseThrow(
                () -> new TherapyNotFound("Therapy not found"));
    }

    public void ensureTherapyNonExistsWithStatusWaitDate(PatientModel patient, PsychologistModel psychologist){
        therapyRepository.findByPatientAndPsychologistAndStatusIs(patient, psychologist, StatusTherapy.WAIT_DATE).ifPresent(the -> {
            throw new TherapyNotFound("Cannot schedule another therapy with a pending one");
        });
    }

    public void ensureTherapyDatesIsValid(TherapyModel therapyModel){
        if(therapyModel.isValidDateForSchedule(therapyModel)){
            throw new TherapyInvalidDates("Schedule date time invalid");
        }
    }
}
