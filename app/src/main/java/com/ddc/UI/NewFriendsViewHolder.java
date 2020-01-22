package com.ddc.UI;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ddc.Model.Users.Person;
import com.ddc.R;

public class NewFriendsViewHolder extends RecyclerView.ViewHolder {

    private TextView userName;
    private TextView userPhone;
    private Person friend;

    public NewFriendsViewHolder(@NonNull View itemView) {
        super(itemView);

        userName = itemView.findViewById(R.id.friend_name_tv);
        userPhone = itemView.findViewById(R.id.friend_phone_tv);
    }

    public void fillView(Person person) {
        this.friend = person;

        userName.setText(friend.getFirstName() + " " + friend.getLastName());
        userPhone.setText(friend.getUserID());
    }
}
