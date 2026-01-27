package com.mz.userserviceauthentication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "backend is live!";
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
