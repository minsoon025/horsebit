package com.a406.horsebit.google.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpResponseDTO {
    private String accessToken;

    public SignUpResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }
}
