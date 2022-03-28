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

    public List<Order> getAvailableOrders(
            Long barbershopId, LocalDateTime dateToStartWeekFrom
    ) {
        return orderReservationRepository.getAvailableOrders(barbershopId, dateToStartWeekFrom);
    }

    public List<Order> getFilteredAvailableOrders(
            Long barbershopId, LocalDateTime dateToStartWeekFrom, OrderFilters orderFilters
    ) {
        if (!orderFilters.getBarberIds().isEmpty())
            return orderReservationRepository.getAvailableFilteredOrders(
                    barbershopId, dateToStartWeekFrom, orderFilters.getBarberIds()
            );

        else return orderReservationRepository.getAvailableOrders(barbershopId, dateToStartWeekFrom);
    }

    public List<Order> reserveCustomerOrders(List<Long> orderIds, Long customerId) {
        return orderReservationRepository.reserveOrdersByOrderIdsAndByCustomerId(orderIds, customerId);
    }
}
