package com.barangkita.barang_kita.controller;

import com.barangkita.barang_kita.dto.AuthRequest;
import com.barangkita.barang_kita.dto.AuthResponse;
import com.barangkita.barang_kita.entity.Akun;
import com.barangkita.barang_kita.entity.Admin;
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

    // 1. REGISTER USER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User newUser) {
        if (akunRepository.findByEmail(newUser.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already taken!");
        }
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRole("User");
        akunRepository.save(newUser);
        return ResponseEntity.ok("User registered successfully!");
    }

    // 2. REGISTER ADMIN
    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody Admin newAdmin) {
        if (akunRepository.findByEmail(newAdmin.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already taken!");
        }
        newAdmin.setPassword(passwordEncoder.encode(newAdmin.getPassword()));
        newAdmin.setRole("Admin");
        akunRepository.save(newAdmin);
        return ResponseEntity.ok("Admin registered successfully!");
    }

    // 3. LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        
        Akun akun = akunRepository.findByEmail(request.getEmail()).orElse(null);
        
        if (akun == null) {
            System.out.println("DEBUG: No account found in DB for email: " + request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
        
        boolean isMatch = passwordEncoder.matches(request.getPassword(), akun.getPassword());
        System.out.println("DEBUG: Password match result: " + isMatch);
        
        if (isMatch) {
            String token = jwtUtil.generateToken(akun.getEmail(), akun.getRole(), akun.getId_akun());
            return ResponseEntity.ok(new AuthResponse(token, akun.getRole()));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }

    @GetMapping("/force-reset")
    public String forceReset() {
        Akun akun = akunRepository.findByEmail("refayasiddharta@gmail.com").orElse(null);
        if (akun != null) {
            akun.setPassword(passwordEncoder.encode("password123"));
            akunRepository.save(akun);
            return "Password successfully reset to password123 using the active PasswordEncoder!";
        }
        return "Account not found!";
    }
}