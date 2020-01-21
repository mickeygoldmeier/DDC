package com.ddc.UI.FindFriendsScreen;

import androidx.lifecycle.ViewModel;

import com.ddc.Model.NotifyDataChange;
import com.ddc.Model.Users.Person;
import com.ddc.Model.Users.User;
import com.ddc.Model.Users.UsersFirebase;

import java.util.ArrayList;
import java.util.List;

public class FindFriendsViewModel extends ViewModel {

    private List<Person> users;

    public FindFriendsViewModel() {
        getUsers();

    }

    private List<Person> getPersonFromUsers(List<User> users){
        List<Person> people =new ArrayList<>();
        for (User user:users){
            if (user instanceof Person)
                people.add((Person) user);
        }
        return people;
    }

    private void getUsers(){
        UsersFirebase.stopNotifyToUserList();
        UsersFirebase.notifyToUserList(new NotifyDataChange<List<User>>() {
            @Override
            public void OnDataChanged(List<User> obj) {
                users = getPersonFromUsers(obj);
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
    }


}