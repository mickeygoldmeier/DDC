package com.ddc.UI.ViewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ddc.Model.Parcel.Parcel;
import com.ddc.Model.Parcel.Parcel_Status;
import com.ddc.Model.Users.Person;
import com.ddc.R;
import com.ddc.UI.FriendsParcels.FriendsParcelsViewModel;

public class FriendParcelViewHolder extends RecyclerView.ViewHolder {

    Button suggestDelivery;
    Button parcelTaken;
    TextView to;
    TextView distributionCenterAddress;
    TextView userAddress;

    FriendsParcelsViewModel viewModel;
    Parcel parcel;
    Person user;
    Person friend;

    public FriendParcelViewHolder(@NonNull View itemView) {
        super(itemView);

        suggestDelivery = itemView.findViewById(R.id.suggest_delivery_btn);
        parcelTaken = itemView.findViewById(R.id.parcel_taken_btn);
        to = itemView.findViewById(R.id.user_name_tv);
        distributionCenterAddress = itemView.findViewById(R.id.current_location_tv);
        userAddress = itemView.findViewById(R.id.final_location_tv);

        suggestDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewModel != null)
                    viewModel.addOptionalDeliver(parcel, friend.getUserID());
            }
        });

        parcelTaken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewModel != null)
                    viewModel.deliverHaveTheParcel(parcel);
            }
        });
    }

    public void fillView(Parcel _parcel, Person _user, Person _friend, FriendsParcelsViewModel _viewModel) {
        this.parcel = _parcel;
        this.user = _user;
        this.friend = _friend;
        this.viewModel = _viewModel;

        distributionCenterAddress.setText(parcel.getDistributionCenterAddress().toString());
        try {
            to.setText(user.getFirstName() + " " + user.getLastName());
            userAddress.setText(user.getAddress().toString());
        } catch (Exception e) {
        }

        try {
            if (!parcel.getOptionalDelivers().contains(friend.getUserID()))
                suggestDelivery.setVisibility(View.VISIBLE);

            if (parcel.getSelectedDeliver().equals(friend.getUserID()) && parcel.getParcelStatus() == Parcel_Status.OnTheWay)
                parcelTaken.setVisibility(View.VISIBLE);
        } catch (Exception e) {
        }
    }
}
