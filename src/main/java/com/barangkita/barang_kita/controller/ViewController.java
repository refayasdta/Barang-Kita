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
}