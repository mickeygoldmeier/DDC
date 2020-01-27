package com.ddc.UI.ParcelHistory;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.ddc.Model.NotifyDataChange;
import com.ddc.Model.Parcel.HistoryParcelFirebase;
import com.ddc.Model.Parcel.Parcel;
import com.ddc.R;
import com.ddc.UI.ViewHolders.HistoryParcelViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ParcelHistoryViewModel extends AndroidViewModel {

    private List<Parcel> HistoryParcels;
    private static ParcelHistoryFragment fragment;

    public ParcelHistoryViewModel(@NonNull Application application) {
        super(application);

        HistoryParcels = new ArrayList<>();

        SharedPreferences sharedPreferences = getApplication().getApplicationContext().getSharedPreferences("com.DDC.LastLoginData", Context.MODE_PRIVATE);
        String userID = sharedPreferences.getString("LastLoginUserID", null);
        HistoryParcelFirebase.notifyToUserParcelList(userID, new NotifyDataChange<List<Parcel>>() {
            @Override
            public void OnDataChanged(List<Parcel> obj) {

                HistoryParcels = obj;

                if (fragment != null)
                    fragment.onUsersChange();
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
    }

    public void setFragment(ParcelHistoryFragment fragment) {
        this.fragment = fragment;
    }

    // get new ParcelRecycleViewAdapter from this view model
    public ParcelRecycleViewAdapter getNewParcelRecycleViewAdapter() {
        return new ParcelRecycleViewAdapter();
    }

    // inner ParcelRecycleViewAdapter class
    public class ParcelRecycleViewAdapter extends RecyclerView.Adapter<HistoryParcelViewHolder> {
        @NonNull
        @Override
        public HistoryParcelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.parcel_view_holder, parent, false);
            return new HistoryParcelViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final HistoryParcelViewHolder holder, final int position) {
            Parcel parcel = HistoryParcels.get(position);
            holder.fillView(parcel, getApplication());

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
            return HistoryParcels.size();
        }
    }


}