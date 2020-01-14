package com.ddc.UI.SignUpScreen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ddc.MainActivity;
import com.ddc.R;
import com.ddc.UI.LogInScreen.LogInViewModel;
import com.ddc.Utils.DataCheck;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class SignUpScreen extends AppCompatActivity {

    private SignUpViewModel signUpViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);

        setContentView(R.layout.activity_sign_up_screen);

        // hide the Action Bar
        getSupportActionBar().hide();

        // define the color of the phoneNumber edit text on run time
        final EditText phoneNumber = findViewById(R.id.phone_number_et);
        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                phoneNumber.setTextColor(signUpViewModel.checkPhoneTextViewColor(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // set the adapter of the cities edit text
        final AutoCompleteTextView city = findViewById(R.id.city_ac);
        city.setAdapter(signUpViewModel.getAutoCompleteCitiesListAdapter(this));

        // define the color of the city edit text on run time
        city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                city.setTextColor(signUpViewModel.checkCityTextViewColor(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // sign up button click
        final EditText firstName = findViewById(R.id.first_name_et);
        final EditText lastName = findViewById(R.id.last_name_et);
        final EditText street = findViewById(R.id.street_et);
        final EditText homeNumber = findViewById(R.id.home_number_et);
        final ProgressBar progressBar = findViewById(R.id.signup_pb);
        final FloatingActionButton signUp = findViewById(R.id.signup_fb);
        final Activity activity = this;
        signUp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                int result = signUpViewModel.createNewPerson(phoneNumber.getText().toString(),
                        firstName.getText().toString(),
                        lastName.getText().toString(),
                        city.getText().toString(),
                        street.getText().toString(),
                        homeNumber.getText().toString());

                if (result == 1)
                    new AlertDialog.Builder(view.getContext())
                            .setMessage(R.string.new_member)
                            .setPositiveButton(R.string.got_it, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra("UserID", signUpViewModel.normalizePhoneNumber(phoneNumber.getText().toString()));
                                    activity.finish();
                                    startActivity(intent);
                                }
                            })
                            .show();
                else
                    Snackbar.make(view, result, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
