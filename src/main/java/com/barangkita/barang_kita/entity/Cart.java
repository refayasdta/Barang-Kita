package com.barangkita.barang_kita.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_cart;
    private int jumlah;
    private int id_item;

    private int id_user;
    private double total_harga;

    public void addToCart(Item item, int jumlah) {
        this.total_harga += item.getHarga() * jumlah;
        System.out.println(jumlah + "x " + item.getNama_item() + " added to cart.");
    }

    public void removeFromCart() {
        System.out.println("Item removed from cart.");
        this.total_harga = 0;
    }

    public double calculateTotal() {
        System.out.println("Total: " + total_harga);
        return total_harga;
    }

    public Order proceedToPayment() {
        Order order = new Order();
        order.createOrder();
        return order;
    }

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    // Add these Getters and Setters to the bottom of the file
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
}
