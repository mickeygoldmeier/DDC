package com.ddc.UI;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ddc.Model.Users.Person;
import com.ddc.R;
import com.ddc.UI.FindFriendsScreen.FindFriendsViewModel;

public class NewFriendsViewHolder extends RecyclerView.ViewHolder {

    private TextView friendName;
    private TextView friendPhone;
    private Button addFriend;
    private Person friend;
    private FindFriendsViewModel findFriendsViewModel;

    public NewFriendsViewHolder(@NonNull View itemView) {
        super(itemView);

        friendName = itemView.findViewById(R.id.friend_name_tv);
        friendPhone = itemView.findViewById(R.id.friend_phone_tv);
        addFriend = itemView.findViewById(R.id.add_friend_btn);

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (friend != null && findFriendsViewModel != null) {
                    findFriendsViewModel.addFriend(friend.getUserID());
                }
            }
        });
    }

    public void fillView(Person _friend, FindFriendsViewModel viewModel) {
        this.friend = _friend;
        this.findFriendsViewModel = viewModel;

        friendName.setText(friend.getFirstName() + " " + friend.getLastName());
        friendPhone.setText(friend.getUserID());
    }
}
