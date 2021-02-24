package model;

import java.sql.Timestamp;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-21
 * Time: 20:25
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public class Customer {
    private Integer id;
    private String shortName;
    private String fullName;
    private String personalNumber;
    private String phoneNumber;
    private String email;
    private String streetAddress;
    private String zipCode;
    private String city;
    private String password;
    private Timestamp created;
    private Timestamp updated;

    public Customer() {}

    public Customer(Integer id, String shortName, String fullName, String personalNumber,
                    String phoneNumber, String email, String streetAddress, String zipCode,
                    String city, String password, Timestamp created, Timestamp updated) {
        this.id = id;
        this.shortName = shortName;
        this.fullName = fullName;
        this.personalNumber = personalNumber;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.streetAddress = streetAddress;
        this.zipCode = zipCode;
        this.city = city;
        this.password = password;
        this.created = created;
        this.updated = updated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
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

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "Customer{" + fullName + "}";
    }
}
