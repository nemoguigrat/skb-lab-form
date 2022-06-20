package com.ivanov.skblabform.user;

import com.ivanov.skblabform.validator.Password;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserDto {
    @Size(min=5, max = 49)
    private String login;
    @Password(message = "Incorrect password! Length not less than 8." +
            " Password must contain at least one [a-z], [0-9], [A-Z]. No special characters.")
    private String password;
    @NotBlank
    private String email;
    @NotBlank
    private String name;
}
