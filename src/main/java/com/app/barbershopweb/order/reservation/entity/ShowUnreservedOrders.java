package com.app.barbershopweb.order.reservation.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class ShowUnreservedOrders {
    private Long barbershopId;
    private LocalDateTime reservationDateToStartWeekFrom;
    private OrderFilters orderFilters;

    public ShowUnreservedOrders(Long barbershopId,
                                LocalDateTime reservationDateToStartWeekFrom,
                                OrderFilters orderFilters) {
        this.barbershopId = barbershopId;
        this.reservationDateToStartWeekFrom = reservationDateToStartWeekFrom;
        this.orderFilters = orderFilters;
    }

    public ShowUnreservedOrders() {
    }

    public Long getBarbershopId() {
        return barbershopId;
    }

    public void setBarbershopId(Long barbershopId) {
        this.barbershopId = barbershopId;
    }

    public LocalDateTime getReservationDateToStartWeekFrom() {
        return reservationDateToStartWeekFrom;
    }

    public void setReservationDateToStartWeekFrom(LocalDateTime reservationDateToStartWeekFrom) {
        this.reservationDateToStartWeekFrom = reservationDateToStartWeekFrom;
    }

    public OrderFilters getOrderFilters() {
        return orderFilters;
    }

    public void setOrderFilters(OrderFilters orderFilters) {
        this.orderFilters = orderFilters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShowUnreservedOrders that = (ShowUnreservedOrders) o;

        if (!Objects.equals(barbershopId, that.barbershopId)) return false;
        if (!Objects.equals(reservationDateToStartWeekFrom, that.reservationDateToStartWeekFrom))
            return false;
        return Objects.equals(orderFilters, that.orderFilters);
    }

    @Override
    public int hashCode() {
        int result = barbershopId != null ? barbershopId.hashCode() : 0;
        result = 31 * result + (reservationDateToStartWeekFrom != null ? reservationDateToStartWeekFrom.hashCode() : 0);
        result = 31 * result + (orderFilters != null ? orderFilters.hashCode() : 0);
        return result;
    }
}
