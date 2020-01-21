package com.ddc.UI.FriendsParcels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.ddc.R;

public class FriendsParcelsFragment extends Fragment {

    private RecyclerView parcelRecyclerView;
    private FriendsParcelsViewModel friendsParcelsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        friendsParcelsViewModel = ViewModelProviders.of(this).get(FriendsParcelsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_friends_parcels, container, false);
        
        return root;
    }

}
