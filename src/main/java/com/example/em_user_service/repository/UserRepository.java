package com.example.em_user_service.repository;

import com.example.em_user_service.data.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(idClass = UUID.class, domainClass = User.class)
public interface UserRepository {

    List<User> findAll(Pageable pageable);

    Optional<User> findById(UUID id);

    boolean existsByName(String name);

    void deleteById(UUID id);

    User save(User user);
}
