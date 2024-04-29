package com.fredyhg.psicocare.services;

import com.fredyhg.psicocare.enums.StatusTherapy;
import com.fredyhg.psicocare.exceptions.code.ConfirmationCodeNotFoundException;
import com.fredyhg.psicocare.exceptions.therapy.TherapyAlreadyExistsException;
import com.fredyhg.psicocare.exceptions.therapy.TherapyInvalidDatesException;
import com.fredyhg.psicocare.exceptions.therapy.TherapyInvalidStatusException;
import com.fredyhg.psicocare.exceptions.therapy.TherapyNotFoundException;
import com.fredyhg.psicocare.models.ConfirmationCodeModel;
import com.fredyhg.psicocare.models.PatientModel;
import com.fredyhg.psicocare.models.PsychologistModel;
import com.fredyhg.psicocare.models.TherapyModel;
import com.fredyhg.psicocare.models.dtos.code.Code;
import com.fredyhg.psicocare.models.dtos.therapy.ReschedulePutRequest;
import com.fredyhg.psicocare.models.dtos.therapy.SchedulePutRequest;
import com.fredyhg.psicocare.models.dtos.therapy.TherapyPostRequest;
import com.fredyhg.psicocare.models.dtos.therapy.TherapyGetRequest;
import com.fredyhg.psicocare.repositories.TherapyRepository;
import com.fredyhg.psicocare.utils.ModelMappers;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.spi.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TherapyService {

    private final TherapyRepository therapyRepository;

    private final PatientService patientService;

    private final PsychologistService psychologistService;

    private final EmailSenderService emailSenderService;

    private final ConfirmationCodeService confirmationCodeService;

    @Transactional
    public void createTherapy(TherapyPostRequest therapyPostRequest){

        var patientModel = patientService.ensurePatientByCPFExists(therapyPostRequest.getCpfPatient());

        var psychologistModel = psychologistService.ensurePsychologistByCrpExists(therapyPostRequest.getCrpPsychologist());

        this.ensureTherapyNonExistsWithInvalidStatus(patientModel, psychologistModel);

        var therapyToBeSaved = ModelMappers.therapyCreateRequestToTherapyModel(therapyPostRequest, patientModel, psychologistModel);

        this.ensureTherapyDatesIsValid(therapyToBeSaved);

        var therapyModel = therapyRepository.save(therapyToBeSaved);

        confirmationCodeService.createConfirmationCodeModel(therapyModel);

        emailSenderService.sendEmail(therapyModel);
    }

    @Transactional
    public void rescheduleTherapy(ReschedulePutRequest reschedulePutRequest) {

        var patient = patientService.ensurePatientByCPFExists(reschedulePutRequest.getPatientCPF());

        var psychologist = psychologistService.ensurePsychologistByCrpExists(reschedulePutRequest.getPsychologistCRP());

        var therapyModel = this.ensureTherapyExistsByPatientAndPsychologist(patient, psychologist, StatusTherapy.WAIT_DATE);

        therapyModel.setDateTime(reschedulePutRequest.getDateTime());

        therapyRepository.save(therapyModel);
    }

    @Transactional
    public void cancelTherapy(SchedulePutRequest schedulePutRequest){

        TherapyModel therapyModel = this.getTherapyByCpfAndCrp(schedulePutRequest.getPatientCPF(), schedulePutRequest.getPsychologistCRP(), StatusTherapy.WAIT_DATE);

        if(!therapyModel.isValidForCancel(therapyModel)){
            throw new TherapyInvalidStatusException("Invalid status for cancel");
        }

        this.changeStatusByTherapyModel(therapyModel, StatusTherapy.CANCELED);

        therapyRepository.save(therapyModel);
    }

    @Transactional
    public void confirmTherapy(UUID id, Code code){

        TherapyModel therapyModel = this.ensureTherapyExistsById(id);

        ConfirmationCodeModel codeModel = confirmationCodeService.getByTherapy(therapyModel);

        if(!Objects.equals(code.getConfirmationCode(), codeModel.getCode())){
            throw new ConfirmationCodeNotFoundException("Code invalid");
        }

        if(!therapyModel.isValidStatusForConfirm(therapyModel)){
            throw new TherapyInvalidStatusException("The status cannot be changed as the therapy already has a non-modifiable status.");
        }

        confirmationCodeService.toggleUse(codeModel);

        TherapyModel therapyModelToBeUpdate = this.changeStatusByTherapyModel(therapyModel, StatusTherapy.WAIT_DATE);

        therapyRepository.save(therapyModelToBeUpdate);
    }

    public Page<TherapyGetRequest> getAllTherapiesPending(Pageable pageable){

        Page<TherapyModel> therapyPageable = therapyRepository.findByStatus(StatusTherapy.WAIT_DATE, pageable);

        List<TherapyGetRequest> listOfTherapyGet = this.convertToTherapyGetRequests(therapyPageable);

        return new PageImpl<>(listOfTherapyGet);
    }

    public Page<TherapyGetRequest> getAllTherapies(Pageable pageable){

        return therapyRepository.findAll(pageable)
                .map(ModelMappers::therapyModelToTherapyGetRequest);
    }

    public Page<TherapyGetRequest> getAllTherapiesWithStatus(Pageable pageable, String status){

        StatusTherapy statusTherapy = StatusTherapy.valueOf(status);

        Page<TherapyModel> therapyPageable = therapyRepository.findByStatus(statusTherapy, pageable);

        List<TherapyGetRequest> listOfTherapyGet = this.convertToTherapyGetRequests(therapyPageable);

        return new PageImpl<>(listOfTherapyGet);
    }

    @Scheduled(cron = "0 0 6 * * ?")
    public void getTodaySchedules(){

        LocalDateTime maxDate = LocalDateTime.now().plusHours(16);


        psychologistService.getAllPsychologists().forEach(psy -> {
            List<TherapyModel> sessions = therapyRepository.findAllByPsychologistAndMinDateMaxDate(psy.getId(), LocalDateTime.now(), maxDate);

            if(!sessions.isEmpty()){
                emailSenderService.schedulesDates(psy, sessions);
            }
        });

    }

    public Page<TherapyGetRequest> getAllTherapiesFinished(Pageable pageable){

        Page<TherapyModel> therapyPageable = therapyRepository.findByStatus(StatusTherapy.FINISH, pageable);

        List<TherapyGetRequest> listOfTherapyGet = this.convertToTherapyGetRequests(therapyPageable);

        return new PageImpl<>(listOfTherapyGet);
    }

    private TherapyModel changeStatusByTherapyModel(TherapyModel therapyModel, StatusTherapy statusTherapy){

        this.ensureTherapyStatusIsValidForChange(therapyModel);

        therapyModel.setStatus(statusTherapy);

        return therapyModel;
    }

    private TherapyModel getTherapyByCpfAndCrp(String cpfPatient, String crpPsychologist, StatusTherapy statusTherapy){
        var patient = patientService.ensurePatientByCPFExists(cpfPatient);

        var psychologist = psychologistService.ensurePsychologistByCrpExists(crpPsychologist);

        return this.ensureTherapyExistsByPatientAndPsychologist(patient, psychologist, statusTherapy);
    }

    private TherapyModel ensureTherapyExistsById(UUID id){
        return therapyRepository.findById(id).orElseThrow(
                () -> new TherapyNotFoundException("Therapy not found")
        );
    }

    private List<TherapyGetRequest> convertToTherapyGetRequests(Page<TherapyModel> listOfTherapyModel){
        return listOfTherapyModel
                .stream()
                .map(ModelMappers::therapyModelToTherapyGetRequest)
                .toList();
    }

    private void ensureTherapyStatusIsValidForChange(TherapyModel therapyModel){
        if(therapyModel.isValidStatusForChange(therapyModel)){
            throw new TherapyInvalidStatusException("Therapy cannot be canceled as it has already been initiated or canceled.");
        }
    }

    private TherapyModel ensureTherapyExistsByPatientAndPsychologist(PatientModel patient, PsychologistModel psychologist, StatusTherapy statusTherapy){
        return therapyRepository.findByPatientAndPsychologistAndStatus(patient, psychologist, statusTherapy).orElseThrow(
                () -> new TherapyNotFoundException("Therapy not found by patient and psychologist"));
    }

    public void ensureTherapyNonExistsWithInvalidStatus(PatientModel patient, PsychologistModel psychologist){
        therapyRepository.findByPatientAndPsychologistAndStatus(patient, psychologist, StatusTherapy.WAIT_DATE).ifPresent(the -> {
            throw new TherapyAlreadyExistsException("Cannot schedule another therapy with a pending one");
        });

        therapyRepository.findByPatientAndPsychologistAndStatus(patient, psychologist, StatusTherapy.WAIT_CONFIRMATION).ifPresent(the -> {
            throw new TherapyAlreadyExistsException("Therapy session cannot be scheduled while one is awaiting confirmation");
        });
    }

    public void ensureTherapyDatesIsValid(TherapyModel therapyModel){
        if(therapyModel.isValidDateForSchedule(therapyModel)){
            throw new TherapyInvalidDatesException("Schedule date time invalid");
        }
    }

    public Page<TherapyGetRequest> getAllTherapiesFiltered(Optional<String> patientCPF,
                                                           Optional<String> psychologistCRP,
                                                           Optional<String> status,
                                                           LocalDateTime startDate,
                                                           LocalDateTime endDate,
                                                           Pageable pageable) {
        StatusTherapy statusTherapy = null;

        if(status.isPresent()){
            statusTherapy = ensureStatusTherapyExistsFromString(status.get());
        }

        return therapyRepository.findAllFiltered(patientCPF.orElse(null),
                psychologistCRP.orElse(null), startDate, endDate, statusTherapy, pageable)
                .map(ModelMappers::therapyModelToTherapyGetRequest);
    }

    public StatusTherapy ensureStatusTherapyExistsFromString(String status){

        if(status == null){
            return null;
        }

        try {
            return StatusTherapy.valueOf(status);
        } catch (IllegalArgumentException ex){
            throw new TherapyInvalidStatusException("invalid status");
        }
    }
}
