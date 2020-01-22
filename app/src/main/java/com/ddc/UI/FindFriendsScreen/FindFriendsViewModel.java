package com.ddc.UI.FindFriendsScreen;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.ddc.Model.Action;
import com.ddc.Model.NotifyDataChange;
import com.ddc.Model.Users.Person;
import com.ddc.Model.Users.User;
import com.ddc.Model.Users.UsersFirebase;
import com.ddc.R;
import com.ddc.UI.NewFriendsViewHolder;

import java.util.ArrayList;
import java.util.List;

public class FindFriendsViewModel extends AndroidViewModel {

    private List<Person> users;
    private Application application;
    private FindFriendsFragment fragment;
    private Person person;
    private FindFriendsViewModel thisViewModel;

    public FindFriendsViewModel(@NonNull Application application) {
        super(application);

        UsersFirebase.stopNotifyToUserList();
        UsersFirebase.notifyToUserList(new NotifyDataChange<List<User>>() {
            @Override
            public void OnDataChanged(List<User> obj) {
                users = getPersonFromUsers(obj);
                if (fragment != null)
                    fragment.onUsersChange(users);
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });

        this.application = application;
        thisViewModel = this;
    }

    // set the using fragment to update when data changed
    public void setFragment(FindFriendsFragment fragment) {
        this.fragment = fragment;
    }

    // filtering the users
    private List<Person> getPersonFromUsers(List<User> users) {
        SharedPreferences sharedPreferences = application.getApplicationContext().getSharedPreferences("com.DDC.LastLoginData", Context.MODE_PRIVATE);
        String userID = sharedPreferences.getString("LastLoginUserID", null);
        List<Person> people = new ArrayList<>();

        // filter only the Persons in the contacts
        for (User user : users) {
            if (user instanceof Person)
                if (userID.equals(user.getUserID())) {
                    person = (Person) user;
                    continue;
                }
                if (contactExist(user.getUserID(), true))
                    people.add((Person) user);
        }

        if (person != null) {
            List<Person> newPeople = new ArrayList<>();
            for (Person friend : people)
                if (!person.getFriends().contains(friend.getUserID()))
                    newPeople.add(friend);
            return newPeople;
        }

        return people;
    }

    // check if the users is in the user contact
    private boolean contactExist(String phone, boolean firstTime) {
        if (fragment.checkPermission()) {
            Uri lookUpURI = Uri.withAppendedPath(
                    ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                    Uri.encode(phone));
            String[] phoneNumberProjection = {ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME};
            Cursor cur = application.getContentResolver().query(lookUpURI, phoneNumberProjection, null, null, null);
            try {
                if (cur.moveToFirst())
                    return true;
            } finally {
                if (cur != null)
                    cur.close();
            }

            // try again with no country code
            if (firstTime)
                return contactExist(phone.replace("+9725", "05"), false);
        }
        return false;
    }

    public FriendsRecycleViewAdapter getNewFriendsRecycleViewAdapter() {
        return new FriendsRecycleViewAdapter();
    }

    public void addFriend(String id) {
        person.addFriend(id);
        UsersFirebase.updateUser(person, new Action<String>() {
            @Override
            public void onSuccess(String obj) {
                Toast.makeText(application.getApplicationContext(), "you now have one more friend. do you still feel lonley?", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Exception exception) {

            }

            @Override
            public void onProgress(String status, double percent) {

            }
        });
    }

    // inner ParcelRecycleViewAdapter class
    public class FriendsRecycleViewAdapter extends RecyclerView.Adapter<NewFriendsViewHolder> {
        @NonNull
        @Override
        public NewFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_friends_view_holder, parent, false);
            return new NewFriendsViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final NewFriendsViewHolder holder, final int position) {
            Person friend = users.get(position);
            holder.fillView(friend, thisViewModel);
        }

        @Override
        public int getItemCount() {
            return users.size();
        }
    }
}