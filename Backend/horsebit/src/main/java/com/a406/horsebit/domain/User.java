package com.a406.horsebit.domain;

import com.a406.horsebit.google.domain.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "USER_INFO")
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no", updatable = false)
    private Long id;

    @Column(name = "nickname", unique = true)
    private String nickname;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    private String password; //TODO: 필요없어서 삭제필요

    @Column(name = "user_name")
    private String userName;

    public String getUserName() {
        return userName;
    }

    //    private String providerName; // 우리는 구글 로그인이라 google
//    private String providerId; // "google_" + Google, Naver, Kakao에서 로그인시 전달되는 id
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role;


    @Column(name = "alarm_push_flag", nullable = false)
    private boolean alarmPushFlag;

    @Column(name = "biometric_login_flag", nullable = false)
    private boolean biometricLoginFlag;

    @Builder
    public User(Long id, String nickname, String password, String email, String userName, boolean alarmPushFlag, boolean biometricLoginFlag) {
        this.id = id;
//        this.providerName = providerName;
//        this.providerId = providerId;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.userName = userName;
        this.alarmPushFlag = alarmPushFlag;
        this.biometricLoginFlag = biometricLoginFlag;
    }

    // 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    //사용자 id를 반환(고유한 값)
    @Override
    public String getUsername() {
        return email;
    }

    //TODO: 성민 확인 필요 - password 필요없다고 했는데 Override 필수인데 어떻게 해야돼?
    //사용자의 패스워드 반환
    @Override
    public String getPassword() {
        return password;
    }

    //계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        //민료되었는지 확인하는 로직
        return true;    //true -> 만료되지 않았음
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;    //true -> 잠금되지 않았음
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;    //true -> 만료되지 않았음
    }

    @Override
    public boolean isEnabled() {
        return true;    //true -> 사용가능
    }

    //사용자 이름 변경
    public User update(String nickname){
        this.nickname = nickname;

        return this;
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }
}
