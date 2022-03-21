package com.app.barbershopweb.order.reservation.repository;

import com.app.barbershopweb.order.crud.Order;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderReservationRepository {

    List<Order>getActiveUnreservedOrdersByBarbershopIdAndWeek(Long barbershopId, LocalDateTime week);
    List<Order> getActiveUnreservedOrdersByBarbershopIdAndWeekAndBarberIds(
            Long barbershopId, LocalDateTime week, List<Long>barberIds
    );
    Order reserveOrderByCustomerId(Long orderId, Long customerId);
    List<Order>reserveOrdersByCustomerId(List<Long>orderIds, Long customerId);
}
