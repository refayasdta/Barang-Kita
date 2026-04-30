package com.barangkita.barang_kita.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "orders")
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
}