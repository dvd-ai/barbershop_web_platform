package com.app.barbershopweb.order.reservation.repository;

import com.app.barbershopweb.order.crud.Order;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderReservationRepository {

    List<Order>getActiveUnreservedOrdersForWeekByBarbershopIdAndDate(Long barbershopId, LocalDateTime dateToStartWeekFrom);
    List<Order> getActiveUnreservedOrdersForWeekByBarbershopIdAndDateAndBarberIds(
            Long barbershopId, LocalDateTime dateToStartWeekFrom, List<Long>barberIds
    );
    Optional<Order> reserveOrderByOrderIdAndCustomerId(Long orderId, Long customerId);
    List<Optional<Order>> reserveOrdersByOrderIdsAndByCustomerId(List<Long>orderIds, Long customerId);
}
