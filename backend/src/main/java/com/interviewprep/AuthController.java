package com.interviewprep;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {

        // check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "Email already registered";
        }

        // check password match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return "Passwords do not match";
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        // ENCRYPT PASSWORD
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(Role.USER);
        user.setVerified(false);

        userRepository.save(user);

        return "User registered successfully";
    }
}