package com.ddc.Model.Users;

import java.util.Calendar;

public class Person extends User {
    private String FirstName;
    private String LastName;

    public Person() {
    }

    public Person(String userID, String password, Calendar lastModified, com.ddc.Model.Address address, String firstName, String lastName) {
        super(userID, password, lastModified, address);
        FirstName = firstName;
        LastName = lastName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    @Override
    public boolean search(String string) {
        return false;
    }

    @Override
    public boolean generatePassword() {
        return false;
    }
}
