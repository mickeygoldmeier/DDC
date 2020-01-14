package com.ddc.UI.LogInScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ddc.MainActivity;
import com.ddc.R;
import com.ddc.UI.SignUpScreen.SignUpScreen;

import java.util.concurrent.TimeUnit;

public class LogInActivity extends AppCompatActivity {

    private LogInViewModel logInViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logInViewModel = ViewModelProviders.of(this).get(LogInViewModel.class);

        setContentView(R.layout.activity_log_in);

        // hide the Action Bar and the Status bar
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            getActionBar().hide();
        }
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // check if the last login was in less then a week
        String lastUserID = logInViewModel.checkLastLogin(this);
        if(lastUserID != null)
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("UserID", lastUserID);
            this.finish();
            startActivity(intent);
        }

        // log in to the next screen
        final EditText id_ed = findViewById(R.id.id_et);
        final TextView message_tv = findViewById(R.id.message_tv);
        final Activity activity = this;
        Button logIn = findViewById(R.id.signin_btn);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText password_et = findViewById(R.id.password_et);
                if (password_et.getVisibility() == EditText.VISIBLE) {
                    String personID = logInViewModel.logIn(id_ed.getText().toString(), password_et.getText().toString());
                    if(personID != null)
                    {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("UserID", personID);
                        activity.finish();
                        startActivity(intent);
                    }
                } else {
                    password_et.setVisibility(View.VISIBLE);
                    message_tv.setText(R.string.enter_your_password);
                }
            }
        });

        // open the sign up screen
        TextView signUp = findViewById(R.id.signup_tv);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SignUpScreen.class);
                startActivity(i);
            }
        });
    }
}
