package com.ddc.Model.Users;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UsersManager {
    private static List<User> UsersList = new LinkedList<>();

    public static List<User> getUsersList() {
        return UsersList;
    }

    public static void setUsersList(List<User> usersList) {
        UsersList = usersList;
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
