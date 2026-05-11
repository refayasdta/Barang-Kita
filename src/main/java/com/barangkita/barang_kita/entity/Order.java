package com.barangkita.barang_kita.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "pesanan")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_order;

    private int id_user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date tanggal_order;

    private double total_pembayaran;

    public void createOrder() {
        this.tanggal_order = new Date();
        System.out.println("Order #" + id_order + " created on " + tanggal_order);
    }

    public String getOrderDetails() {
        return "Order ID: " + id_order +
               " | User: " + id_user +
               " | Date: " + tanggal_order +
               " | Total: " + total_pembayaran;
    }

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    // Add these Getters and Setters to the bottom of the file
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}