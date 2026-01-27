package com.mz.userserviceauthentication.service;

import com.mz.userserviceauthentication.dto.LoginRequest;
import com.mz.userserviceauthentication.dto.UserRequest;
import com.mz.userserviceauthentication.dto.UserResponse;
import com.mz.userserviceauthentication.model.User;
import com.mz.userserviceauthentication.repository.UserRepository;
import com.mz.userserviceauthentication.security.JWTUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UserResponse createuser(UserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        String hashedPassword = bCryptPasswordEncoder.encode(request.getPassword());
        User newUser = new User(request.getName(), request.getEmail(),hashedPassword);
        User saved = userRepository.save(newUser);
        return new UserResponse(
                saved.getId(),
                saved.getName(),
                saved.getEmail()
        );
    }

    public Map<String, String> login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("Invalid Credentials"));
        if(!bCryptPasswordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new RuntimeException("Invalid Password");
        }
        String token  = JWTUtil.generateToken(user.getEmail());

        return Map.of("token", token);
    }
}
