package com.app.barbershopweb.barbershop.crud;

import java.time.LocalTime;
import java.util.Objects;

public class Barbershop {
    private Long id;
    private String address;
    private String name;
    private String phoneNumber;
    private String email;
    private LocalTime workTimeFrom;
    private LocalTime workTimeTo;
    private boolean active;


    public Barbershop() {
    }

    public Barbershop(Long id, String address, String name,
                      String phoneNumber, String email,
                      LocalTime workTimeFrom, LocalTime workTimeTo,
                      boolean active) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.workTimeFrom = workTimeFrom;
        this.workTimeTo = workTimeTo;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalTime getWorkTimeFrom() {
        return workTimeFrom;
    }

    public void setWorkTimeFrom(LocalTime workTimeFrom) {
        this.workTimeFrom = workTimeFrom;
    }

    public LocalTime getWorkTimeTo() {
        return workTimeTo;
    }

    public void setWorkTimeTo(LocalTime workTimeTo) {
        this.workTimeTo = workTimeTo;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Barbershop that = (Barbershop) o;

        if (active != that.active) return false;
        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(address, that.address)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(phoneNumber, that.phoneNumber)) return false;
        if (!Objects.equals(email, that.email)) return false;
        if (!Objects.equals(workTimeFrom, that.workTimeFrom)) return false;
        return Objects.equals(workTimeTo, that.workTimeTo);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (workTimeFrom != null ? workTimeFrom.hashCode() : 0);
        result = 31 * result + (workTimeTo != null ? workTimeTo.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        return result;
    }
}
