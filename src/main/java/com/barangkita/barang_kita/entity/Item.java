package com.barangkita.barang_kita.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_item;

    private String nama_item;
    private double harga;
    private String deskripsi;
    private String gambar_item;
    private String adminUsername;

    public Item() {}

    public Item(String nama_item, double harga, String deskripsi, String gambar_item) {
        this.nama_item = nama_item;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.gambar_item = gambar_item;
    }

    public int getId_item() { return id_item; }
    public void setId_item(int id_item) { this.id_item = id_item; }

    public String getNama_item() { return nama_item; }
    public void setNama_item(String nama_item) { this.nama_item = nama_item; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getGambar_item() { return gambar_item; }
    public void setGambar_item(String gambar_item) { this.gambar_item = gambar_item; }

    @JsonIgnore
    public Item getItemDetails() {
        System.out.println("ID: " + id_item);
        System.out.println("Name: " + nama_item);
        System.out.println("Price: " + harga);
        System.out.println("Description: " + deskripsi);
        return this;
    }

    public String getAdminUsername() { return adminUsername; }
    public void setAdminUsername(String adminUsername) { this.adminUsername = adminUsername; }
}