package com.ddc.Model.Person;

import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.ddc.Model.Action;
import com.ddc.Model.NotifyDataChange;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PersonFirebase {
    static DatabaseReference usersRef;
    static List<Person> userList;
    static MutableLiveData<List<Person>> personLiveData;

    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");
        userList = new LinkedList<>();
        personLiveData = new MediatorLiveData<>();
    }

    private static void updateLiveData()
    {
        personLiveData.postValue(userList);
        personLiveData.setValue(userList);
    }

    public static void addUser(final Person user, final Action<String> action) {
        String key = user.getUserID();
        usersRef.child(key).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                action.onSuccess(user.getUserID());
                action.onProgress("upload user data", 100);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                action.onFailure(e);
                action.onProgress("error upload user data", 100);
            }
        });
    }


    public static void removeUser(String userid, final Action<String> action) {
        final String key = userid;

        usersRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Person value = dataSnapshot.getValue(Person.class);
                if (value == null)
                    action.onFailure(new Exception("user not find ..."));
                else {
                    usersRef.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            action.onSuccess(value.getUserID());
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

    private static ChildEventListener userRefChildEventListener;

    public static void notifyToUserList(final NotifyDataChange<List<Person>> notifyDataChange) {
        if (notifyDataChange != null) {
            if (userRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify user list"));
                return;
            }
            userList.clear();

            userRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Person user;
                    if (dataSnapshot.child("lastName").getValue() != null) {
                        user = dataSnapshot.getValue(Person.class);
                        userList.add(user);
                        notifyDataChange.OnDataChanged(userList);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Person user = dataSnapshot.getValue(Person.class);
                    String parcelid = dataSnapshot.getKey();

                    for (int i = 0; i < userList.size(); i++) {
                        if (userList.get(i).getUserID().equals(parcelid)) {
                            userList.set(i, user);
                            break;
                        }
                    }
                    notifyDataChange.OnDataChanged(userList);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Person user = dataSnapshot.getValue(Person.class);
                    String parcelid = dataSnapshot.getKey();

                    for (int i = 0; i < userList.size(); i++) {
                        if (userList.get(i).getUserID() == parcelid) {
                            userList.remove(i);
                            break;
                        }
                    }
                    notifyDataChange.OnDataChanged(userList);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }
            };
            usersRef.addChildEventListener(userRefChildEventListener);
        }
    }


    public static void stopNotifyToUserList() {
        if (userRefChildEventListener != null) {
            usersRef.removeEventListener(userRefChildEventListener);
            userRefChildEventListener = null;
        }
    }

}