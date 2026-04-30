package com.barangkita.barang_kita.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "akun")
public abstract class Akun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_akun;

    private String email;
    private String password;

    public boolean login() {
        return true;
    }

    public void logout() {
        System.out.println("Logged out.");
    }

    public abstract String getRole();
}