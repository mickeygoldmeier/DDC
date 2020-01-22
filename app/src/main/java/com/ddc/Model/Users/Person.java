package com.ddc.Model.Users;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Person extends User {
    private String FirstName;
    private String LastName;
    private List<String> Friends;

    public Person() {
        Friends = new ArrayList<>();
    }

    public Person(String userID, String password, Calendar lastModified, com.ddc.Model.Address address, String firstName, String lastName) {
        super(userID, password, lastModified, address);
        FirstName = firstName;
        LastName = lastName;
        Friends = new ArrayList<>();
    }

    public Person(String userID, String password, Calendar lastModified, com.ddc.Model.Address address, String firstName, String lastName, List<String> friends) {
        super(userID, password, lastModified, address);
        FirstName = firstName;
        LastName = lastName;
        Friends = friends;
    }

    public List<String> getFriends() {
        return Friends;
    }

    public void setFriends(List<String> friends) {
        Friends = friends;
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

    public void addFriend(String id){
        Friends.add(id);
    }
}
