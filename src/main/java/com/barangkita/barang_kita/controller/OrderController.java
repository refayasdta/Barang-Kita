package com.barangkita.barang_kita.controller;

import com.barangkita.barang_kita.entity.Item; // <-- NEW
import com.barangkita.barang_kita.entity.Order;
import com.barangkita.barang_kita.entity.OrderItem;
import com.barangkita.barang_kita.service.ItemService; // <-- NEW
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

    @Autowired
    private ItemService itemService; // <-- NEW: Allows us to edit item stock

    // User - get their own orders
    @GetMapping("/user/{id_user}")
    public List<Order> getOrderByUser(@PathVariable int id_user) {
        return orderService.getOrderByUser(id_user);
    }

    // User - create new order with items
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        order.createOrder();
        if (order.getStatus() == null) order.setStatus("PENDING");

        // Link each OrderItem back to this order
        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                item.setOrder(order);
                // Calculate subtotal if not set
                if (item.getSubtotal() == 0) {
                    item.setSubtotal(item.getHarga() * item.getJumlah());
                }

                // ═══ NEW: INVENTORY DEDUCTION LOGIC ═══
                // 1. Find the physical item in the database
                Item dbItem = itemService.getItemById(item.getId_item());
                if (dbItem != null) {
                    // 2. Get current stock (default to 1 if null)
                    int currentStock = dbItem.getStok() != null ? dbItem.getStok() : 1;
                    int purchasedQty = item.getJumlah();
                    
                    // 3. Deduct stock (Math.max prevents it from dropping below 0)
                    dbItem.setStok(Math.max(0, currentStock - purchasedQty));
                    
                    // 4. Save the updated stock back to the database
                    itemService.saveItem(dbItem);
                }
                // ══════════════════════════════════════
            }
        }

        Order saved = orderService.saveOrder(order);
        return ResponseEntity.ok(saved);
    }

    // User/Admin - get order by ID
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable int id) {
        return orderService.getOrderById(id);
    }

    // Admin - get ALL orders
    @GetMapping("/all")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Admin - confirm payment
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

    // Admin - cancel order
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

    // Admin - set back to pending
    @PutMapping("/{id}/pending")
    public ResponseEntity<?> pendingOrder(@PathVariable int id) {
        Order order = orderService.getOrderById(id);
        if (order == null) return ResponseEntity.notFound().build();
        order.setStatus("PENDING");
        orderService.updateOrder(order);
        return ResponseEntity.ok("Order #" + id + " set to pending!");
    }
}