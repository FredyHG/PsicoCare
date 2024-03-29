package com.fredyhg.psicocare.services;

import com.fredyhg.psicocare.exceptions.ConfirmationCodeNotFoundException;
import com.fredyhg.psicocare.models.ConfirmationCodeModel;
import com.fredyhg.psicocare.models.TherapyModel;
import com.fredyhg.psicocare.repositories.ConfirmationCodeRepository;
import com.fredyhg.psicocare.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfirmationCodeService {

    private final ConfirmationCodeRepository confirmationCodeRepository;

    public ConfirmationCodeModel getByTherapy(TherapyModel therapyModel){
        return confirmationCodeRepository.findByTherapy(therapyModel).orElseThrow(
                () -> new ConfirmationCodeNotFoundException("Confirmation code not found")
        );
    }

    public void createConfirmationCodeModel(TherapyModel therapyModel){
        ConfirmationCodeModel confirmationCode = ConfirmationCodeModel.builder()
                .used(false)
                .therapy(therapyModel)
                .code(String.valueOf(Utils.genConfirmCode()))
                .build();

        confirmationCodeRepository.save(confirmationCode);
    }

    public void toggleUse(ConfirmationCodeModel confirmationCodeModel){

        ConfirmationCodeModel confirmationCode = confirmationCodeRepository.findById(confirmationCodeModel.getId()).orElseThrow(
                () -> new ConfirmationCodeNotFoundException("Confirmation code not found"));

        confirmationCode.setUsed(true);

        confirmationCodeRepository.save(confirmationCode);
    }
}
