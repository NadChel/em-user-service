package com.example.em_user_service.data.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRequestDto {

    @NotBlank
    private String name;
    @NotBlank
    @Size(min = 4, message = "The password must be at least 4 characters long")
    private String password;
}
