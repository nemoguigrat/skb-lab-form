package com.ivanov.skblabform.dao;

import com.ivanov.skblabform.user.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "name")
    private String name;

    public User(UserDto userDto) {
        this.login = userDto.getLogin();
        this.email = userDto.getEmail();
        this.name = userDto.getName();
        this.password = userDto.getPassword();
    }

    public User(String login, String encodedPassword, String email, String name) {
        this.login = login;
        this.password = encodedPassword;
        this.email = email;
        this.name = name;
    }
}
