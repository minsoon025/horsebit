package com.a406.horsebit.service;

import com.a406.horsebit.domain.User;
import com.a406.horsebit.dto.UserSettingDTO;
import com.a406.horsebit.dto.request.AddUserRequest;

public interface UserService {
    public Long save(AddUserRequest dto);

    public User findById(Long userId);

    public User findByEmail(String email);

    /**
     * 개인 설정 조회
     */
    UserSettingDTO findSettingsByUserNo(Long userNo);
    void updateSetting(Long userNo, UserSettingDTO userSetting);
}
