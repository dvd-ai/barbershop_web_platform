package com.app.barbershopweb.barbershop;

import java.time.LocalTime;

public class Barbershop {
    private Long id;
    private String address;
    private String name;
    private String phoneNumber;
    private String email;
    private LocalTime workTimeFrom;
    private LocalTime workTimeTo;


    public Barbershop() {
    }

    public Barbershop(Long id, String address, String name,
                      String phoneNumber, String email,
                      LocalTime workTimeFrom, LocalTime workTimeTo) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.workTimeFrom = workTimeFrom;
        this.workTimeTo = workTimeTo;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Barbershop that = (Barbershop) o;

        if (!id.equals(that.id)) return false;
        if (!address.equals(that.address)) return false;
        if (!name.equals(that.name)) return false;
        if (!phoneNumber.equals(that.phoneNumber)) return false;
        if (!email.equals(that.email)) return false;
        if (!workTimeFrom.equals(that.workTimeFrom)) return false;
        return workTimeTo.equals(that.workTimeTo);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + address.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + phoneNumber.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + workTimeFrom.hashCode();
        result = 31 * result + workTimeTo.hashCode();
        return result;
    }
}
