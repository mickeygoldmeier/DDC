package com.ddc.Model;

import androidx.room.TypeConverter;

import com.ddc.Model.Parcel.Parcel_Status;
import com.ddc.Model.Parcel.Parcel_Type;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    // Convert Parcel_Type to String when extracting data from database
    @TypeConverter
    public static String parcelTypeToString(Parcel_Type value) {
        return value == null ? null : value.name();
    }

    // Convert String to Parcel_Type when extracting data from database
    @TypeConverter
    public static Parcel_Type stringToParcelType(String value) {
        return value == null ? null : Parcel_Type.valueOf(value);
    }

    // Convert Parcel_Type to String when extracting data from database
    @TypeConverter
    public static String parcelStatusToString(Parcel_Status value) {
        return value == null ? null : value.name();
    }

    // Convert String to Parcel_Type when extracting data from database
    @TypeConverter
    public static Parcel_Status stringToParcelStatus(String value) {
        return value == null ? null : Parcel_Status.valueOf(value);
    }

    @TypeConverter
    public static String stringListToList(List<String> list) {
        String result = "";
        for (String id : list)
            result += id + "|";

        return result.substring(0, result.length() - 1);
    }

    @TypeConverter
    public static List<String> stringToStringList(String string) {
        String[] stringArray = string.split("\\|");
        List<String> stringList = new ArrayList<>();
        for (String id : stringArray)
            stringList.add(id);

        return stringList;
    }

}
