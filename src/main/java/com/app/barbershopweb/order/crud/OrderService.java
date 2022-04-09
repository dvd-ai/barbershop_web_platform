package com.app.barbershopweb.order.crud;

import com.app.barbershopweb.order.crud.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Long addOrder(Order order) {
        return orderRepository.addOrder(order);
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteOrder(orderId);
    }

    public Optional<Order> updateOrder(Order order) {
        return orderRepository.updateOrder(order);
    }

    public Optional<Order> findOrder(Long orderId) {
        return orderRepository.findOrder(orderId);
    }

    public List<Order> getOrders() {
        return orderRepository.getOrders();
    }
}
