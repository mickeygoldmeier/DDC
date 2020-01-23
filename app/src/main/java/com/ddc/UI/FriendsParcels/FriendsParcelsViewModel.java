package com.ddc.UI.FriendsParcels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.ddc.Model.Action;
import com.ddc.Model.NotifyDataChange;
import com.ddc.Model.Parcel.Parcel;
import com.ddc.Model.Parcel.ParcelFirebase;
import com.ddc.Model.Users.Person;
import com.ddc.Model.Users.User;
import com.ddc.Model.Users.UsersFirebase;
import com.ddc.R;
import com.ddc.UI.ViewHolders.FriendParcelViewHolder;

import java.util.ArrayList;
import java.util.List;

public class FriendsParcelsViewModel extends AndroidViewModel {

    private Person person;
    private List<Parcel> friendsParcels;
    private List<User> allUsers;
    private FriendsParcelsFragment fragment;
    private FriendsParcelsViewModel thisViewModel;

    public FriendsParcelsViewModel(@NonNull Application application) {
        super(application);

        friendsParcels = new ArrayList<>();

        allUsers = new ArrayList<>();
        UsersFirebase.stopNotifyToUserList();
        UsersFirebase.notifyToUserList(new NotifyDataChange<List<User>>() {
            @Override
            public void OnDataChanged(List<User> obj) {
                allUsers = obj;
                fragment.getRecyclerAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });

        getPerson();

        thisViewModel = this;
    }

    // set up the listener of the friendsParcels list
    private void notifyToFriendsParcels() {
        if (person.getFriends() != null && person.getFriends().size() != 0) {
            for (String id : person.getFriends()) {
                ParcelFirebase.notifyTouserParcelList(id, new NotifyDataChange<List<Parcel>>() {
                    @Override
                    public void OnDataChanged(List<Parcel> obj) {
                        List<Parcel> newList = new ArrayList<>();

                        for (Parcel parcel : obj) {
                            boolean parcelFounded = false;
                            for (Parcel parcel1 : friendsParcels)
                                if (parcel1.getParcelID().equals(parcel.getParcelID())) {
                                    newList.add(parcel);
                                    parcelFounded = true;
                                    break;
                                }
                            if (!parcelFounded)
                                newList.add(parcel);
                        }

                        friendsParcels = newList;
                        fragment.onUsersChange();
                    }

                    @Override
                    public void onFailure(Exception exception) {

                    }
                });
            }
        }
    }

    private void getPerson() {
        SharedPreferences sharedPreferences = getApplication().getApplicationContext().getSharedPreferences("com.DDC.LastLoginData", Context.MODE_PRIVATE);
        String userID = sharedPreferences.getString("LastLoginUserID", null);
        // filter only the Persons in the contacts
        UsersFirebase.stopNotifyToUserList();
        UsersFirebase.getUser(userID, new NotifyDataChange<Person>() {
            @Override
            public void OnDataChanged(Person obj) {
                person = obj;
                notifyToFriendsParcels();
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
    }

    // set the using fragment to update when data changed
    public void setFragment(FriendsParcelsFragment fragment) {
        this.fragment = fragment;
    }

    // get new ParcelRecycleViewAdapter from this view model
    public ParcelRecycleViewAdapter getNewParcelRecycleViewAdapter() {
        return new ParcelRecycleViewAdapter();
    }

    public void addOptionalDeliver(Parcel parcel, String id) {
        parcel.addOptionalDeliver(id);
        ParcelFirebase.updateParcel(parcel, new Action<String>() {
            @Override
            public void onSuccess(String obj) {
                Toast.makeText(getApplication().getBaseContext(), "נשרמת כמוביל לחבילה הזאת", Toast.LENGTH_SHORT).show();
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
    public class ParcelRecycleViewAdapter extends RecyclerView.Adapter<FriendParcelViewHolder> {
        @NonNull
        @Override
        public FriendParcelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_parcel_view_holder, parent, false);
            return new FriendParcelViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final FriendParcelViewHolder holder, final int position) {
            Parcel parcel = friendsParcels.get(position);

            // find the original user of the parcel
            Person originalUser = null;
            for (User user : allUsers)
                if (user.getUserID().equals(parcel.getRecipientPhone())) {
                    originalUser = (Person) user;
                    break;
                }

            holder.fillView(parcel, originalUser, person, thisViewModel);
        }

        @Override
        public int getItemCount() {
            return friendsParcels.size();
        }
    }
}