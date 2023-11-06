package org.example.user.register.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserRegisterResponse(
        String firstName,
        String lastName,
        String address,
        String city,
        String state,
        String country,
        String postcode,
        String phone,
        String dob,
        String email,
        String id,
        String createdAt
) {
}
