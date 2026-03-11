package com.interviewprep;

import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody RegisterRequest request) {

        Map<String, String> response = new HashMap<>();

        // check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            response.put("message", "Email already registered");
            return response;
        }

        // check password match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            response.put("message", "Passwords do not match");
            return response;
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole("USER");
        user.setVerified(false);

        userRepository.save(user);

        response.put("message", "User registered successfully");

        return response;
    }
}