package com.barangkita.barang_kita.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    private int id_item;
    private String nama_item;
    private int jumlah;
    private double harga;
    private double subtotal;

    // ── Constructors ──
    public OrderItem() {}

    public OrderItem(Order order, int id_item, String nama_item, int jumlah, double harga) {
        this.order = order;
        this.id_item = id_item;
        this.nama_item = nama_item;
        this.jumlah = jumlah;
        this.harga = harga;
        this.subtotal = harga * jumlah;
    }

    // ── Getters & Setters ──
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public int getId_item() { return id_item; }
    public void setId_item(int id_item) { this.id_item = id_item; }

    public String getNama_item() { return nama_item; }
    public void setNama_item(String nama_item) { this.nama_item = nama_item; }

    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}