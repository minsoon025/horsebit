package com.a406.horsebit.controller;

import com.a406.horsebit.config.oauth.OAuth2UserCustomService;
import com.a406.horsebit.dto.UserLogDto;
import com.a406.horsebit.dto.request.AddUserRequest;
import com.a406.horsebit.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final OAuth2UserCustomService oAuth2UserCustomService;

//    @GetMapping("/login")
//    public String login(@RequestBody UserLogDto userResponseDto) throws Exception{
//        oAuth2UserCustomService.loadUser(userResponseDto);
//        return "로그인 성공";
//    }

    @GetMapping("/login")
    public String login(){
        return "oauthLogin";
    }

//    @GetMapping("/signup")
//    public String signup(){
//        return "signup";
//    }

    @PostMapping("/user")
    public String signup(AddUserRequest request){
        userService.save(request);  //회원가입 메서드 호출
        return "redirect:/login";   //회원가입 완료 후 로그인 페이지로 이동
    }

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
}
