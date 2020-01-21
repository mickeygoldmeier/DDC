package com.ddc.UI.FindFriendsScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.ddc.R;

public class FindFriendsFragment extends Fragment {

    private FindFriendsViewModel findFriendsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        findFriendsViewModel =
                ViewModelProviders.of(this).get(FindFriendsViewModel.class);
        View root = inflater.inflate(R.layout.find_friends_slideshow, container, false);
        return root;
    }
}