package com.fredyhg.psicocare.exceptions.utils;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class ResponseMessage {
    private int statusCode;
    private Date timestamp;
    private String message;
    private String description;
}