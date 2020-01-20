package com.ddc.Model.Users;

import java.util.Calendar;

public class Company extends User {
    private String Name;

    public Company() {
    }

    public Company(String userID, String password, Calendar lastModified, com.ddc.Model.Address address, String name) {
        super(userID, password, lastModified, address);
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public boolean search(String string) {
        return (this.Name.contains(string)) || (this.getUserID().contains(string));
    }

    @Override
    public boolean generatePassword() {
        //Calendar currentDate = Calendar.getInstance();
        //Calendar lastDate = getLastModified();
        //Calendar diffrence = Calendar.getInstance();
        //diffrence.set(currentDate.get(Calendar.YEAR)-lastDate.get(Calendar.YEAR),currentDate.get(Calendar.MONTH)-lastDate.get(Calendar.MONTH),currentDate.get(Calendar.DAY_OF_MONTH)-lastDate.get(Calendar.DAY_OF_MONTH),currentDate.get(Calendar.HOUR_OF_DAY)-lastDate.get(Calendar.HOUR_OF_DAY),currentDate.get(Calendar.MINUTE)-lastDate.get(Calendar.MINUTE));
        //Calendar sixMonth = Calendar.getInstance();
        //sixMonth.set(0,6,1,0,0);
        //if (diffrence.equals(sixMonth))
        return true;


    }
}
