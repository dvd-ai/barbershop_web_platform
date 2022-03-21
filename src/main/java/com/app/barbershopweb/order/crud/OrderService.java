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
        //check date (barbershop should work on given date, date format (per hour))
        //check: customerId != barberId
        return orderRepository.addOrder(order);
    }

    public void deleteOrderByOrderId(Long id) {
        orderRepository.deleteOrderByOrderId(id);
    }

    public Optional<Order> updateOrder(Order order) {
        //check date (barbershop should work on given date, date format (per hour))
        //check: customerId != barberId
        return orderRepository.updateOrder(order);
    }

    public Optional<Order> findOrderByOrderId(Long id) {
        return orderRepository.findOrderByOrderId(id);
    }

    public List<Order> getOrders() {
        return orderRepository.getOrders();
    }

    private void checkOrderFormat(Order order) {

    }
}
