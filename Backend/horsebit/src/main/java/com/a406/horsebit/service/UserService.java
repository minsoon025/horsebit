package com.a406.horsebit.service;

import com.a406.horsebit.domain.User;
import com.a406.horsebit.dto.UserSettingDTO;
import com.a406.horsebit.google.dto.request.RefreshDTO;
import com.a406.horsebit.google.dto.request.SignInDTO;
import com.a406.horsebit.google.dto.request.SignUpDTO;
import com.a406.horsebit.google.dto.response.RefreshResponseDTO;
import com.a406.horsebit.google.dto.response.SignInResponseDTO;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;
import java.util.Optional;

public interface UserService {
    public User findById(Long userId);

    public User findByEmail(String email);

    //로그인
    SignInResponseDTO signIn(SignInDTO signInDto) throws ParseException, JOSEException;

    //회원가입
//    User signUp(SignUpDTO signUpDto) throws ParseException, JOSEException;
    User signUp(SignUpDTO signUpDTO) throws ParseException, JOSEException;
    //토큰 정보 전달
    RefreshResponseDTO issueAccessTokenByRefreshToken(RefreshDTO refreshDto);

    //사용자이름(어플에서 사용하는 이름) 중복확인
    boolean isDuplicatedUserName(String userName);

    //회원탈퇴
    void deleteUser(Long userId);

    //헤더에 담긴 액세스 토큰에서 유저 정보 조회
    User userInfoFromToken(String accessToken) throws ParseException;

    /**
     * 개인 설정 조회
     */
    UserSettingDTO findSettingsByUserNo(Long userNo);
    void updateSetting(Long userNo, UserSettingDTO userSetting);
}
