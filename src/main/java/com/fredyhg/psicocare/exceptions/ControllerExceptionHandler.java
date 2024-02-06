package com.fredyhg.psicocare.exceptions;

import com.fredyhg.psicocare.exceptions.patient.PatientAlreadyRegisteredException;
import com.fredyhg.psicocare.exceptions.patient.PatientNotFoundException;
import com.fredyhg.psicocare.exceptions.psychologist.PsychologistAlreadyRegisteredException;
import com.fredyhg.psicocare.exceptions.psychologist.PsychologistNotFoundException;
import com.fredyhg.psicocare.exceptions.security.ExpiredTokenException;
import com.fredyhg.psicocare.exceptions.therapy.TherapyAlreadyExistsException;
import com.fredyhg.psicocare.exceptions.therapy.TherapyInvalidDatesException;
import com.fredyhg.psicocare.exceptions.therapy.TherapyNotFoundException;
import com.fredyhg.psicocare.exceptions.utils.AgeException;
import com.fredyhg.psicocare.exceptions.utils.ParseEmailInfosException;
import com.fredyhg.psicocare.exceptions.utils.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(PatientNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseMessage patientNotFound(Exception ex, WebRequest request){
        return this.createNewErrorMessage(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PsychologistNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseMessage psychologistNotFound(Exception ex, WebRequest request){
        return this.createNewErrorMessage(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TherapyNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseMessage therapyNotFound(Exception ex, WebRequest request){
        return this.createNewErrorMessage(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PatientAlreadyRegisteredException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseMessage patientAlreadyRegistered(Exception ex, WebRequest request){
        return this.createNewErrorMessage(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PsychologistAlreadyRegisteredException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseMessage psychologistAlreadyRegistered(Exception ex, WebRequest request){
        return this.createNewErrorMessage(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TherapyAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseMessage therapyAlreadyRegistered(Exception ex, WebRequest request){
        return this.createNewErrorMessage(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ParseEmailInfosException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseMessage errorToParseEmailInfos(Exception ex, WebRequest request){
        return this.createNewErrorMessage(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TherapyInvalidDatesException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseMessage therapyInvalidDate(Exception ex, WebRequest request){
        return this.createNewErrorMessage(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AgeException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseMessage invalidAge(Exception ex, WebRequest request){
        return this.createNewErrorMessage(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseMessage invalidArgument(Exception ex, WebRequest request){
        return this.createNewErrorMessage(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ExpiredTokenException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseMessage jwtExpired(Exception ex, WebRequest request){
        return this.createNewErrorMessage(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseMessage genericException(){
        return this.createGenericMessage(HttpStatus.INTERNAL_SERVER_ERROR);
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

    public ResponseMessage createGenericMessage(HttpStatus httpStatus) {
        return ResponseMessage
                .builder()
                .statusCode(httpStatus.value())
                .timestamp(new Date())
                .message("Internal Server Error")
                .description("Internal error encountered")
                .build();
    }
}
