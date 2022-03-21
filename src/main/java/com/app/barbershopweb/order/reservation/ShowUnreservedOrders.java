package com.app.barbershopweb.order.reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ShowUnreservedOrders {
    Long barbershopId;
    LocalDateTime reservationDateToStartWeekFrom;
    List<Long>barberIds;
    public ShowUnreservedOrders(Long barbershopId,
                                LocalDateTime reservationDateToStartWeekFrom,
                                List<Long> barberIds) {
        this.barbershopId = barbershopId;
        this.reservationDateToStartWeekFrom = reservationDateToStartWeekFrom;
        this.barberIds = barberIds;
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

        ShowUnreservedOrders that = (ShowUnreservedOrders) o;

        if (!Objects.equals(barbershopId, that.barbershopId)) return false;
        if (!Objects.equals(reservationDateToStartWeekFrom, that.reservationDateToStartWeekFrom))
            return false;
        return Objects.equals(barberIds, that.barberIds);
    }

    @Override
    public int hashCode() {
        int result = barbershopId != null ? barbershopId.hashCode() : 0;
        result = 31 * result + (reservationDateToStartWeekFrom != null ? reservationDateToStartWeekFrom.hashCode() : 0);
        result = 31 * result + (barberIds != null ? barberIds.hashCode() : 0);
        return result;
    }
}
