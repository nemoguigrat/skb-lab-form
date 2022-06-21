package com.ivanov.skblabform.exception;

import com.ivanov.skblabform.user.UserDto;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class FormExceptionHandler {
    @ExceptionHandler(UserExistsException.class)
    public String handleUserExistsException(UserExistsException ex, Model model) {
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("exceptions", List.of(ex.getMessage()));
        return "registration";
    }

    @ExceptionHandler(BindException.class)
    public String handleRestValidationException(BindException ex, Model model) {
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("exceptions", ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()));
        // если какие-то проблемы с валидацией, то очищаем полностью форму и говорим, какие поля были заполнены не верно.
        return "registration";
    }
}
