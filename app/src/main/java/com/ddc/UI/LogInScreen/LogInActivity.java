package com.ddc.UI.LogInScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ddc.MainActivity;
import com.ddc.R;
import com.ddc.UI.SignUpScreen.SignUpScreen;
import com.ddc.Utils.MessageListener;
import com.ddc.Utils.MessageReceiver;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.TimeUnit;

public class LogInActivity extends AppCompatActivity implements MessageListener {

    private LogInViewModel logInViewModel;
    private EditText password_et;

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
        // TODO: make it work!
        String lastUserID = logInViewModel.checkLastLogin(this);
        if(lastUserID != null)
        {
            logInViewModel.getUser(lastUserID);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("UserID", lastUserID);
            this.finish();
            startActivity(intent);
        }

        // log in to the next screen
        final EditText id_ed = findViewById(R.id.id_et);
        final TextView message_tv = findViewById(R.id.message_tv);
        final LogInActivity activity = this;
        this.password_et = findViewById(R.id.password_et);
        Button logIn = findViewById(R.id.signin_btn);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password_et.getVisibility() == EditText.VISIBLE) {
                    if(TextUtils.isEmpty(password_et.getText().toString()))
                        message_tv.setText("Error");
                    else
                        logInViewModel.checkSMSCode(password_et.getText().toString());
                } else if(!TextUtils.isEmpty(id_ed.getText().toString())) {
                    // check if the user exist and then send him the SMS
                    if(logInViewModel.logInWithSMS(id_ed.getText().toString(), activity))
                    {
                        password_et.setVisibility(View.VISIBLE);
                        message_tv.setText(R.string.waiting_for_sms);
                    }
                }
            }
        });

        // listener for sms
        MessageReceiver messageReceiver = new MessageReceiver();
        messageReceiver.bindListener(this);
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(messageReceiver, mIntentFilter);

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

    public void openMainScreen(String userID)
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("UserID", userID);
        this.finish();
        startActivity(intent);
    }

    @Override
    public void messageReceived(String message) {
        password_et.setText(message);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
