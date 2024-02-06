package com.fredyhg.psicocare.utils;

import com.fredyhg.psicocare.models.ConfirmationCodeModel;
import com.fredyhg.psicocare.models.dtos.code.Code;

public class ConfirmationCodeCreator {
    public static ConfirmationCodeModel createValidConfirmationCode(){
        return ConfirmationCodeModel.builder()
                .code("123456")
                .therapy(TherapyCreator.createValidTherapy())
                .used(false)
                .build();
    }

    public static Code createValidCode(){
        return Code.builder()
                .confirmationCode("123456")
                .build();
    }

    public static Code createInvalidCode(){
        return Code.builder()
                .confirmationCode("1213")
                .build();
    }
}
