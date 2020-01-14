package com.ddc.UI.LogInScreen;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.ddc.Model.Person.Person;
import com.ddc.Model.Person.PersonRepository;
import com.ddc.Utils.CitiesList;
import com.ddc.Utils.DataCheck;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LogInViewModel extends AndroidViewModel {

    private PersonRepository repository;
    private LiveData<List<Person>> allPerson;

    public LogInViewModel(@NonNull Application application) {
        super(application);
        repository = new PersonRepository(application);
        allPerson = repository.getAllPersons();

        // update the cites list
        CitiesList.UpdateCitiesList(getApplication().getApplicationContext());
    }

    // try log in to the app using the entered phone nu,ber and id
    public String logIn(String phone, String password) {
        try {
            for (Person person : allPerson.getValue())
                if (person.getUserID().equals(DataCheck.normalizePhoneNumber(phone)))
                    if (person.getPassword().equals(password)) {
                        return person.getUserID();
                    }
        } catch (Exception e) { }
        return null;
    }

    // return the id of the last user if the last login was in less then a week
    public String checkLastLogin(Activity activity)
    {
        try
        {
            SharedPreferences sharedPreferences = activity.getSharedPreferences("com.DDC.LastLoginData", Context.MODE_PRIVATE);
            Date lastLogin = new Date(sharedPreferences.getString("LastLoginTime", null));
            Calendar lastWeek = Calendar.getInstance();
            lastWeek.setTime(lastWeek.getTime());
            lastWeek.add(Calendar.DATE, -7);
            if(lastLogin.after(lastWeek.getTime()))
            {
                String lastLoginUserID = sharedPreferences.getString("LastLoginUserID", null);
                if(lastLoginUserID != null)
                    return lastLoginUserID;
            }
        } catch (Exception e){}
        return null;
    }
}
