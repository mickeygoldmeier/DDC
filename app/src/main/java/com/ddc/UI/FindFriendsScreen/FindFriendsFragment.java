package com.ddc.UI.FindFriendsScreen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddc.Model.Users.Person;
import com.ddc.R;

import java.util.List;

public class FindFriendsFragment extends Fragment {

    RecyclerView friendsRecyclerView;
    private FindFriendsViewModel findFriendsViewModel;
    private LinearLayout loading;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        findFriendsViewModel = ViewModelProviders.of(this).get(FindFriendsViewModel.class);
        View root = inflater.inflate(R.layout.find_friends_slideshow, container, false);

        loading = root.findViewById(R.id.loading_friends_ll);

        friendsRecyclerView = root.findViewById(R.id.find_friends_rv);
        friendsRecyclerView.setHasFixedSize(true);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        findFriendsViewModel.setFragment(this);

        return root;
    }

    public void onUsersChange(List<Person> people) {
        if (friendsRecyclerView.getAdapter() == null)
            friendsRecyclerView.setAdapter(findFriendsViewModel.getNewFriendsRecycleViewAdapter());
        loading.setVisibility(View.GONE);
        friendsRecyclerView.getAdapter().notifyDataSetChanged();
    }

    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 1);
                return false;
            }
        }
        return true;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                Toast.makeText(getContext(), "ההרשאה אושרה", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "אי אפשר לקבל את רשימת החברים עד אישור ההרשאה", Toast.LENGTH_SHORT).show();
        }
    }


}