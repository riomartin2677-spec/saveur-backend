package com.saveur.food_app.controller;

import com.saveur.food_app.model.Order;
import com.saveur.food_app.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000", "https://saveur-frontend.vercel.app"})
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public Order placeOrder(@RequestBody Order order, Authentication auth) {
        if (auth != null) {
            order.setCustomerEmail(auth.getName());
        }
        return orderService.placeOrder(order);
    }

    @PatchMapping("/orders/{id}/stage")
    public Order advanceStage(@PathVariable Long id) {
        return orderService.advanceStage(id);
    }

    @GetMapping("/orders/active")
    public List<Order> getActiveOrders() {
        return orderService.getActiveOrders();
    }

    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/orders/my")
    public List<Order> getMyOrders(Authentication auth) {
        return orderService.getOrdersByEmail(auth.getName());
    }
}