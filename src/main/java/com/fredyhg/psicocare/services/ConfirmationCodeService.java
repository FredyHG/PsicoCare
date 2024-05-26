package com.fredyhg.psicocare.services;

import com.fredyhg.psicocare.models.ConfirmationCodeModel;
import com.fredyhg.psicocare.models.TherapyModel;

public interface ConfirmationCodeService {
    ConfirmationCodeModel getByTherapy(TherapyModel therapyModel);

    void createConfirmationCodeModel(TherapyModel therapyModel);

    void toggleUse(ConfirmationCodeModel confirmationCodeModel);
}
