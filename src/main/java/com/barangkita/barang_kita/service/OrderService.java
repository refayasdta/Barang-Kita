package com.barangkita.barang_kita.service;

import com.barangkita.barang_kita.entity.Order;
import com.barangkita.barang_kita.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getOrderByUser(int id_user) {
    return orderRepository.findByUserId(id_user);
    }

    public Order saveOrder(Order order) {
        order.createOrder();
        return orderRepository.save(order);
    }

    public Order getOrderById(int id) {
        return orderRepository.findById(id).orElse(null);
    }
}