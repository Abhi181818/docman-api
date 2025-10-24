package com.abhi.docman.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.abhi.docman.model.User;
import com.abhi.docman.model.UserRole;
import com.abhi.docman.repo.UserRepo;
import com.abhi.docman.util.JwtUtil;

import lombok.AllArgsConstructor;
import lombok.Data;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Email and password are required"));
        }

        User user = userRepo.findByEmail(request.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid credentials"));
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid credentials"));
        }

        String token = jwtUtil.generateToken(user.getEmail());
        AuthenticatorResponse response = new AuthenticatorResponse(token, user.getEmail(), user.getFirstName());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Email and password are required"));
        }

        if (userRepo.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("User with this email already exists"));
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(UserRole.USER);

        userRepo.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        AuthenticatorResponse response = new AuthenticatorResponse(token, user.getEmail(), user.getFirstName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

@Data
class LoginRequest {
    private String email;
    private String password;
}

@Data
class RegisterRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}

@Data
@AllArgsConstructor
class AuthenticatorResponse {
    private String token;
    private String email;
    private String firstName;
}

@Data
@AllArgsConstructor
class ErrorResponse {
    private String message;

}