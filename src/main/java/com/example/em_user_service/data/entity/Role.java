package com.example.em_user_service.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Role {

    @Id
    private UUID id;
    private String name;
    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public static Role forName(String name) {
        Role role = new Role();
        role.setName(name);
        return role;
    }
}
