package com.ivanov.skblabform.user;

import com.ivanov.skblabform.validator.Password;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserDto {
    @Size(min=5, max = 49)
    private String login;
    @Password
    private String password;
    @NotBlank
    private String email;
    @NotBlank
    private String name;
}
