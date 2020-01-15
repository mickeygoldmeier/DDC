package com.ddc.Model.Users;

import com.ddc.Model.Address;
import com.google.firebase.database.Exclude;

import java.util.Calendar;

public abstract class User {
    private String UserID;
    private String Password;
    private com.ddc.Model.Address Address;
    private long Date;

    private Calendar LastModified;


    public User() {
    }

    public User(String userID, String password, Calendar lastModified, Address address) {
        UserID = userID;
        Password = password;
        LastModified = lastModified;
        Date = lastModified.getTimeInMillis();
        Address = address;
    }

    public long getDate() {
        Date = getLastModified().getTimeInMillis();
        return Date;
    }

    public void setDate(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        setLastModified(calendar);
        Date = date;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    @Exclude
    public Calendar getLastModified() {
        return LastModified;
    }

    @Exclude
    public void setLastModified(Calendar lastModified) {
        LastModified = lastModified;
    }

    public Address getAddress() {
        return Address;
    }

    public void setAddress(Address address) {
        Address = address;
    }

    public abstract boolean search(String string);

    public abstract boolean generatePassword();
}
