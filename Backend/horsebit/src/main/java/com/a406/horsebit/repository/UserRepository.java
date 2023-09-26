package com.a406.horsebit.repository;

import com.a406.horsebit.domain.User;
import com.a406.horsebit.dto.UserSettingDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // email로 사용자 정보 가져옴
    Optional<User> findByEmail(String Email);

    //사용자 이름(앱에서 이름) 중복체크
    boolean existsByUserName(String UserName);

    Optional<User> findByProviderId(String providerId);
    @Modifying(clearAutomatically = true)
    @Query("update User u set u.alarmPushFlag = :alarmOn, u.biometricLoginFlag = :biometricLogin where u.id = :userNo")
    void updateSettingByUserNo(Long userNo, boolean alarmOn, boolean biometricLogin);
}