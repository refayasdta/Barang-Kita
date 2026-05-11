package com.barangkita.barang_kita.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore // Prevents the JSON infinite loop when fetching orders
    private Order order;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private Integer quantity;
    
    @Column(name = "harga_satuan")
    private Double hargaSatuan; // Price at the time of purchase
    
    private Double subtotal;

    // Constructors
    public OrderItem() {}

    public OrderItem(Order order, Item item, Integer quantity, Double hargaSatuan, Double subtotal) {
        this.order = order;
        this.item = item;
        this.quantity = quantity;
        this.hargaSatuan = hargaSatuan;
        this.subtotal = subtotal;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getHargaSatuan() { return hargaSatuan; }
    public void setHargaSatuan(Double hargaSatuan) { this.hargaSatuan = hargaSatuan; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
}