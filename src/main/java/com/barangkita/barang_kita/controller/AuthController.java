package com.barangkita.barang_kita.controller;

import com.barangkita.barang_kita.dto.AuthRequest;
import com.barangkita.barang_kita.dto.AuthResponse;
import com.barangkita.barang_kita.entity.Akun;
import com.barangkita.barang_kita.entity.User;
import com.barangkita.barang_kita.repository.AkunRepository;
import com.barangkita.barang_kita.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AkunRepository akunRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. REGISTRATION ENDPOINT
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User newUser) {
        // Check if email already exists
        if (akunRepository.findByEmail(newUser.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already taken!");
        }

        // Hash the password before saving to the database
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        
        // Ensure role is set (assuming you have a setRole or default role logic)
        newUser.setRole("USER"); 

        akunRepository.save(newUser);
        return ResponseEntity.ok("User registered successfully!");
    }

    // 2. LOGIN ENDPOINT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        // Find the user by email
        Akun akun = akunRepository.findByEmail(request.getEmail()).orElse(null);

        // Check if user exists AND if the passwords match
        if (akun != null && passwordEncoder.matches(request.getPassword(), akun.getPassword())) {
            
            // Passwords match! Generate the token.
            String token = jwtUtil.generateToken(akun.getEmail(), akun.getRole());
            
            // Return the token and the role to the frontend/Postman
            return ResponseEntity.ok(new AuthResponse(token, akun.getRole()));
        }

        // If email not found or password incorrect
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }
}