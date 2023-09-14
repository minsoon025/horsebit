package com.a406.horsebit.service;

import com.a406.horsebit.domain.User;
import com.a406.horsebit.dto.request.AddUserRequest;

public interface UserService {
    public Long save(AddUserRequest dto);

    public User findById(Long userId);

    public User findByEmail(String email);
}
