package com.a406.horsebit.config.jwt;

import com.a406.horsebit.domain.User;
import com.a406.horsebit.repository.UserRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Service
@Slf4j
public class TokenProvider {
////    private final JwtProperties jwtProperties;
//
////    @Value("${jwt.access.header}")
//    private String accessHeader;
//
////    @Value("${jwt.refresh.header}")
//    private String refreshHeader;

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

//    public String generateToken(User user, Duration expiredAt){
//        Date now = new Date();
//        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
//    }
//
//    //JWT 토큰 생성 메서드
//    private String makeToken(Date expiry, User user) {
//        Date now = new Date();
//        String jwtToken = Jwts.builder()
//                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)   //헤더 TYP : JWT
//                // 내용 iss : ajufresh@gmail.com (propertise 파일에서 설정한 값)
//                .setIssuer(jwtProperties.getIssuer())
//                .setIssuedAt(now)       // 내용 iat : 현재 시간
//                .setExpiration(expiry)  // 내용 exp : expiry 멤버 변숫값
//                .setSubject(user.getEmail())    //내용 sub : 유저의 이메일
//                .claim("id", user.getId())  // 클레임 id : 유저 ID
//                // 서명 : 비밀값과 함께 해시값을 HS256 방식으로 암호화
//                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretkey())
//                .compact();
//        System.out.println("jwt토큰 : "+jwtToken);
//        return jwtToken;
//    }
//
//    //JWT 토큰 유효성 검증 메서드
//    public boolean validToken(String token){
//        try{
//            Jwts.parser()
//                    .setSigningKey(jwtProperties.getSecretkey())    //비밀값으로 복호화
//                    .parseClaimsJws(token);
//            return true;
//        } catch (Exception e) { //복호화 과정에서 에러가 나면 유효하지 않은 토큰
//            return false;
//        }
//    }
//
//    //토큰 기반으로 인증 정보를 가져오는 메서드
//    public Authentication getAuthentication(String token){
//        Claims claims = getClaims(token);
//        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
//
//        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities), token, authorities);
//    }
//
//    //토큰 기반으로 유저 ID를 가져오는 메서드
//    public Long getUserId(String token) {
//        Claims claims = getClaims(token);
//        return claims.get("id", Long.class);
//    }
//
//    //클레임 조회
//    private Claims getClaims(String token){
//        return Jwts.parser().setSigningKey(jwtProperties.getSecretkey())
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//
//    private void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
//        response.setHeader(refreshHeader, refreshToken);
//    }
//
//    private void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
//        response.setHeader(accessHeader, accessToken);
//    }
//
//
//    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
//        response.setStatus(HttpServletResponse.SC_OK);
//
//        setAccessTokenHeader(response, accessToken);
//        setRefreshTokenHeader(response, refreshToken);
//        log.info("Access Token, Refresh Token 헤더 설정 완료");
//    }


    public Authentication getAuthentication(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = (SignedJWT) parseAccessToken(token);
        String email = signedJWT.getJWTClaimsSet().getStringClaim("email");

        // AuthenticationManager 거치지 않고 Authentication 진행
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("토큰에 맞는 사용자정보가 없습니다."));

        // 현재 권한은 하나만 부여가능
        Collection<? extends GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(user.getRole().getKey())
        );

        return new UsernamePasswordAuthenticationToken(user.getId(), token, authorities);
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

        //TODO: 아래 주석처리한 코드는 providerName 사용 했을 때 -> 프론트와 이야기 후 삭제 예정
//    public void validateJwtWithJwk(String token, String jwkStr) throws JOSEException, ParseException {
////        try {
////            log.debug("JWK를 이용하여 JWT 검증 시작... token: {}, jwkStr: {}", token, jwkStr);
////
////            SignedJWT signedJWT = SignedJWT.parse(token);
////            log.debug("검증 대상 토큰의 Calims signedJWT: {}", signedJWT.getJWTClaimsSet().toString());
////            JWKSelector jwkSelector = new JWKSelector(JWKMatcher.forJWSHeader(signedJWT.getHeader()));
////
////            log.debug("JWK String을 파싱...");
////            JWKSet jwkSet = JWKSet.parse(jwkStr);
////            log.debug("JWK String 파싱 결과 jwkSet: {}", jwkSet.getKeys());
////            JWK jwk = jwkSelector.select(jwkSet).get(0);
////
////            PublicKey publicKey = jwk.toRSAKey().toPublicKey();
////
////            RSASSAVerifier verifier = new RSASSAVerifier((RSAPublicKey) publicKey);
////
////            if (!signedJWT.verify(verifier)) {
////                throw new IllegalArgumentException("ID Token이 유효하지 않습니다.");
////            }
////
////            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
////            if (!(jwtClaimsSet.getStringClaim("iss").equals("https://accounts.google.com") || jwtClaimsSet.getStringClaim("aud").equals("423467642364-p8mlbskkjht9t5dptd2odmosrb2g47ta.apps.googleusercontent.com"))) {
////                throw new IllegalArgumentException(("idToken의 iss 혹은 aud가 일치하지 않습니다."));
////            }
////
////            if (jwtClaimsSet.getExpirationTime().before(new Date())) {
////                log.info("만료시각을 검증중... now: {}, exp: {}", new Date(), jwtClaimsSet.getExpirationTime());
////                throw new IllegalArgumentException("idToken이 만료되었습니다.");
////            }
////        } catch (JOSEException | ParseException e) {
////            throw new IllegalArgumentException("id Token이 유효하지 않습니다. " + e.getMessage());
////        }
////
////    }


    public void validateJwtWithJwk(String token) throws JOSEException, ParseException {
        try {
            log.info("JWK를 이용하여 JWT 검증 시작... token: {}", token);

            SignedJWT signedJWT = SignedJWT.parse(token);
            log.debug("검증 대상 토큰의 Calims signedJWT: {}", signedJWT.getJWTClaimsSet().toString());
            JWKSelector jwkSelector = new JWKSelector(JWKMatcher.forJWSHeader(signedJWT.getHeader()));

//            log.debug("JWK String을 파싱...");
//            JWKSet jwkSet = JWKSet.parse(jwkStr);
//            log.debug("JWK String 파싱 결과 jwkSet: {}", jwkSet.getKeys());
//            JWK jwk = jwkSelector.select(jwkSet).get(0);

//            PublicKey publicKey = jwk.toRSAKey().toPublicKey();

//            RSASSAVerifier verifier = new RSASSAVerifier((RSAPublicKey) publicKey);
//
//            if (!signedJWT.verify(verifier)) {
//                throw new IllegalArgumentException("ID Token이 유효하지 않습니다.");
//            }

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
//            RSVerifier verifier = new RSVerifier(publicKey);
            log.info(signedJWT.toString());
            log.info(verifier.supportedJWSAlgorithms().toString());

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


    //성민 이메일 유효 테스트 메서드 제작
    public String extractEmailTest(String token) {
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

}