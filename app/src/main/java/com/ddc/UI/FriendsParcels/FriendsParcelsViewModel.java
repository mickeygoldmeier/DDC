package com.ddc.UI.FriendsParcels;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.ddc.Model.Parcel.Parcel;
import com.ddc.Model.Parcel.ParcelRepository;
import com.ddc.R;
import com.ddc.UI.ParcelViewHolder;

import java.util.List;

public class FriendsParcelsViewModel extends AndroidViewModel {

    private ParcelRepository repository;
    private LiveData<List<Parcel>> allParcel;

    public FriendsParcelsViewModel(@NonNull Application application) {
        super(application);
        repository = new ParcelRepository(application);
        allParcel = repository.getAllParcels();
    }

    public LiveData<List<Parcel>> getAllParcels() {
        return allParcel;
    }

    // get new ParcelRecycleViewAdapter from this view model
    public ParcelRecycleViewAdapter getNewParcelRecycleViewAdapter() {
        return new ParcelRecycleViewAdapter();
    }

    // inner Parcel
    public class ParcelRecycleViewAdapter extends RecyclerView.Adapter<ParcelViewHolder> {
        @NonNull
        @Override
        public ParcelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getApplication().getApplicationContext()).inflate(R.layout.parcel_view_holder, parent, false);
            return new ParcelViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ParcelViewHolder holder, int position) {
            Parcel parcel = allParcel.getValue().get(position);
            holder.fillView(parcel);
        }

        @Override
        public int getItemCount() {
            return allParcel.getValue().size();
        }
    }
}