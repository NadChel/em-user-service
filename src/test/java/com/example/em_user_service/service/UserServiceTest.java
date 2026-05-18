package com.example.em_user_service.service;

import com.example.em_user_service.data.dto.request.UserRequestDto;
import com.example.em_user_service.data.dto.response.UserResponseDto;
import com.example.em_user_service.data.entity.Role;
import com.example.em_user_service.data.entity.User;
import com.example.em_user_service.mapper.UserMapper;
import com.example.em_user_service.repository.RoleRepository;
import com.example.em_user_service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UserMapper mapper;
    @InjectMocks
    UserService userService;

    @Test
    void findAll_givenUsersExist_returnsExpectedUserDtos() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        List<User> users = List.of(user);
        given(userRepository.findAll(any())).willReturn(users);
        UserResponseDto expectedResponseUser = UserResponseDto.builder().id(userId).build();
        given(mapper.toDto(user)).willReturn(expectedResponseUser);

        List<UserResponseDto> responseUsers = userService.findAll(any());

        assertThat(responseUsers).containsExactly(expectedResponseUser);
    }

    @Test
    void findById_givenUserExists_returnsExpectedUserDto() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        given(userRepository.findById(userId)).willReturn(Optional.ofNullable(user));
        UserResponseDto expectedResponseUser = UserResponseDto.builder().id(userId).build();
        given(mapper.toDto(user)).willReturn(expectedResponseUser);

        UserResponseDto responseUser = userService.findById(userId);

        assertThat(responseUser).isEqualTo(expectedResponseUser);
    }

    @Test
    void existsByName_givenUserExists_returnsTrue() {
        String name = "sam";
        given(userRepository.existsByName(name)).willReturn(true);

        assertThat(userService.existsByName(name)).isTrue();
    }

    @Test
    void saveUser_givenUserSaved_returnsExpectedDto() {
        UUID userId = UUID.randomUUID();
        String name = "serge";
        String password = "password";
        String encodedPassword = "wsjkfsasd";
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .name(name)
                .password(password)
                .build();
        User user = User.builder().name(name).password(password).build();
        given(mapper.toUser(userRequestDto)).willReturn(user);
        UserResponseDto expectedResponseUser = UserResponseDto.builder()
                .id(userId)
                .name(name)
                .build();
        given(mapper.toDto(user)).willReturn(expectedResponseUser);
        Role userRole = Role.forName("USER");
        given(roleRepository.findByName("USER")).willReturn(Optional.of(userRole));
        given(userRepository.save(user)).willReturn(user);
        given(passwordEncoder.encode(password)).willReturn(encodedPassword);

        UserResponseDto responseUser = userService.saveUser(userRequestDto);

        assertThat(responseUser).isEqualTo(expectedResponseUser);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        then(userRepository).should().save(captor.capture());
        assertThat(captor.getValue().getPassword()).isEqualTo(encodedPassword);
        assertThat(captor.getValue().getRoles()).containsExactly(userRole);
    }

    @Test
    void saveAdmin_givenUserSaved_returnsExpectedDto() {
        UUID userId = UUID.randomUUID();
        String name = "serge";
        String password = "password";
        String encodedPassword = "wsjkfsasd";
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .name(name)
                .password(password)
                .build();
        User user = User.builder().name(name).password(password).build();
        given(mapper.toUser(userRequestDto)).willReturn(user);
        UserResponseDto expectedResponseUser = UserResponseDto.builder()
                .id(userId)
                .name(name)
                .build();
        given(mapper.toDto(user)).willReturn(expectedResponseUser);
        Role adminRole = Role.forName("ADMIN");
        given(roleRepository.findByName("ADMIN")).willReturn(Optional.of(adminRole));
        given(userRepository.save(user)).willReturn(user);
        given(passwordEncoder.encode(password)).willReturn(encodedPassword);

        UserResponseDto responseUser = userService.saveAdmin(userRequestDto);

        assertThat(responseUser).isEqualTo(expectedResponseUser);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        then(userRepository).should().save(captor.capture());
        assertThat(captor.getValue().getPassword()).isEqualTo(encodedPassword);
        assertThat(captor.getValue().getRoles()).containsExactly(adminRole);
    }

    @Test
    void deleteById_givenSuccessfulDelete_returnsExpectedDto() {
        UUID userId = UUID.randomUUID();
        willDoNothing().given(userRepository).deleteById(userId);

        userService.deleteById(userId);

        then(userRepository).should().deleteById(userId);
    }
}