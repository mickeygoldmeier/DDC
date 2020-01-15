package com.ddc.Model.Parcel;

import androidx.annotation.NonNull;

import com.ddc.Model.Action;
import com.ddc.Model.NotifyDataChange;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConfigDS {
    static DatabaseReference configRef;
    static String Config;

    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        configRef = database.getReference("Config");
        Config = "100000";
    }


    public static void addConfig(final String key, final String data, final Action<String> action) {
        configRef.child(key).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                action.onSuccess(key);
                action.onProgress("upload " + key + " data", 100);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                action.onFailure(e);
                action.onProgress("error upload " + key + " data", 100);

            }
        });
    }


    public static void removeConfig(final String key, final Action<String> action) {

        configRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String value = dataSnapshot.getValue(String.class);
                if (value == null)
                    action.onFailure(new Exception("config not find ..."));
                else {
                    configRef.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            action.onSuccess(value);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            action.onFailure(e);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                action.onFailure(databaseError.toException());
            }
        });
    }

    public static void updateConfig(final String key, final String data, final Action<String> action) {

        configRef.child(key).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                action.onSuccess(key);
                action.onProgress("upload " + key + " config", 100);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                action.onFailure(e);
            }
        });

    }

    public static void getConfigID(final NotifyDataChange<String> notifyDataChange) {
        DatabaseReference usersRef = configRef.child("ParcelID");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Config = dataSnapshot.getValue(String.class);
                notifyDataChange.OnDataChanged(Config);
                ConfigDS.updateConfig("ParcelID", String.valueOf(Integer.parseInt(Config) + 1), new Action<String>() {
                    @Override
                    public void onSuccess(String obj) {
                    }

                    @Override
                    public void onFailure(Exception exception) {

                    }

                    @Override
                    public void onProgress(String status, double percent) {
                    }
                });
                //Do what you need to do with your list

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Don't ignore errors!
            }
        };
        usersRef.addListenerForSingleValueEvent(valueEventListener);
    }


}