package com.barangkita.barang_kita.service;

import com.barangkita.barang_kita.entity.Cart;
import com.barangkita.barang_kita.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public List<Cart> getCartByUser(int id_user) {
    return cartRepository.findByUserId(id_user);
    }

    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public void deleteCart(int id) {
        cartRepository.deleteById(id);
    }
}