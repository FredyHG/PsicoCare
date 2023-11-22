package com.fredyhg.psicocare.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ResponseMessage {
    private int statusCode;
    private Date timestamp;
    private String message;
    private String description;
}