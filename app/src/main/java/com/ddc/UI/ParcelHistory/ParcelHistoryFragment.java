package com.ddc.UI.ParcelHistory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.ddc.R;

public class ParcelHistoryFragment extends Fragment {

    private RecyclerView parcelRecyclerView;
    private ParcelHistoryViewModel parcelHistoryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        parcelHistoryViewModel = ViewModelProviders.of(this).get(ParcelHistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_parcel_history, container, false);

        parcelRecyclerView = root.findViewById(R.id.history_parcels_rv);
        parcelRecyclerView.setHasFixedSize(true);
        parcelRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ((SimpleItemAnimator) parcelRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        parcelHistoryViewModel.setFragment(this);

        return root;
    }

    public void onUsersChange() {
        if (parcelRecyclerView.getAdapter() == null)
            parcelRecyclerView.setAdapter(parcelHistoryViewModel.getNewParcelRecycleViewAdapter());
        parcelRecyclerView.getAdapter().notifyDataSetChanged();
    }
}