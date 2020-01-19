package com.ddc.UI.FriendsParcels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ddc.Model.Parcel.Parcel;
import com.ddc.R;

import java.util.List;

public class FriendsParcelsFragment extends Fragment {

    private FriendsParcelsViewModel friendsParcelsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        friendsParcelsViewModel = ViewModelProviders.of(this).get(FriendsParcelsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_friends_parcels, container, false);

        final TextView textView = root.findViewById(R.id.text_home);
        friendsParcelsViewModel.getAllParcels().observe(this, new Observer<List<Parcel>>() {
            @Override
            public void onChanged(List<Parcel> parcels) {
                String str = "";
                for (Parcel parcel : parcels)
                    str += parcel.getParcelID() + " " + parcel.getType() + " " + parcel.getCompanyID() + "\n";
                textView.setText(str);
            }
        });
        
        return root;
    }

}
