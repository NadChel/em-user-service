package com.example.em_user_service.repository;

import com.example.em_user_service.data.entity.Role;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(idClass = UUID.class, domainClass = Role.class)
public interface RoleRepository {

    Optional<Role> findByName(String name);
}
