package com.ddc.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Address {
    private String Country;
    private String City;
    private String Street;
    private int Number;

    public Address() {
    }

    public Address(String country, String city, String street, int number) {
        setCountry(country);
        setCity(city);
        setStreet(street);
        setNumber(number);
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public int getNumber() {
        return Number;
    }

    public void setNumber(int number) {
        Number = number;
    }

    @Override
    public String toString() {
        return Street + " " + Number + ", " + City;
    }
}
