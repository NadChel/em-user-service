package com.example.em_user_service.mapper;

import com.example.em_user_service.data.dto.request.UserRequestDto;
import com.example.em_user_service.data.dto.response.UserResponseDto;
import com.example.em_user_service.data.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    User toUser(UserRequestDto userRequestDto);

    UserResponseDto toDto(User user);
}
