package com.barangkita.barang_kita.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // This tells Spring to look for login.html
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/item-detail")
    public String itemDetail() {
        return "item-detail";
    }

    @GetMapping("/admin-dashboard")
    public String adminDashboard() {
        return "admin-dashboard";
    }

    @GetMapping("/cart")
    public String cart() {
        return "cart";
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "checkout";
    }

    @GetMapping("/admin-orders")
    public String adminOrders() {
        return "admin-orders";
    }

    @GetMapping("/profile")
    public String profile() { return "profile"; }

    @GetMapping("/edit-profile")
    public String editProfile() { return "edit-profile"; }

    @GetMapping("/order-history")
    public String orderHistory() { 
        return "order-history"; 
    }

    @GetMapping("/tentang-kami")
    public String tentangKami() { 
        return "tentang-kami"; 
    }

    @GetMapping("/final-checkout")
    public String finalCheckout() { 
        return "final-checkout"; 
    }
}