package com.ddc.UI.MyParcelsScreen;

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
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.ddc.Model.Parcel.Parcel;
import com.ddc.R;

import java.util.List;

public class MyParcelsFragment extends Fragment {

    private MyParcelsViewModel myParcelsViewModel;
    private RecyclerView parcelRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myParcelsViewModel = ViewModelProviders.of(this).get(MyParcelsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_parcels, container, false);

        parcelRecyclerView = (RecyclerView) root.findViewById(R.id.my_parcels_rv);
        parcelRecyclerView.setHasFixedSize(true);
        parcelRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myParcelsViewModel.getAllParcels().observe(this, new Observer<List<Parcel>>() {
            @Override
            public void onChanged(List<Parcel> parcels) {
                if (parcelRecyclerView.getAdapter() == null)
                    parcelRecyclerView.setAdapter(myParcelsViewModel.getNewParcelRecycleViewAdapter());
                parcelRecyclerView.getAdapter().notifyDataSetChanged();
            }
        });
        ((SimpleItemAnimator) parcelRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        return root;
    }
}