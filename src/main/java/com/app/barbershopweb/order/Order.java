package com.app.barbershopweb.order;

import java.time.LocalDateTime;
import java.util.Objects;

public class Order {

    private Long orderId;
    private Long barbershopId;
    private Long barberId;
    private Long customerId;
    private LocalDateTime orderDate;
    private Boolean active;

    public Order() {
    }

    public Order(Long orderId, Long barbershopId,
                 Long barberId, Long customerId,
                 LocalDateTime orderDate, Boolean active) {

        this.orderId = orderId;
        this.barbershopId = barbershopId;
        this.barberId = barberId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.active = active;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getBarbershopId() {
        return barbershopId;
    }

    public void setBarbershopId(Long barbershopId) {
        this.barbershopId = barbershopId;
    }

    public Long getBarberId() {
        return barberId;
    }

    public void setBarberId(Long barberId) {
        this.barberId = barberId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (!Objects.equals(orderId, order.orderId)) return false;
        if (!Objects.equals(barbershopId, order.barbershopId)) return false;
        if (!Objects.equals(barberId, order.barberId)) return false;
        if (!Objects.equals(customerId, order.customerId)) return false;
        if (!Objects.equals(orderDate, order.orderDate)) return false;
        return Objects.equals(active, order.active);
    }

    @Override
    public int hashCode() {
        int result = orderId != null ? orderId.hashCode() : 0;
        result = 31 * result + (barbershopId != null ? barbershopId.hashCode() : 0);
        result = 31 * result + (barberId != null ? barberId.hashCode() : 0);
        result = 31 * result + (customerId != null ? customerId.hashCode() : 0);
        result = 31 * result + (orderDate != null ? orderDate.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        return result;
    }
}
