package com.ivanov.skblabform.user;

import com.ivanov.skblabform.validator.Password;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class UserDto implements Serializable {
    @Size(min=5, max = 49, message = "Login must be at least 5 characters")
    private String login;
    @Password(message = "Incorrect password! Length not less than 8." +
            " Password must contain at least one [a-z], [0-9], [A-Z]. No special characters.")
    private String password;
    @NotBlank(message = "Email field must not be empty")
    private String email;
    @NotBlank(message = "Name field must not be empty")
    private String name;
}
