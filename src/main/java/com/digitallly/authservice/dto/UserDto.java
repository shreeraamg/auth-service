package com.digitallly.authservice.dto;

import com.digitallly.authservice.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private String id;
    private String name;
    private String email;
    private String password;
    private UserRole role;
    private boolean isPasswordChanged;

}
