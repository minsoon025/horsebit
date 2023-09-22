package com.a406.horsebit.dto;

import com.a406.horsebit.domain.RefreshToken;
import com.a406.horsebit.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserLogDto {
    private Long id;
    private String email;
    private String nickname;
    private String accessToken;
    private String refreshToken;
    //계좌번호 추가 필요

    public UserLogDto(User user, RefreshToken refreshToken) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken.getRefreshToken();
    }
}
