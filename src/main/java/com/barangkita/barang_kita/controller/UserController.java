package com.barangkita.barang_kita.controller;

import com.barangkita.barang_kita.entity.User;
import com.barangkita.barang_kita.entity.Akun;
import com.barangkita.barang_kita.repository.AkunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.UUID;
import com.barangkita.barang_kita.entity.Admin;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired // <-- CRITICAL: Re-added this so the database connects
    private AkunRepository akunRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        return akunRepository.findById(id)
            .map(u -> ResponseEntity.ok(u))
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable int id, 
            @RequestParam(value = "nama", required = false) String nama,
            @RequestParam(value = "no_telepon", required = false) String noTelepon,
            @RequestParam(value = "alamat", required = false) String alamat,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "file", required = false) MultipartFile file) {
            
        return akunRepository.findById(id).map(akun -> {
            
            // If the account is a regular User
            if (akun instanceof User) {
                User user = (User) akun;
                if (nama != null) user.setNama(nama);
                if (noTelepon != null) user.setNo_telepon(noTelepon);
                if (alamat != null) user.setAlamat(alamat);

                if (file != null && !file.isEmpty()) {
                    try {
                        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                        Path uploadPath = Paths.get("src/main/resources/static/images/");
                        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                        Path filePath = uploadPath.resolve(fileName);
                        Files.copy(file.getInputStream(), filePath);
                        user.setFoto_profile(fileName);
                    } catch (IOException e) {
                        return ResponseEntity.internalServerError().build();
                    }
                }
                akunRepository.save(user);
                return ResponseEntity.ok((Object) user);
            } 
            // If the account is an Admin
            else if (akun instanceof Admin) {
                Admin admin = (Admin) akun;
                if (username != null) admin.setUsername(username);
                akunRepository.save(admin);
                return ResponseEntity.ok((Object) admin);
            }
            
            return ResponseEntity.badRequest().body(Map.of("error", "Tipe akun tidak valid."));
            
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable int id, @RequestBody Map<String, String> payload) {
        String oldPassword = payload.get("oldPassword");
        String newPassword = payload.get("newPassword");

        // 1. Find the account (using Akun instead of User)
        Akun akun = akunRepository.findById(id).orElse(null); 
        if (akun == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User tidak ditemukan"));
        }

        // 2. Verify the old password using BCrypt
        if (!passwordEncoder.matches(oldPassword, akun.getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Password lama salah!"));
        }

        // 3. Hash the new password and save it
        akun.setPassword(passwordEncoder.encode(newPassword));
        akunRepository.save(akun);

        return ResponseEntity.ok(Map.of("message", "Password berhasil diubah!"));
    }
}