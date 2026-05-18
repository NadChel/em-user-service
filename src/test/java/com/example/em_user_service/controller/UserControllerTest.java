package com.example.em_user_service.controller;

import com.example.em_user_service.data.dto.request.UserRequestDto;
import com.example.em_user_service.data.dto.response.UserResponseDto;
import com.example.em_user_service.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvcTester mvcTester;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    UserService userService;

    @Test
    void findAll_givenUsersExist_returns200_andExpectedDtos() {
        UUID userId = UUID.randomUUID();
        String name = "Joshua";
        UserResponseDto user = UserResponseDto.builder().id(userId).name(name).build();
        List<UserResponseDto> users = List.of(user);
        given(userService.findAll(any())).willReturn(users);

        mvcTester.get().uri("/api/admin/users")
                .with(adminJwt())
                .exchange()
                .assertThat()
                .hasStatusOk()
                .bodyJson().convertTo(list(UserResponseDto.class))
                .hasSize(1)
                .containsExactlyElementsOf(users);
    }


    private static JwtRequestPostProcessor adminJwt() {
        return SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt
                .subject(UUID.randomUUID().toString())
                .claim("roles", List.of("ADMIN")));
    }

    @Test
    void findById_givenUserExist_returns200_andExpectedDto() {
        UUID userId = UUID.randomUUID();
        String name = "Joshua";
        UserResponseDto user = UserResponseDto.builder().id(userId).name(name).build();
        given(userService.findById(userId)).willReturn(user);

        mvcTester.get().uri("/api/admin/users/{id}", userId)
                .with(adminJwt())
                .exchange()
                .assertThat()
                .hasStatusOk()
                .bodyJson().convertTo(UserResponseDto.class)
                .isEqualTo(user);
    }

    @Test
    void saveUser_givenUserSaved_returns201_andExpectedDto() {
        UUID userId = UUID.randomUUID();
        String name = "Joshua";
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .name(name)
                .password(name)
                .build();
        UserResponseDto responseUser = UserResponseDto.builder()
                .id(userId)
                .name(name)
                .roles(Set.of("USER"))
                .build();
        given(userService.saveUser(userRequestDto)).willReturn(responseUser);

        mvcTester.post().uri("/api/admin/users")
                .with(adminJwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto))
                .exchange()
                .assertThat()
                .hasStatus(HttpStatus.CREATED)
                .bodyJson().convertTo(UserResponseDto.class)
                .isEqualTo(responseUser);
    }

    @Test
    void saveAdmin_givenAdminSaved_returns201_andExpectedDto() {
        UUID userId = UUID.randomUUID();
        String name = "Joshua";
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .name(name)
                .password(name)
                .build();
        UserResponseDto responseUser = UserResponseDto.builder()
                .id(userId)
                .name(name)
                .roles(Set.of("ADMIN"))
                .build();
        given(userService.saveAdmin(userRequestDto)).willReturn(responseUser);

        mvcTester.post().uri("/api/admin/users/admins")
                .with(adminJwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto))
                .exchange()
                .assertThat()
                .hasStatus(HttpStatus.CREATED)
                .bodyJson().convertTo(UserResponseDto.class)
                .isEqualTo(responseUser);
    }

    @Test
    void delete_givenUserDeleted_returns200() {
        UUID userId = UUID.randomUUID();
        willDoNothing().given(userService).deleteById(userId);

        mvcTester.delete().uri("/api/admin/users/{id}", userId)
                .with(adminJwt())
                .exchange()
                .assertThat()
                .hasStatusOk()
                .body().isEmpty();
    }
}