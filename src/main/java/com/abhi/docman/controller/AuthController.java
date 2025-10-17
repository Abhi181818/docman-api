package com.abhi.docman.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abhi.docman.model.User;
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
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            String token = jwtUtil.generateToken(request.getEmail());
            User user = userRepo.findByEmail(request.getEmail());

            AuthenticatorResponse response = new AuthenticatorResponse(token, user.getEmail(), user.getFirstName());
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        }
    }
}

@Data
class LoginRequest {
    private String email;
    private String password;
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
    private LocalDateTime timestamp = LocalDateTime.now();

}