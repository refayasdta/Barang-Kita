package com.barangkita.barang_kita.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnore // Prevents the JSON infinite loop when fetching carts
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private Integer quantity;
    private Double subtotal;

    // Constructors
    public CartItem() {}

    public CartItem(Cart cart, Item item, Integer quantity, Double subtotal) {
        this.cart = cart;
        this.item = item;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
}