package com.barangkita.barang_kita.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // This tells Spring to look for register.html
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // This tells Spring to look for login.html
    }
}