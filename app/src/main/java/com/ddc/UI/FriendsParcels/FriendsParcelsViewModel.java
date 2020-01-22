package com.ddc.UI.FriendsParcels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.ddc.Model.NotifyDataChange;
import com.ddc.Model.Parcel.Parcel;
import com.ddc.Model.Parcel.ParcelFirebase;
import com.ddc.Model.Users.Person;
import com.ddc.Model.Users.User;
import com.ddc.Model.Users.UsersFirebase;

import java.util.ArrayList;
import java.util.List;

public class FriendsParcelsViewModel extends AndroidViewModel {

    private Person person;
    private Application application;
    private List<Parcel> friendsparcels;

    public FriendsParcelsViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        getPerson();

    }


    private void getFriendsParcels(){
        if (person.getFriends() != null && person.getFriends().size() != 0) {
            for (String id : person.getFriends()) {
                ParcelFirebase.notifyTouserParcelList(id, new NotifyDataChange<List<Parcel>>() {
                    @Override
                    public void OnDataChanged(List<Parcel> obj) {
                        for (Parcel parcel : obj) {
                            if (!friendsparcels.contains(parcel)) {
                                friendsparcels.add(parcel);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Exception exception) {

                    }
                });
            }
        }
    }

    private void getPerson() {
        SharedPreferences sharedPreferences = application.getApplicationContext().getSharedPreferences("com.DDC.LastLoginData", Context.MODE_PRIVATE);
        String userID = sharedPreferences.getString("LastLoginUserID", null);
        // filter only the Persons in the contacts
        UsersFirebase.stopNotifyToUserList();
        UsersFirebase.getUser(userID, new NotifyDataChange<Person>() {
            @Override
            public void OnDataChanged(Person obj) {
                person = obj;
                getFriendsParcels();
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
    }
}