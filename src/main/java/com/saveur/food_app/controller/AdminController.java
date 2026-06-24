package com.saveur.food_app.controller;

import com.saveur.food_app.model.User;
import com.saveur.food_app.model.UserRole;
import com.saveur.food_app.model.Order;
import com.saveur.food_app.repository.UserRepository;
import com.saveur.food_app.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderService orderService;

    // Get all users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Change user role
    @PatchMapping("/users/{id}/role")
    public User changeRole(@PathVariable Long id, @RequestBody Map<String, String> req) {
        User user = userRepository.findById(id).orElseThrow();
        user.setRole(UserRole.valueOf(req.get("role")));
        return userRepository.save(user);
    }

    // Delete user
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    // Get dashboard stats
    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        List<Order> allOrders = orderService.getAllOrders();
        List<User> allUsers = userRepository.findAll();

        double totalRevenue = allOrders.stream()
            .mapToDouble(o -> o.getTotal() != null ? o.getTotal() : 0)
            .sum();

        long deliveredOrders = allOrders.stream()
            .filter(o -> o.getStage() != null &&
                o.getStage().name().equals("DELIVERED"))
            .count();

        long activeOrders = allOrders.stream()
            .filter(o -> o.getStage() != null &&
                !o.getStage().name().equals("DELIVERED"))
            .count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRevenue", totalRevenue);
        stats.put("totalOrders", allOrders.size());
        stats.put("deliveredOrders", deliveredOrders);
        stats.put("activeOrders", activeOrders);
        stats.put("totalUsers", allUsers.size());
        stats.put("totalCustomers", allUsers.stream()
            .filter(u -> u.getRole() == UserRole.CUSTOMER).count());
        return stats;
    }
}