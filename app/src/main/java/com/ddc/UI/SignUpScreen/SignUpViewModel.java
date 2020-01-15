package com.ddc.UI.SignUpScreen;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.ddc.Model.Address;
import com.ddc.Model.Users.Person;
import com.ddc.Model.Users.UsersFirebase;
import com.ddc.R;
import com.ddc.Utils.CitiesList;
import com.ddc.Utils.DataCheck;

import java.util.GregorianCalendar;

public class SignUpViewModel extends AndroidViewModel {

    public SignUpViewModel(@NonNull Application application) {
        super(application);
    }

    // return the right color for the edit text of the phone number
    public int checkPhoneTextViewColor(String phone) {
        try {
            phone = DataCheck.normalizePhoneNumber(phone);
            return Color.BLACK;
        } catch (Exception e) {
            return Color.RED;
        }
    }

    // return adapter for the city edit text
    public ArrayAdapter<String> getAutoCompleteCitiesListAdapter(Context context) {
        final String[] citiesList = CitiesList.getCitiesArray();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, citiesList);
        return adapter;
    }

    // return the right color for the edit text of the city
    public int checkCityTextViewColor(String text) {
        final String[] citiesList = CitiesList.getCitiesArray();
        for (String city : citiesList)
            if (city.equals(text))
                return Color.BLACK;
        return Color.RED;
    }

    // save the new user
    public int createNewPerson(String phoneNumber, String firstName, String lastName, String city, String street, String homeNumber) {
        try {
            phoneNumber = DataCheck.normalizePhoneNumber(phoneNumber);
        } catch (Exception e) {
            return R.string.worng_phone_number;
        }

        if (firstName.length() < 2)
            return R.string.worng_first_name;

        if (lastName.length() < 2)
            return R.string.worng_last_name;

        if (checkCityTextViewColor(city) == Color.RED)
            return R.string.worng_city;

        if (street.length() < 2)
            return R.string.worng_street;

        int number;
        try {
            number = Integer.valueOf(homeNumber);
        } catch (Exception e) {
            return R.string.worng_home_number;
        }

        try {
            Address address = new Address("ישראל", city, street, number);

            Person person = new Person(phoneNumber, "", new GregorianCalendar(1, 1, 1), address, firstName, lastName);

            UsersFirebase.addUser(person, null);

            return 1;
        } catch (Exception e) {
            return R.string.general_error;
        }
    }

    // call the normalizePhoneNumber for the view
    public String normalizePhoneNumber(String phone)
    {
        try{
            return DataCheck.normalizePhoneNumber(phone);
        } catch (Exception e)
        {
            return null;
        }
    }
}
