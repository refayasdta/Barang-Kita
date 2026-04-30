package com.barangkita.barang_kita.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
public class User extends Akun {

    private String nama;
    private String foto_profile;
    private String no_telepon;
    private String alamat;

    @Override
    public String getRole() {
        return "User";
    }

    public void register() {
        System.out.println("User " + nama + " registered.");
    }

    public void editProfile() {
        System.out.println("Profile updated for " + nama);
    }

    public List<String> searchItem(String keyword) {
        List<String> results = new java.util.ArrayList<>();
        results.add("Item matching: " + keyword);
        return results;
    }

    public List<String> viewOrderHistory() {
        List<String> history = new java.util.ArrayList<>();
        history.add("Order history for: " + nama);
        return history;
    }
}