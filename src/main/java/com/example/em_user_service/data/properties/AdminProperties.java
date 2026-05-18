package com.example.em_user_service.data.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.admin")
public class AdminProperties {

    private String username;
    private String password;
}
