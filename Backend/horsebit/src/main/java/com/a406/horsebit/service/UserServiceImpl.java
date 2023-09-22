package com.a406.horsebit.service;

import java.util.Optional;

import com.a406.horsebit.domain.User;
import com.a406.horsebit.dto.UserSettingDTO;
import com.a406.horsebit.dto.request.AddUserRequest;
import com.a406.horsebit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public Long save(AddUserRequest dto){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .build()).getId();
    }
    public User findById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
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
