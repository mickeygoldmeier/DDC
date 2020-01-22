package com.ddc.UI.LogOutScreen;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.Date;

public class LogOutViewModel extends AndroidViewModel {

    Application application;

    public LogOutViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public void logOut() {
        SharedPreferences.Editor editor = application.getSharedPreferences("com.DDC.LastLoginData", application.MODE_PRIVATE).edit();
        editor.putString("LastLoginTime", new Date(1, 1, 1).toString());
        editor.putString("LastLoginUserID", null);
        editor.apply();
    }
}