//package com.a406.horsebit.config.oauth;
//
//import com.a406.horsebit.config.jwt.TokenProvider;
//import com.a406.horsebit.domain.RefreshToken;
//import com.a406.horsebit.domain.User;
//import com.a406.horsebit.repository.RefreshTokenRepository;
//import com.a406.horsebit.service.UserService;
//import com.a406.horsebit.util.CookieUtil;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.io.IOException;
//import java.time.Duration;
//
//@RequiredArgsConstructor
//@Component
//@Slf4j
//public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
//    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
//    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
//    public static final String REDIRECT_PATH = "/";
//
//    private final TokenProvider tokenProvider;
//    private final RefreshTokenRepository refreshTokenRepository;
//    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
//    private final UserService userService;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
//        log.info("OAuth2 Login 성공!");
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        User user = userService.findByEmail((String) oAuth2User.getAttributes().get("email"));
//
//        //리프레시 토큰 생성 -> 저장 -> 쿠키에 저장
//        String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION);
//        saveRefreshToken(user.getId(), refreshToken);
//        addRefreshTokenToCookie(request, response, refreshToken);
//
//        //액세스 토큰 생성 -> 패스에 액세스 토큰을 추가
//        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
//        String targetUrl = getTargetUrl(accessToken);
//
//        log.info("액세스토큰 발급 : "+accessToken);
//
//        //인증 관련 설정값, 쿠키 제거
//        clearAuthenticationAttributes(request, response);
//
//        //리다이렉트
//        getRedirectStrategy().sendRedirect(request, response, targetUrl);
//
//        //리프레시 토큰, 액세스 토큰 전송
//        tokenProvider.sendAccessAndRefreshToken(response, accessToken, refreshToken);
//        log.info("리다이렉트, 토큰 정보 전송 완료");
//
//    }
//
//    //생성된 리프레시 토큰을 전달받아 데이터베이스에 저장
//    private void saveRefreshToken(Long userId, String newRefreshToken) {
//        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
//                .map(entity -> entity.update(newRefreshToken))
//                .orElse(new RefreshToken(userId, newRefreshToken));
//
//        refreshTokenRepository.save(refreshToken);
//    }
//
//    //생성된 리프레시 토큰을 쿠키에 저장
//    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
//        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();
//
//        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
//        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
//    }
//
//    //인증 관련 설정값, 쿠키 제거
//    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
//        super.clearAuthenticationAttributes(request);
//        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
//    }
//
//    //액세스 토큰을 패스에 추가
//    private String getTargetUrl(String token) {
//        return UriComponentsBuilder.fromUriString(REDIRECT_PATH)
//                .queryParam("token", token)
//                .build()
//                .toUriString();
//    }
//}