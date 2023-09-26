package com.a406.horsebit.google.dto.request;

import com.a406.horsebit.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String refreshToken;
    private String nickname;

    private String email;
    private String userName;

    @Builder
    public UserDTO(Long id, String refreshToken, String nickname, String email, String userName) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.nickname = nickname;
        this.email = email;
        this.userName = userName;
    }

    public static UserDTO from(User user){
        return UserDTO.builder()
                .id(user.getId())
                .refreshToken(user.getRefreshToken())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .userName(user.getUsername())
                .build();
    }
}
