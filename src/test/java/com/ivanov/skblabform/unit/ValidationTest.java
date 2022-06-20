package com.ivanov.skblabform.unit;

import com.ivanov.skblabform.user.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.validation.*;
import java.util.Set;

public class ValidationTest {
    private Validator validator;

    @BeforeEach
    public void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest
    @CsvSource({"AAAAAAa9", "bbbbbbA9", "123456Af", "AbcdAb907Triel", "aa970KLpiue6658"})
    public void validateUserDto_CORRECT_PASSWORD(String password) {
        UserDto userDto = new UserDto();
        userDto.setEmail("test");
        userDto.setLogin("login");
        userDto.setName("name");
        userDto.setPassword(password);
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        Assertions.assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({"0", "a1", "ab2", "abgrtyo", "abcdefrt", "AAAAAAAA", "12345678", "Abcdef9"})
    public void validateUserDto_INVALID_PASSWORD(String password) {
        UserDto userDto = new UserDto();
        userDto.setEmail("test");
        userDto.setLogin("login");
        userDto.setName("name");
        userDto.setPassword(password);
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        Assertions.assertFalse(violations.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({"0", "a1", "ab2", "abg-"})
    public void validateUserDto_LOGIN_LENGTH_LESS(String login) {
        UserDto userDto = new UserDto();
        userDto.setEmail("test");
        userDto.setLogin(login);
        userDto.setName("name");
        userDto.setPassword("aa970KLpiue6658");
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void validateUserDto_BLANK() {
        UserDto userDto = new UserDto();
        userDto.setPassword("aa970KLpiue6658");
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        Assertions.assertFalse(violations.isEmpty());
        System.out.println(violations.toString());
    }
}
