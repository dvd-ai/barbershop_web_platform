package com.app.barbershopweb.order.reservation;

import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.reservation.entity.OrderFilters;
import com.app.barbershopweb.order.reservation.repository.OrderReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderReservationService {

    private final OrderReservationRepository orderReservationRepository;

    public OrderReservationService(OrderReservationRepository orderReservationRepository) {
        this.orderReservationRepository = orderReservationRepository;
    }

    public List<Order> getBarbershopActiveUnreservedOrdersForWeek(
            Long barbershopId, LocalDateTime dateToStartWeekFrom
    ) {
        return orderReservationRepository.getActiveUnreservedOrdersForWeekByBarbershopIdAndDate(barbershopId, dateToStartWeekFrom);
    }

    public List<Order> getBarbershopFilteredActiveUnreservedOrdersForWeek(
            Long barbershopId, LocalDateTime dateToStartWeekFrom, OrderFilters orderFilters
    ) {
        return orderReservationRepository.getActiveUnreservedOrdersForWeekByBarbershopIdAndDateAndBarberIds(
            barbershopId, dateToStartWeekFrom, orderFilters.getBarberIds()
        );
    }

    public List<Order> reserveCustomerOrders(List<Long> orderIds, Long customerId) {
        return orderReservationRepository.reserveOrdersByOrderIdsAndByCustomerId(orderIds, customerId);
    }
}
