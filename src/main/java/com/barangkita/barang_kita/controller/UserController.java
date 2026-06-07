package com.barangkita.barang_kita.controller;

import com.barangkita.barang_kita.entity.User;
import com.barangkita.barang_kita.repository.AkunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private AkunRepository akunRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        return akunRepository.findById(id)
            .map(u -> ResponseEntity.ok(u))
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody User updated) {
        return akunRepository.findById(id).map(akun -> {
            User user = (User) akun;
            user.setNama(updated.getNama());
            user.setNo_telepon(updated.getNo_telepon());
            user.setAlamat(updated.getAlamat());
            akunRepository.save(user);
            return ResponseEntity.ok((Object) user);
        }).orElse(ResponseEntity.notFound().build());
    }
}