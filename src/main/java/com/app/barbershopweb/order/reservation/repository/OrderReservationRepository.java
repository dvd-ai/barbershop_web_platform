package com.app.barbershopweb.order.reservation.repository;

import com.app.barbershopweb.order.crud.Order;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderReservationRepository {
    //gets active unreserved orders for week by barbershopId and date
    List<Order> getAvailableOrders(Long barbershopId, LocalDateTime dateToStartWeekFrom);

    //gets active unreserved orders for week by barbershopId, date, barberIds
    List<Order> getAvailableFilteredOrders(
            Long barbershopId, LocalDateTime dateToStartWeekFrom, List<Long>barberIds
    );
    Optional<Order> reserveOrderByOrderIdAndCustomerId(Long orderId, Long customerId);
    List<Order> reserveOrdersByOrderIdsAndByCustomerId(List<Long>orderIds, Long customerId);
}
