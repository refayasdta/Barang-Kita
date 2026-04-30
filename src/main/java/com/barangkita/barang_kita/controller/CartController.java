package com.barangkita.barang_kita.controller;

import com.barangkita.barang_kita.entity.Cart;
import com.barangkita.barang_kita.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/user/{id_user}")
    public List<Cart> getCartByUser(@PathVariable int id_user) {
        return cartService.getCartByUser(id_user);
    }

    @PostMapping
    public Cart createCart(@RequestBody Cart cart) {
        return cartService.saveCart(cart);
    }

    @DeleteMapping("/{id}")
    public String deleteCart(@PathVariable int id) {
        cartService.deleteCart(id);
        return "Cart deleted successfully";
    }
}