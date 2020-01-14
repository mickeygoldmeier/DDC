package com.ddc.Model.Person;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ddc.Model.Address;
import com.google.firebase.database.Exclude;

@Entity(tableName = "persons_table")
public class Person {

    @PrimaryKey
    @NonNull
    private String UserID;
    private String FirstName;
    private String LastName;
    private String Password;
    private com.ddc.Model.Address Address;
    private long Date;
    private Calendar LastModified;

    public Person() {
    }

    public Person(String userID, String password, Calendar lastModified, Address address, String firstName, String lastName) {
        UserID = userID;
        Password = password;
        LastModified = lastModified;
        Date = lastModified.getTimeInMillis();
        Address = address;
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

    public long getDate() {
        Date = getLastModified().getTimeInMillis();
        return Date;
    }

    public void setDate(long date) {
        Calendar calendar =  Calendar.getInstance();
        calendar.setTimeInMillis(date);
        setLastModified(calendar);
        Date = date;
    }

    public String getUserID() {return UserID; }
    public void setUserID(String userID) {UserID = userID; }
    public String getPassword() {return Password; }
    public void setPassword(String password) {Password = password; }

    public boolean search(String string) {
        return false;
    }
    public boolean generatePassword() {
        return false;
    }
    @Exclude
    public Calendar getLastModified() {return LastModified; }
    @Exclude
    public void setLastModified(Calendar lastModified) {LastModified = lastModified;}
    public com.ddc.Model.Address getAddress() {return Address; }
    public void setAddress(com.ddc.Model.Address address) {Address = address;}
}
