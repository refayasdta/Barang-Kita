package com.barangkita.barang_kita.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "admins")
public class Admin extends Akun {

    private String username;

    @Override
    public String getRole() {
        return "Admin";
    }

    public void uploadNewItem(Item item) {
        System.out.println("Admin " + username + " uploading: " + item.getNama_item());
    }

    public void manageItems() {
        System.out.println("Admin " + username + " managing items.");
    }
}