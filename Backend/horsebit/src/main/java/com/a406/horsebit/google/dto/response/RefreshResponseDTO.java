package com.a406.horsebit.google.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshResponseDTO {
    private String accessToken;
    private String refreshToken;

    @Builder
    public RefreshResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
