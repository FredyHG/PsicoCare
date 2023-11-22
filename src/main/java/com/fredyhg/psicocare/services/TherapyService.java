package com.fredyhg.psicocare.services;

import com.fredyhg.psicocare.enums.StatusTherapy;
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

    @Transactional
    public void createTherapy(TherapyCreateRequest therapyCreateRequest){

        var patientModel = patientService.ensurePatientByCPFExists(therapyCreateRequest.getCpfPatient());

        var psychologistModel = psychologistService.ensurePsychologistByCrpExists(therapyCreateRequest.getCrpPsychologist());

        ensureTherapyNonExistsWithStatusWaitDate(patientModel, psychologistModel);

        var therapyToBeSaved = ModelMappers.therapyCreateRequestToTherapyModel(therapyCreateRequest, patientModel, psychologistModel);

        therapyRepository.save(therapyToBeSaved);
    }

    private TherapyModel ensureTherapyExists(PatientModel patient, PsychologistModel psychologist){
        return therapyRepository.findByPatientAndPsychologist(patient, psychologist).orElseThrow(
                () -> new TherapyNotFound("Therapy not found"));
    }

    public void ensureTherapyNonExistsWithStatusWaitDate(PatientModel patient, PsychologistModel psychologist){
        therapyRepository.findByPatientAndPsychologist(patient, psychologist).ifPresent(the -> {
            if(the.getStatus() == StatusTherapy.WAIT_DATE){
                throw new TherapyNotFound("Cannot schedule another therapy with a pending one");
            }
        });
    }
}
