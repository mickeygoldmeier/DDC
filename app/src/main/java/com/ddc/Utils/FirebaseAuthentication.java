package com.ddc.Utils;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ddc.UI.LogInScreen.LogInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.internal.zzl;

import java.util.concurrent.TimeUnit;

public class FirebaseAuthentication extends AppCompatActivity {
    private String verificationId;
    private PhoneAuthProvider authProvider;
    private FirebaseAuth auth;
    private LogInActivity logInActivity;
    private String userID;

    public FirebaseAuthentication(LogInActivity logInActivity, String userID) {
        this.logInActivity = logInActivity;
        this.userID = userID;
        FirebaseAuth.getInstance().signOut();
        auth = FirebaseAuth.getInstance();
        auth.signOut();
    }

    public void startAuth()
    {
        auth.useAppLanguage();
        authProvider = PhoneAuthProvider.getInstance();

        AuthCredential credential = PhoneAuthProvider.getCredential("+9725097913620", "518090");
        FirebaseAuthSettings user = FirebaseAuth.getInstance().getFirebaseAuthSettings();

        authProvider.verifyPhoneNumber(
                userID,             // Phone number to verify
                60,              // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                logInActivity,      // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        System.out.println(1);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {

                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                    }
                });
    }

    public void signIn(String code)
    {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        if(credential.getSmsCode().equals(code))
            logInActivity.openMainScreen(userID);
        // [END verify_with_code]
        //signInWithPhoneAuthCredential(credential);
    }

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseAuth.getInstance().signOut();
                            logInActivity.openMainScreen(userID);
                        } else {
                            // Sign in failed, display a message and update the UI
                           if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                //mVerificationField.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            //updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }
    // [END sign_in_with_phone]
}
