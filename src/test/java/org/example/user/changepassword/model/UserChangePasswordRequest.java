package org.example.user.changepassword.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserChangePasswordRequest(
        String currentPassword,
        String newPassword,
        String newPasswordConfirmation
) {
}
