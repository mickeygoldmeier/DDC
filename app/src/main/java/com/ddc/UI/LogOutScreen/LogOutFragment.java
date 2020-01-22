package com.ddc.UI.LogOutScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.ddc.R;
import com.ddc.UI.LogInScreen.LogInActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LogOutFragment extends Fragment {

    private LogOutViewModel logOutViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        logOutViewModel = ViewModelProviders.of(this).get(LogOutViewModel.class);
        View root = inflater.inflate(R.layout.fragment_log_out, container, false);

        final Fragment fragment = this;
        final FloatingActionButton button = root.findViewById(R.id.log_out_fb);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutViewModel.logOut();
                Intent intent = new Intent(fragment.getContext(), LogInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                fragment.startActivity(intent);
                fragment.getActivity().finish();
            }
        });

        return root;
    }
}