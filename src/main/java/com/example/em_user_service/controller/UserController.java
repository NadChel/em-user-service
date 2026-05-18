package com.example.em_user_service.controller;

import com.example.em_user_service.data.dto.request.UserRequestDto;
import com.example.em_user_service.data.dto.response.UserResponseDto;
import com.example.em_user_service.service.UserService;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
@SecurityScheme(type = SecuritySchemeType.HTTP,
        name = "bearer-key",
        scheme = "bearer", bearerFormat = "JWT")
public class UserController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll(@ParameterObject @PageableDefault Pageable pageable) {
        List<UserResponseDto> users = service.findAll(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable UUID id) {
        UserResponseDto user = service.findById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> saveUser(@RequestBody UserRequestDto userRequestDto) {
        UserResponseDto savedUserResponseDto = service.saveUser(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserResponseDto);
    }

    @PostMapping("/admins")
    public ResponseEntity<UserResponseDto> saveAdmin(@RequestBody UserRequestDto userRequestDto) {
        UserResponseDto savedUserResponseDto = service.saveAdmin(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDto> delete(@PathVariable UUID id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
