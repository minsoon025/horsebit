package com.a406.horsebit.config;

import com.a406.horsebit.config.jwt.JwtAuthenticationEntryPoint;
import com.a406.horsebit.config.jwt.TokenProvider;
import com.a406.horsebit.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.a406.horsebit.config.oauth.OAuth2UserCustomService;
import com.a406.horsebit.repository.RefreshTokenRepository;
import com.a406.horsebit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebOAuthSecurityConfig {
    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        //토큰 방식으로 인증을 하기 때문에 기존에 사용하던 폼로그인, 세션 비활성화
//        http.csrf().disable()
//                .httpBasic().disable()
//                .formLogin().disable()
//                .logout().disable();
//
//        http.sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//
//
//        http.authorizeRequests()
//                .requestMatchers("/api/token").permitAll()
//                .requestMatchers("/api/**").authenticated()
//                .anyRequest().permitAll();
//
//        http.oauth2Login()
//                .loginPage("/login")
//                .authorizationEndpoint()
//                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
//                .and()
//                .successHandler(oAuth2SuccessHandler())
//                .userInfoEndpoint()
//                .userService(oAuth2UserCustomService);
//
////        http.authorizeRequests()
////                .requestMatchers("/api/token").permitAll()
////                .requestMatchers("/api/**").permitAll()
////                .anyRequest().permitAll()
////            .and()
////            .oauth2Login()
////                .loginPage("/login") // OAuth2 로그인 페이지 설정 (사용하지 않을 경우, 디폴트 로그인 페이지로 이동)
////                .authorizationEndpoint()
////                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
////            .and()
////            .successHandler(oAuth2SuccessHandler())
////            .userInfoEndpoint()
////                .userService(oAuth2UserCustomService);
//
//        http.logout()
//                .logoutSuccessUrl("/login");
//
//
//        http.exceptionHandling()
//                .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
//                        new AntPathRequestMatcher("/api/**"));

        http
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/*/*/auth/**").permitAll()
                                .requestMatchers("/api/**").authenticated()
                                .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new TokenAuthenticationFilter(tokenProvider), AnonymousAuthenticationFilter.class)
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(new JwtAuthenticationEntryPoint()));


        return http.build();
    }


    @Bean
    public AuthenticationSuccessHandler oAuth2SuccessHandler() {
//        return new OAuth2SuccessHandler(tokenProvider,
//                refreshTokenRepository,
//                oAuth2AuthorizationRequestBasedOnCookieRepository(),
//                userService
//        );
        return null;
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
