package com.interviewprep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {


@Autowired
private UserRepository userRepository;

@Autowired
private PasswordEncoder passwordEncoder;

@Autowired
private EmailService emailService;

@Autowired
private OtpService otpService;

@Autowired
private JwtService jwtService;

@PostMapping("/register")
public String register(@RequestBody RegisterRequest request) {

    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
        return "Email already registered";
    }

    if (!request.getPassword().equals(request.getConfirmPassword())) {
        return "Passwords do not match";
    }

    User user = new User();

    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(Role.USER);
    user.setVerified(false);

    userRepository.save(user);

    String otp = otpService.generateOtp(user.getEmail());
    emailService.sendOtpEmail(user.getEmail(), otp);

    return "User registered successfully. OTP sent to email.";
}

@PostMapping("/verify-otp")
public String verifyOtp(@RequestBody VerifyOtpRequest request) {

    boolean valid = otpService.verifyOtp(request.getEmail(), request.getOtp());

    if (!valid) {
        return "Invalid OTP";
    }

    Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

    if (optionalUser.isPresent()) {

        User user = optionalUser.get();
        user.setVerified(true);
        userRepository.save(user);

        return "Account verified successfully";
    }

    return "User not found";
}

@PostMapping("/login")
public String login(@RequestBody LoginRequest request) {

    Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

    if (optionalUser.isEmpty()) {
        return "User not found";
    }

    User user = optionalUser.get();

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        return "Invalid password";
    }

    if (!user.isVerified()) {
        return "Please verify your email before login";
    }

    String token = jwtService.generateToken(user.getEmail());

    return token;
}


}
