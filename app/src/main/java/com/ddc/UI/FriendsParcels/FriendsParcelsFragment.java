package com.ddc.UI.FriendsParcels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddc.Model.Parcel.Parcel;
import com.ddc.R;

import java.util.List;

public class FriendsParcelsFragment extends Fragment {

    private RecyclerView parcelRecyclerView;
    private FriendsParcelsViewModel friendsParcelsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        friendsParcelsViewModel = ViewModelProviders.of(this).get(FriendsParcelsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_friends_parcels, container, false);

        parcelRecyclerView = (RecyclerView) root.findViewById(R.id.friends_parcels_rv);
        parcelRecyclerView.setHasFixedSize(true);
        parcelRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        friendsParcelsViewModel.getAllParcels().observe(this, new Observer<List<Parcel>>() {
            @Override
            public void onChanged(List<Parcel> parcels) {
                if (parcelRecyclerView.getAdapter() == null)
                    parcelRecyclerView.setAdapter(friendsParcelsViewModel.getNewParcelRecycleViewAdapter());
                parcelRecyclerView.getAdapter().notifyDataSetChanged();
            }
        });
        
        return root;
    }

}
