package com.ddc.UI.FindFriendsScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ddc.R;

public class FindFriendsFragment extends Fragment {

    private FindFriendsViewModel findFriendsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        findFriendsViewModel =
                ViewModelProviders.of(this).get(FindFriendsViewModel.class);
        View root = inflater.inflate(R.layout.find_friends_slideshow, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        findFriendsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}