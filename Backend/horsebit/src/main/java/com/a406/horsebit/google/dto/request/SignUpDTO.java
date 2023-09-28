package com.a406.horsebit.google.dto.request;

import com.a406.horsebit.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignUpDTO {
//    private String providerName;    //이건 구글 로그인이라 google
    private String token; // Authorization Server(google, naver, kakao) 에서 발급받은 token
    private String nickname;
    private String email;
    private String userName;

    public User toEntity(){
        return User.builder()
//                .providerName(providerName)
                .email(email)
                .nickname(nickname)
                .userName(userName)
                .build();
    }
}
