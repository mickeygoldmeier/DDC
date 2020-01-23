package com.ddc.UI.FriendsParcels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddc.R;

public class FriendsParcelsFragment extends Fragment {

    private RecyclerView parcelRecyclerView;
    private FriendsParcelsViewModel friendsParcelsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        friendsParcelsViewModel = ViewModelProviders.of(this).get(FriendsParcelsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_friends_parcels, container, false);

        parcelRecyclerView = root.findViewById(R.id.friends_parcels_rv);
        parcelRecyclerView.setHasFixedSize(true);
        parcelRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        friendsParcelsViewModel.setFragment(this);
        
        return root;
    }

    public void onUsersChange() {
        if (parcelRecyclerView.getAdapter() == null)
            parcelRecyclerView.setAdapter(friendsParcelsViewModel.getNewParcelRecycleViewAdapter());
        parcelRecyclerView.getAdapter().notifyDataSetChanged();
    }

    public RecyclerView.Adapter getRecyclerAdapter() {
        return parcelRecyclerView.getAdapter();
    }

}
