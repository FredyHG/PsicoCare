package com.fredyhg.psicocare.utils;

import com.fredyhg.psicocare.exceptions.ResponseMessage;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Utils {


    public static ResponseMessage createResponseMessage(String msg, int statusCode, String description){
        return ResponseMessage.builder()
                .message(msg)
                .statusCode(statusCode)
                .timestamp(new Date())
                .description(description)
                .build();
    }

}
