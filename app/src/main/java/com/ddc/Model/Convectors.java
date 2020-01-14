package com.ddc.Model;

import androidx.room.TypeConverter;

import java.util.Calendar;

public class Convectors {

    // Convert address to string to be used in the database
    @TypeConverter
    public static Address stringToAddress(String value) {
        try {
            String[] splitValue = value.split("::");
            Address address = new Address();
            address.setCountry(splitValue[0]);
            address.setCity(splitValue[1]);
            address.setStreet(splitValue[2]);
            address.setNumber(Integer.valueOf(splitValue[3]));
            return address;
        } catch (Exception e) {
            return null;
        }
    }

    // Convert string to address when extracting data from database
    @TypeConverter
    public static String addressToString(Address value) {
        try {
            String newValue =
                    value.getCountry() + "::" +
                            value.getCity() + "::" +
                            value.getStreet() + "::" +
                            value.getNumber();
            return newValue;
        } catch (Exception e) {
            return null;
        }
    }


    // Convert calendar to long to be used in the database
    @TypeConverter
    public static long calendarToLong(Calendar value) {
        return value == null ? null : value.getTimeInMillis();
    }

    // Convert long to calendar when extracting data from database
    @TypeConverter
    public static Calendar longToCalendar(long value) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(value);
            return calendar;
        } catch (Exception e) {
            return null;
        }
    }

}
