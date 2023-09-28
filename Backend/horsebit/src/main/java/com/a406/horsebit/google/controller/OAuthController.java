package com.a406.horsebit.google.controller;

import com.a406.horsebit.domain.User;
import com.a406.horsebit.google.dto.request.SignInDTO;
import com.a406.horsebit.google.dto.request.SignUpDTO;
import com.a406.horsebit.google.dto.response.SignInResponseDTO;
import com.a406.horsebit.service.UserService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/login/auth")
public class OAuthController {

    private final UserService userService;

    //로그인
    @PostMapping("/signIn")
    public ResponseEntity<SignInResponseDTO> signIn(@RequestBody SignInDTO signInDTO) throws ParseException, JOSEException {
        log.debug("signIn()");

        SignInResponseDTO signInResponseDTO = userService.signIn(signInDTO);

        return new ResponseEntity<>(signInResponseDTO, HttpStatus.OK);
    }

    //회원가입
    @PostMapping("/signUp")
    public ResponseEntity<User> signUp(@RequestBody SignUpDTO signUpDTO) throws ParseException, JOSEException {
        log.debug("signUp()");

        User signUpUser = userService.signUp(signUpDTO);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

//    @GetMapping("/duplication")
//    public ResponseEntity<>
}
