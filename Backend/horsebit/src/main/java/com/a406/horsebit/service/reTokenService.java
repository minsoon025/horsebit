//package com.a406.horsebit.service;
//
//import com.a406.horsebit.config.jwt.TokenProvider;
//import com.a406.horsebit.domain.User;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//
//@RequiredArgsConstructor
//@Service
//public class reTokenService {
//
//    private final TokenProvider tokenProvider;
//    private final RefreshTokenService refreshTokenService;
//    private final UserService userService;
//
//    public String createNewAccessToken(String refreshToken){
//        //토큰 유효성 검사 실패하면 예외 발생
//        if(!tokenProvider.validToken(refreshToken)){
//            throw new IllegalArgumentException("Unexpected token");
//        }
//        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
//        User user = userService.findById(userId);
//
//        return tokenProvider.generateToken(user, Duration.ofHours(2));
//    }
//}