package com.ivanov.skblabform.controller;

import com.ivanov.skblabform.dao.VerificationStatus;
import com.ivanov.skblabform.user.UserDto;
import com.ivanov.skblabform.user.UserService;
import com.sun.net.httpserver.HttpServer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@Slf4j
@AllArgsConstructor
public class FormController {
    private final UserService userService;

    @GetMapping("/register")
    public String registrationForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "registration";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute UserDto userDto, Model model) {
        log.error(userDto.toString());
        model.addAttribute("userDto", new UserDto());
        userService.verificationUser(userDto);
        model.addAttribute("message", VerificationStatus.PROCESSING.getDescription());
        return "registration";
    }
}
