package com.ddc.UI.MyParcelsScreen;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.ddc.Model.NotifyDataChange;
import com.ddc.Model.Parcel.Parcel;
import com.ddc.Model.Parcel.ParcelRepository;
import com.ddc.Model.Parcel.Parcel_Status;
import com.ddc.Model.Users.Person;
import com.ddc.Model.Users.User;
import com.ddc.Model.Users.UsersFirebase;
import com.ddc.R;
import com.ddc.UI.ViewHolders.ParcelViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyParcelsViewModel extends AndroidViewModel {

    private List<User> allUsers;
    private ParcelRepository repository;
    private LiveData<List<Parcel>> allParcel;
    private MyParcelsViewModel thisViewModel;
    private MyParcelsFragment fragment;

    public MyParcelsViewModel(@NonNull Application application) {
        super(application);

        repository = new ParcelRepository(application);
        allParcel = repository.getAllParcels();
        thisViewModel = this;

        allUsers = new ArrayList<>();
        UsersFirebase.stopNotifyToUserList();
        UsersFirebase.notifyToUserList(new NotifyDataChange<List<User>>() {
            @Override
            public void OnDataChanged(List<User> obj) {
                allUsers = obj;
                if (fragment != null)
                    fragment.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
    }

    public LiveData<List<Parcel>> getAllParcels() {
        return allParcel;
    }

    // get new ParcelRecycleViewAdapter from this view model
    public ParcelRecycleViewAdapter getNewParcelRecycleViewAdapter() {
        return new ParcelRecycleViewAdapter();
    }

    public void chooseParcelDeliver(Parcel parcel, String id) {
        parcel.setSelectedDeliver(id);
        parcel.setParcelStatus(Parcel_Status.OnTheWay);
        repository.update(parcel);
    }

    // inner ParcelRecycleViewAdapter class
    public class ParcelRecycleViewAdapter extends RecyclerView.Adapter<ParcelViewHolder> {
        @NonNull
        @Override
        public ParcelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.parcel_view_holder, parent, false);
            return new ParcelViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final ParcelViewHolder holder, final int position) {
            Parcel parcel = allParcel.getValue().get(position);

            holder.fillView(parcel, getApplication(), thisViewModel, filterPersons(parcel.getOptionalDelivers()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean expanded = holder.isExpanded();
                    holder.setExpanded(!expanded);
                    notifyItemChanged(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return allParcel.getValue().size();
        }
    }

    public void setFragment(MyParcelsFragment fragment) {
        this.fragment = fragment;
    }

    private HashMap<String, Person> filterPersons(List<String> IDlist) {
        HashMap<String, Person> filteredUses = new HashMap<>();

        for (String id : IDlist)
            for (User user : allUsers)
                if (user.getUserID().equals(id)) {
                    filteredUses.put(id, (Person)user);
                    break;
                }

        return filteredUses;
    }
}