package com.app.barbershopweb.user.crud;

import java.time.LocalDateTime;

public class Users {

    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String role;
    private LocalDateTime registrationDate;

    public Users() {
    }

    public Users(Long id, String firstName,
                 String lastName, String phoneNumber,
                 String email, String role,
                 LocalDateTime registrationDate) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.role = role;
        this.registrationDate = registrationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Users users = (Users) o;

        if (!id.equals(users.id)) return false;
        if (!firstName.equals(users.firstName)) return false;
        if (!lastName.equals(users.lastName)) return false;
        if (!phoneNumber.equals(users.phoneNumber)) return false;
        if (!email.equals(users.email)) return false;
        if (!role.equals(users.role)) return false;
        return registrationDate.equals(users.registrationDate);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + phoneNumber.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + role.hashCode();
        result = 31 * result + registrationDate.hashCode();
        return result;
    }
}
