package com.a406.horsebit.controller;

import com.a406.horsebit.dto.UserSettingDTO;

import com.a406.horsebit.config.oauth.OAuth2UserCustomService;

import com.a406.horsebit.dto.request.AddUserRequest;
import com.a406.horsebit.service.UserService;
import com.nimbusds.jose.shaded.gson.JsonObject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final OAuth2UserCustomService oAuth2UserCustomService;


    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    @GetMapping
    public ResponseEntity<?> getUser(@RequestHeader(required = true, name = "Authorization") String token){

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //회원 탈퇴
    @DeleteMapping("/api/user/delete")
    public ResponseEntity deleteUser(@RequestParam Long userId){
        log.info("delete user id : {}", userId);

        userService.deleteUser(userId);
        return new ResponseEntity(HttpStatus.OK);
    }



    //TODO: mapping url 공통으로 묶기
    //TODO: OAuth 개발하여 userNo 삭제 필요
    /**
     * 개인 설정 조회
     */
    @GetMapping("/api/user/setting")
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
    @PostMapping("/api/user/setting")
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
