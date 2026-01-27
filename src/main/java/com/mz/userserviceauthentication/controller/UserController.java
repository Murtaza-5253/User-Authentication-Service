package com.mz.userserviceauthentication.controller;

import com.mz.userserviceauthentication.dto.LoginRequest;
import com.mz.userserviceauthentication.dto.UserRequest;
import com.mz.userserviceauthentication.dto.UserResponse;
import com.mz.userserviceauthentication.model.User;
import com.mz.userserviceauthentication.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponse createUser(@Valid @RequestBody UserRequest user) {
        return userService.createuser(user);
    }

    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }
}
