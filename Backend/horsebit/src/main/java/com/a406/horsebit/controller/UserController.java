package com.a406.horsebit.controller;

import com.a406.horsebit.config.jwt.TokenProvider;
import com.a406.horsebit.domain.User;
import com.a406.horsebit.dto.UserSettingDTO;

import com.a406.horsebit.config.oauth.OAuth2UserCustomService;

import com.a406.horsebit.repository.UserRepository;
import com.a406.horsebit.service.UserService;
import com.nimbusds.jose.shaded.gson.JsonObject;

import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getUser(@RequestHeader(required = true, name = "Authorization") String token){

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //회원 탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity deleteUser(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        String token = (request.getHeader("Authorization")).substring("Bearer ".length());
        SignedJWT signedJWT = (SignedJWT) tokenProvider.parseAccessToken(token);
        String email = signedJWT.getJWTClaimsSet().getStringClaim("email");
        log.info("사용자 이메일 : "+email);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("토큰에 맞는 사용자정보가 없습니다."));

        Long userId = user.getId();
        log.info("delete user id : {}", userId);

        userService.deleteUser(userId);
        return new ResponseEntity(HttpStatus.OK);
    }

    //TODO: OAuth 개발하여 userNo 삭제 필요
    /**
     * 개인 설정 조회
     */
    @GetMapping("/setting")
    public UserSettingDTO findSettings() {
        Long userNo = 1L;
        log.info("UserController::findSettings() START");

        UserSettingDTO result = userService.findSettingsByUserNo(userNo);
        log.info("" + result.isAlarmOn() + result.isBiometricLogin());
        return result;
    }

    /**
     * 개인 설정 변경
     */
    @PostMapping("/setting")
    public String updateSetting(@RequestBody UserSettingDTO userSetting) {
        Long userNo = 1L;
        log.info("UserController::updateSetting() START");
        JsonObject obj = new JsonObject();

        log.info("RequestBody: " + userSetting.isAlarmOn() + ", " + userSetting.isBiometricLogin());
        userService.updateSetting(userNo, userSetting);

        obj.addProperty("result", "SUCCESS");
        return obj.toString();
    }

}
