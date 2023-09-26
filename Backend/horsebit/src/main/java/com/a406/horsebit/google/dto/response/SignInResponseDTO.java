package com.a406.horsebit.google.dto.response;

import com.a406.horsebit.google.dto.request.UserDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInResponseDTO {
    private String accessToken;
    private String refreshToken;
    private UserDTO userDTO;

    @Builder
    public SignInResponseDTO(String accessToken, String refreshToken, UserDTO userDTO) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userDTO = userDTO;
    }
}
