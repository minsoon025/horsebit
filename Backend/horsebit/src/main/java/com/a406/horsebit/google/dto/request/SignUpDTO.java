package com.a406.horsebit.google.dto.request;

import com.a406.horsebit.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignUpDTO {
    private String token; // Authorization Server(google, naver, kakao) 에서 발급받은 token
    private String userName;
//    //TODO: 계좌 정보 추가
//    private String bankAccount;

    public User toEntity(){
        return User.builder()
                .userName(userName)
                .build();
//                .bankAccount(bankAccount)
    }
}