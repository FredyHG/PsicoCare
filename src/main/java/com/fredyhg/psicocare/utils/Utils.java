package com.fredyhg.psicocare.utils;

import com.fredyhg.psicocare.exceptions.utils.ResponseMessage;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;

@Component
public class Utils {

    private Utils() {
    }

    public static ResponseMessage createResponseMessage(int statusCode, String description){
        return ResponseMessage.builder()
                .message("Success")
                .statusCode(statusCode)
                .timestamp(new Date())
                .description(description)
                .build();
    }

    public static String genConfirmCode(){
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1000000));
    }
}
