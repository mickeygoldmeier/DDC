package com.ddc.UI.LogInScreen;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.ddc.Model.NotifyDataChange;
import com.ddc.Model.Users.User;
import com.ddc.Model.Users.UsersFirebase;
import com.ddc.Model.Users.UsersManager;
import com.ddc.Utils.CitiesList;
import com.ddc.Utils.DataCheck;
import com.ddc.Utils.FirebaseAuthentication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LogInViewModel extends AndroidViewModel {

    private List<User> users = new ArrayList<>();
    private FirebaseAuthentication authentication;

    public LogInViewModel(@NonNull Application application) {
        super(application);
        UsersFirebase.notifyToUserList(new NotifyDataChange<List<User>>() {
            @Override
            public void OnDataChanged(List<User> obj) {
                users = obj;
                UsersManager.setUsersList(obj);
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });

        // update the cites list
        CitiesList.UpdateCitiesList(getApplication().getApplicationContext());
        //if (checkLastLogin(this) != null)
         //   UsersManager.getUserFromFirebase(checkLastLogin(this));

    }

    // try log in to the app using the entered phone nu,ber and id
    public String logIn(String phone, String password) {
        try {
            for (User person : users)
                if (person.getUserID().equals(DataCheck.normalizePhoneNumber(phone)))
                    if (person.getPassword().equals(password)) {
                        return person.getUserID();
                    }
        } catch (Exception e) { }
        return null;
    }

    public void logInWithSMS(String phone, LogInActivity activity)
    {
        try {
            phone = DataCheck.normalizePhoneNumber(phone);
        } catch (Exception e){return;}

        authentication = new FirebaseAuthentication(activity, phone);
        authentication.startAuth();
    }

    public void checkSMSCode(String code)
    {
        authentication.signIn(code);
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
