package com.a406.horsebit.service;

import com.a406.horsebit.dto.VolumeDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TokenServiceImplTest {
    TokenService tokenService;

    @Autowired
    TokenServiceImplTest(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Test
    void findTokenVolumes() {
        List<VolumeDTO> tokenVolumes = tokenService.findTokenVolumes(1L);
    }
}