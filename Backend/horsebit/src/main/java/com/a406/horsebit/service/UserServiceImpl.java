package com.a406.horsebit.service;

import com.a406.horsebit.config.jwt.TokenProvider;
import com.a406.horsebit.domain.User;
import com.a406.horsebit.dto.UserSettingDTO;
import com.a406.horsebit.google.domain.Role;
import com.a406.horsebit.google.dto.request.*;
import com.a406.horsebit.google.dto.response.RefreshResponseDTO;
import com.a406.horsebit.google.dto.response.SignInResponseDTO;
import com.a406.horsebit.google.exception.NoSuchUserException;
import com.a406.horsebit.google.repository.InMemoryProviderRepository;
import com.a406.horsebit.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final InMemoryProviderRepository inMemoryProviderRepository;

    public User findById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    @Override
    public SignInResponseDTO signIn(SignInDTO signInDto) throws ParseException, JOSEException {
        log.info("로그인");
//        OAuthProvider provider = findProvider(signInDto.getProviderName());
//        String jwksStr = provider.getJwks();
        String idToken =signInDto.getToken();
        log.info("토큰입니다." + idToken);
        SignedJWT signedJWT = (SignedJWT) tokenProvider.parseTokenWithoutValidation(idToken);

        try {
            tokenProvider.validateJwtWithJwk(idToken);
        } catch (Exception e) {
            log.info("Id Token 검증 실패: {}", e.getMessage());
            throw new IllegalArgumentException("Id Token이 유효하지 않습니다.");
        }

        // 사용자 조회
//        JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
//        String sub = jwtClaimsSet.getStringClaim("sub");
//        String providerId = signInDto.getProviderName() + "_" + sub;
        String email = tokenProvider.extractEmail(idToken);
        log.info("이메일 : " + email);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchUserException("회원 가입을 먼저 진행하십시오."));
        String accessToken = tokenProvider.bulidAccessToken(user);
        String refreshToken = tokenProvider.buildRefreshToken(user);

        user.setRefreshToken(refreshToken);

        return SignInResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userDTO(UserDTO.from(user))
                .build();
    }

    @Override
    public User signUp(SignUpDTO signUpDTO) throws ParseException, JOSEException {
        log.info("회원가입 중");
//        OAuthProvider provider = findProvider(signUpDTO.getProviderName());
//        String jwksStr = provider.getJwks();
        String idToken = signUpDTO.getToken();

        SignedJWT signedJWT = (SignedJWT) tokenProvider.parseTokenWithoutValidation(idToken);

        try {
//            tokenProvider.validateJwtWithJwk(idToken, jwksStr);
            tokenProvider.validateJwtWithJwk(idToken);
        } catch (Exception e) {
            throw new IllegalArgumentException("Id Token이 유효하지 않습니다. " + e.getMessage());
        }

        // 사용자 정보를 저장
//        JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
//        String sub = jwtClaimsSet.getStringClaim("sub");
//        String providerId = signUpDTO.getProviderName() + "_" + sub;

        String email = tokenProvider.extractEmail(idToken);
        log.info("이메일 : " + email);
        String nickname = tokenProvider.extractNickname(idToken);
        User user = User.builder()
                        .email(email)
                        .nickname(nickname)
                        .userName(signUpDTO.getUserName())
                        .build();

//        user.setProviderId(providerId);
        user.setRole(Role.USER);

        log.info("회원가입 완료");
        return userRepository.save(user);
    }


    @Override
    public RefreshResponseDTO issueAccessTokenByRefreshToken(RefreshDTO refreshDTO) {
        log.info("Refresh Token 검증");
        try {
            SignedJWT signedJWT = (SignedJWT) tokenProvider.parseRefreshToken(refreshDTO.getRefreshToken());
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            User user = userRepository.findByEmail(claims.getStringClaim("email")).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

            if (!user.getRefreshToken().equals(refreshDTO.getRefreshToken())) {
                throw new IllegalArgumentException("유효하지 않은 Refresh Token 입니다.");
            }

            String refreshToken = tokenProvider.buildRefreshToken(user);
            user.setRefreshToken(refreshToken);

            String accessToken = tokenProvider.bulidAccessToken(user);

            return RefreshResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (ParseException | JOSEException e) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token 입니다. " + e.getMessage());
        }
    }

    //userName 중복확인
    @Override
    public boolean isDuplicatedUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }


//    private OAuthProvider findProvider(String providerName) {
//        return inMemoryProviderRepository.findByProviderName(providerName);
//    }

    //회원탈퇴
    @Override
    public void deleteUser(Long userId){
        userRepository.deleteById(userId);
    }

    @Override
    public UserSettingDTO findSettingsByUserNo(Long userNo) {
        log.info("UserServiceImpl::findSettingsByUserNo() START");
        User user = userRepository.findById(userNo)
            .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));

        log.info("userSetting: " + user.isAlarmPushFlag() + ", " + user.isBiometricLoginFlag());
        return new UserSettingDTO(user.isAlarmPushFlag(), user.isBiometricLoginFlag());
    }

    @Override
    public void updateSetting(Long userNo, UserSettingDTO userSetting) {
        userRepository.updateSettingByUserNo(userNo, userSetting.isAlarmOn(), userSetting.isBiometricLogin());
    }
}
