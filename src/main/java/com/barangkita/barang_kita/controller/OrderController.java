package com.barangkita.barang_kita.controller;

import com.barangkita.barang_kita.entity.Order;
import com.barangkita.barang_kita.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/user/{id_user}")
    public List<Order> getOrderByUser(@PathVariable int id_user) {
        return orderService.getOrderByUser(id_user);
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderService.saveOrder(order);
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable int id) {
        return orderService.getOrderById(id);
    }
}