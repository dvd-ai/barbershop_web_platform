package com.app.barbershopweb.order.reservation.entity;

import java.util.List;
import java.util.Objects;

public class OrderFilters {

    private List<Long> barberIds;

    public OrderFilters() {
    }

    public OrderFilters(List<Long> barberIds) {
        this.barberIds = barberIds;
    }

    public List<Long> getBarberIds() {
        return barberIds;
    }

    public void setBarberIds(List<Long> barberIds) {
        this.barberIds = barberIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderFilters that = (OrderFilters) o;

        return Objects.equals(barberIds, that.barberIds);
    }

    @Override
    public int hashCode() {
        return barberIds != null ? barberIds.hashCode() : 0;
    }
}
