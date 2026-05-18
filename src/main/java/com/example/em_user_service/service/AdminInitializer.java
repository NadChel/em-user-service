package com.example.em_user_service.service;

import com.example.em_user_service.data.dto.request.UserRequestDto;
import com.example.em_user_service.data.properties.AdminProperties;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    private final UserService userService;
    private final AdminProperties adminProperties;

    @Override
    @Transactional
    public void run(@NonNull ApplicationArguments args) {
        String adminUsername = adminProperties.getUsername();
        if (userService.existsByName(adminUsername)) return;
        userService.saveAdmin(createAdmin());
    }

    private UserRequestDto createAdmin() {
        UserRequestDto admin = new UserRequestDto();
        admin.setName(adminProperties.getUsername());
        admin.setPassword(adminProperties.getPassword());
        return admin;
    }
}