package com.fredyhg.psicocare.exceptions;

import com.fredyhg.psicocare.exceptions.patient.PatientAlreadyRegistered;
import com.fredyhg.psicocare.exceptions.patient.PatientNotFound;
import com.fredyhg.psicocare.exceptions.psychologist.PsychologistAlreadyRegistered;
import com.fredyhg.psicocare.exceptions.psychologist.PsychologistNotFound;
import com.fredyhg.psicocare.exceptions.therapy.TherapyAlreadyExists;
import com.fredyhg.psicocare.exceptions.therapy.TherapyInvalidDates;
import com.fredyhg.psicocare.exceptions.therapy.TherapyNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(PatientNotFound.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseMessage patientNotFound(Exception ex, WebRequest request){
        return createNewErrorMessage(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PsychologistNotFound.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseMessage psychologistNotFound(Exception ex, WebRequest request){
        return createNewErrorMessage(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TherapyNotFound.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseMessage therapyNotFound(Exception ex, WebRequest request){
        return createNewErrorMessage(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PatientAlreadyRegistered.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseMessage patientAlreadyRegistered(Exception ex, WebRequest request){
        return createNewErrorMessage(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PsychologistAlreadyRegistered.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseMessage psychologistAlreadyRegistered(Exception ex, WebRequest request){
        return createNewErrorMessage(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TherapyAlreadyExists.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseMessage therapyAlreadyRegistered(Exception ex, WebRequest request){
        return createNewErrorMessage(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ErrorToParseEmailInfos.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseMessage errorToParseEmailInfos(Exception ex, WebRequest request){
        return createNewErrorMessage(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TherapyInvalidDates.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseMessage therapyInvalidDate(Exception ex, WebRequest request){
        return createNewErrorMessage(ex, request, HttpStatus.CONFLICT);
    }

    public ResponseMessage createNewErrorMessage(Exception ex, WebRequest request, HttpStatus httpStatus) {
        return ResponseMessage
                .builder()
                .statusCode(httpStatus.value())
                .timestamp(new Date())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .build();
    }
}
