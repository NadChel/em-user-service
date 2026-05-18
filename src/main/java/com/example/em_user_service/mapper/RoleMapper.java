package com.example.em_user_service.mapper;

import com.example.em_user_service.data.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    default String toName(Role role) {
        return role != null ? role.getName() : null;
    }
}
