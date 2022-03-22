package com.app.barbershopweb.order.reservation.entity;

import java.util.List;
import java.util.Objects;

public class OrderReservation {

    private List<Long> orderIds;
    private Long customerId;

    public OrderReservation() {
    }

    public OrderReservation(List<Long> orderIds, Long customerId) {
        this.orderIds = orderIds;
        this.customerId = customerId;
    }

    public List<Long> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<Long> orderIds) {
        this.orderIds = orderIds;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderReservation that = (OrderReservation) o;

        if (!Objects.equals(orderIds, that.orderIds)) return false;
        return Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        int result = orderIds != null ? orderIds.hashCode() : 0;
        result = 31 * result + (customerId != null ? customerId.hashCode() : 0);
        return result;
    }
}
