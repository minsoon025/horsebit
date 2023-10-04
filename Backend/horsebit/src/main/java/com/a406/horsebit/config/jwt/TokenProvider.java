package com.a406.horsebit.config.jwt;

import com.a406.horsebit.domain.User;
import com.a406.horsebit.repository.UserRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Service
@Slf4j
public class TokenProvider {

    private final Long ACCESS_TOKEN_EXPIRATION_TIME;
    private final Long REFRESH_TOKEN_EXPIRATION_TIME;
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private final UserRepository userRepository;
    private final byte[] sharedSecret;

    public TokenProvider(
            @Value("1200000") Long ACCESS_TOKEN_EXPIRATION_TIME,
            @Value("1210000000") Long REFRESH_TOKEN_EXPIRATION_TIME,
            UserRepository userRepository) {
        this.ACCESS_TOKEN_EXPIRATION_TIME = ACCESS_TOKEN_EXPIRATION_TIME;
        this.REFRESH_TOKEN_EXPIRATION_TIME = REFRESH_TOKEN_EXPIRATION_TIME;
        this.userRepository = userRepository;

        // Generate random 256-bit (32-byte) shared secret
        SecureRandom random = new SecureRandom();
        sharedSecret = new byte[32];
        random.nextBytes(sharedSecret);
    }

    //인증 절차
    public Authentication getAuthentication(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = (SignedJWT) parseAccessToken(token);
        String email = signedJWT.getJWTClaimsSet().getStringClaim("email");

        // AuthenticationManager 거치지 않고 Authentication 진행
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("토큰에 맞는 사용자정보가 없습니다."));

        // 현재 권한은 하나만 부여가능
        Collection<? extends GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(user.getRole().getKey())
        );

        return new UsernamePasswordAuthenticationToken(user, token, authorities);
    }



    //AccessToken 생성 메서드
    public String bulidAccessToken(User user) throws JOSEException, ParseException {
        JWSSigner signer = new MACSigner(sharedSecret);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(ACCESS_TOKEN_SUBJECT)
                .expirationTime(new Date(new Date().getTime() + ACCESS_TOKEN_EXPIRATION_TIME))
                .claim("email", user.getEmail())
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

        signedJWT.sign(signer);

        String s = signedJWT.serialize();

        return s;
    }

    //Refresh Token 생성 메서드
    public String buildRefreshToken(User user) throws JOSEException {
        JWSSigner signer = new MACSigner(sharedSecret);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(REFRESH_TOKEN_SUBJECT)
                .expirationTime(new Date(new Date().getTime() + REFRESH_TOKEN_EXPIRATION_TIME))
                .claim("email", user.getEmail())
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

        signedJWT.sign(signer);

        String s = signedJWT.serialize();

        return s;
    }

    //검증 없이 토큰 파싱 ( idToken 파싱 시 사용)
    public Object parseTokenWithoutValidation(String token) throws ParseException, JOSEException {
        return SignedJWT.parse(token);
    }

    public void validateJwtWithJwk(String token) throws JOSEException, ParseException {
        try {
            log.info("JWK를 이용하여 JWT 검증 시작... token: {}", token);

            SignedJWT signedJWT = SignedJWT.parse(token);
            log.debug("검증 대상 토큰의 Calims signedJWT: {}", signedJWT.getJWTClaimsSet().toString());
            JWKSelector jwkSelector = new JWKSelector(JWKMatcher.forJWSHeader(signedJWT.getHeader()));

            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
            if (!(jwtClaimsSet.getStringClaim("iss").equals("https://accounts.google.com") || jwtClaimsSet.getStringClaim("aud").equals("423467642364-p8mlbskkjht9t5dptd2odmosrb2g47ta.apps.googleusercontent.com"))) {
                throw new IllegalArgumentException(("idToken의 iss 혹은 aud가 일치하지 않습니다."));
            }

            if (jwtClaimsSet.getExpirationTime().before(new Date())) {
                log.info("만료시각을 검증중... now: {}, exp: {}", new Date(), jwtClaimsSet.getExpirationTime());
                throw new IllegalArgumentException("idToken이 만료되었습니다.");
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException("id Token이 유효하지 않습니다. " + e.getMessage());
        }

    }

    public Object parseAccessToken(String token) {
        SignedJWT signedJWT = null;
        try {
            signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(sharedSecret);

            log.info(signedJWT.toString());
            log.info(verifier.toString());

            if (!signedJWT.verify(verifier)) {
                throw new IllegalArgumentException("Access 토큰이 유효하지 않습니다.");
            }
            log.info(signedJWT.toString());
;
            if (signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date())) {
                throw new IllegalArgumentException("Access 토큰이 만료되었습니다.");
            }

        } catch (ParseException | JOSEException e) {
            throw new IllegalArgumentException("Refresh 토큰이 유효하지 않습니다." + e.getMessage());
        }

        return signedJWT;
    }

    public Object parseRefreshToken(String refreshToken) {
        SignedJWT signedJWT = null;
        try {
            signedJWT = SignedJWT.parse(refreshToken);
            JWSVerifier verifier = new MACVerifier(sharedSecret);

            if (!signedJWT.verify(verifier)) {
                throw new IllegalArgumentException("Refresh 토큰이 유효하지 않습니다.");
            }

            if (signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date())) {
                throw new IllegalArgumentException("Refresh 토큰이 만료되었습니다.");
            }
        } catch (ParseException | JOSEException e) {
            throw new IllegalArgumentException("Refresh 토큰이 유효하지 않습니다. " + e.getMessage());
        }


        return signedJWT;
    }


    //idToken으로부터 이메일 추출 메서드 제작
    public String extractEmail(String token) {
        String[] parts = token.split("\\.");

        if (parts.length == 3) {
            try {
                String payload = new String(Base64.getDecoder().decode(parts[1]));
                JsonObject payloadJson = new JsonParser().parse(payload).getAsJsonObject();
                String email = payloadJson.get("email").getAsString();
                return email;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    //idToken으로부터 닉네임 추출 메서드 제작
    public String extractNickname(String token) {
        String[] parts = token.split("\\.");

        if (parts.length == 3) {
            try {
                String payload = new String(Base64.getDecoder().decode(parts[1]));
                JsonObject payloadJson = new JsonParser().parse(payload).getAsJsonObject();
                String nickname = payloadJson.get("name").getAsString();
                return nickname;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

}