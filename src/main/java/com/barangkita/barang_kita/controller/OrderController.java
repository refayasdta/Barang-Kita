package com.barangkita.barang_kita.controller;

import com.barangkita.barang_kita.entity.Order;
import com.barangkita.barang_kita.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/all")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<?> confirmOrder(@PathVariable int id) {
        Order order = orderService.getOrderById(id);
        
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found!");
        }

        order.setStatus("CONFIRMED");
        orderService.updateOrder(order);
        return ResponseEntity.ok("Order #" + id + " confirmed successfully!");
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable int id) {
        Order order = orderService.getOrderById(id);

        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found!");
        }

        order.setStatus("CANCELLED");
        orderService.updateOrder(order);
        return ResponseEntity.ok("Order #" + id + " cancelled!");
    }
}