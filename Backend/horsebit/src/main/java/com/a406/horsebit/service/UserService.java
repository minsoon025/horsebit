package com.a406.horsebit.service;

import com.a406.horsebit.domain.User;
import com.a406.horsebit.dto.UserSettingDTO;
import com.a406.horsebit.dto.request.AddUserRequest;
import com.a406.horsebit.google.dto.request.RefreshDTO;
import com.a406.horsebit.google.dto.request.SignInDTO;
import com.a406.horsebit.google.dto.request.SignUpDTO;
import com.a406.horsebit.google.dto.response.RefreshResponseDTO;
import com.a406.horsebit.google.dto.response.SignInResponseDTO;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface UserService {
    public User findById(Long userId);

    public User findByEmail(String email);

    //로그인
    SignInResponseDTO signIn(SignInDTO signInDto) throws ParseException, JOSEException;

    //회원가입
    User signUp(SignUpDTO signUpDto) throws ParseException, JOSEException;

    //토큰 정보 전달
    RefreshResponseDTO issueAccessTokenByRefreshToken(RefreshDTO refreshDto);

    //사용자이름(어플에서 사용하는 이름) 중복확인
    boolean isDuplicatedUserName(String userName);

    /**
     * 개인 설정 조회
     */
    UserSettingDTO findSettingsByUserNo(Long userNo);
    void updateSetting(Long userNo, UserSettingDTO userSetting);
}
