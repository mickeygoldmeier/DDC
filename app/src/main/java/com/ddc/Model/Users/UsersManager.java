package com.ddc.Model.Users;

import com.ddc.Model.NotifyDataChange;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UsersManager {
    private static List<User> UsersList = new LinkedList<>();
    private static boolean changed = false;
    private static Person user;


    public static void getUserFromFirebase(String id){
        UsersFirebase.getUser(id, new NotifyDataChange<Person>() {
            @Override
            public void OnDataChanged(Person obj) {
                user = obj;
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
    }

    public static void activateUserManager(){
        UsersFirebase.notifyToUserList(new NotifyDataChange<List<User>>() {
            @Override
            public void OnDataChanged(List<User> obj) {
                UsersList = obj;
                changed = true;
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
    }

    public synchronized static List<User> getUsersList() {
        return UsersList;
    }
    public synchronized static Person getUser() {
        return user;
    }

    public static void setUsersList(List<User> usersList) {
        UsersList = usersList;
    }

    public static void setUser(Person person){
        user = person;
    }

    public static User getUser(String id) throws Exception {
        for (User user : UsersList)
            if (user.getUserID().equals(id))
                return user;
        throw new Exception();
    }

    public static String[] getStringPhoneList() {
        List<String> list = new ArrayList<String>();
        for (User user : UsersList) {
            list.add(user.getUserID());
        }
        return list.toArray(new String[list.size()]);
    }
}
