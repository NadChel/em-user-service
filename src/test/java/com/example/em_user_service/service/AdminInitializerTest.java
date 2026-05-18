package com.example.em_user_service.service;

import com.example.em_user_service.data.dto.request.UserRequestDto;
import com.example.em_user_service.data.properties.AdminProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class AdminInitializerTest {

    @Mock
    UserService userService;
    @Mock
    AdminProperties adminProperties;
    @InjectMocks
    AdminInitializer adminInitializer;

    @Test
    void run_ifAdminExists_doesNothing() {
        String adminUsername = "admin";
        given(adminProperties.getUsername()).willReturn(adminUsername);
        given(userService.existsByName(adminUsername)).willReturn(true);

        adminInitializer.run(mock());

        then(userService).should(never()).saveAdmin(any());
    }

    @Test
    void run_ifAdminDoesNotExist_savesAdmin() {
        String adminUsername = "admin";
        String adminPassword = "password";
        given(adminProperties.getUsername()).willReturn(adminUsername);
        given(adminProperties.getPassword()).willReturn(adminPassword);
        given(userService.existsByName(adminUsername)).willReturn(false);

        adminInitializer.run(mock());

        ArgumentCaptor<UserRequestDto> captor = ArgumentCaptor.forClass(UserRequestDto.class);
        then(userService).should().saveAdmin(captor.capture());
        UserRequestDto savedUser = captor.getValue();
        assertThat(savedUser.getName()).isEqualTo(adminUsername);
        assertThat(savedUser.getPassword()).isEqualTo(adminPassword);
    }
}