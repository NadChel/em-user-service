package com.example.em_user_service.service;

import com.example.em_user_service.data.dto.request.UserRequestDto;
import com.example.em_user_service.data.dto.response.UserResponseDto;
import com.example.em_user_service.data.entity.Role;
import com.example.em_user_service.data.entity.User;
import com.example.em_user_service.repository.RoleRepository;
import com.example.em_user_service.repository.UserRepository;
import com.example.em_user_service.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    public List<UserResponseDto> findAll(Pageable pageable) {
        List<User> users = userRepository.findAll(pageable);
        return users.stream().map(mapper::toDto).toList();
    }

    public UserResponseDto findById(UUID id) {
        User user = loadUser(id);
        return mapper.toDto(user);
    }

    private User loadUser(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public boolean existsByName(String name) {
        return userRepository.existsByName(name);
    }

    @Transactional(readOnly = false)
    public UserResponseDto saveUser(UserRequestDto userRequestDto) {
        return save(userRequestDto, "USER");
    }

    @Transactional(readOnly = false)
    public UserResponseDto saveAdmin(UserRequestDto userRequestDto) {
        return save(userRequestDto, "ADMIN");
    }

    private UserResponseDto save(UserRequestDto userRequestDto, String role) {
        User user = mapper.toUser(userRequestDto);
        user.setRoles(Set.of(loadRole(role)));
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        User savedUser = userRepository.save(user);
        return mapper.toDto(savedUser);
    }

    private Role loadRole(String role) {
        return roleRepository.findByName(role).orElseThrow(() -> new EntityNotFoundException("Role not found: " + role));
    }

    @Transactional(readOnly = false)
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }
}
