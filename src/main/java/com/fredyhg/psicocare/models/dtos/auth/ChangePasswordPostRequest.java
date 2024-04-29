package com.fredyhg.psicocare.models.dtos.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePasswordPostRequest {
    private String oldPassword;

    private String newPassword;
}
