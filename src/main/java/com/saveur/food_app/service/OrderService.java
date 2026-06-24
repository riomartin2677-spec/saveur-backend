package com.saveur.food_app.service;

import com.saveur.food_app.model.Order;
import com.saveur.food_app.model.OrderStage;
import com.saveur.food_app.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Order placeOrder(Order order) {
        order.setStage(OrderStage.PLACED);
        Order saved = orderRepository.save(order);
        messagingTemplate.convertAndSend("/topic/kitchen", saved);
        return saved;
    }

    public Order advanceStage(Long id) {
        Order order = orderRepository.findById(id).orElseThrow();
        order.setStage(order.getStage().next());
        Order saved = orderRepository.save(order);
        messagingTemplate.convertAndSend("/topic/order/" + id, saved);
        return saved;
    }

    public List<Order> getActiveOrders() {
        return orderRepository.findByStageNot(OrderStage.DELIVERED);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Order> getOrdersByEmail(String email) {
        return orderRepository.findByCustomerEmailOrderByCreatedAtDesc(email);
    }
}