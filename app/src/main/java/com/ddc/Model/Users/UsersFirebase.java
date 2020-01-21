package com.ddc.Model.Users;

import androidx.annotation.NonNull;

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
import java.util.List;

public class UsersFirebase {
    static DatabaseReference usersRef;
    static List<User> userList;
    static Person user;

    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");
        userList = new ArrayList<>();
        user = new Person();
    }


    public static void addUser(final User user, final Action<String> action) {
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


    public static void removeParcel(String userid, final Action<String> action) {
        final String key = userid;


        usersRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User value = dataSnapshot.getValue(User.class);
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
    public static void notifyToUserList(final NotifyDataChange<List<User>> notifyDataChange) {
        if (notifyDataChange != null) {
            if (userRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify user list"));
                return;
            }
            userList.clear();

            userRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    User user;
                    if (dataSnapshot.child("lastName").getValue() != null)
                        user = dataSnapshot.getValue(Person.class);
                    else
                        user = dataSnapshot.getValue(Company.class);


                    userList.add(user);
                    // UsersManager.setUsersList(userList);
                    notifyDataChange.OnDataChanged(userList);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    User user = dataSnapshot.getValue(User.class);
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
                    User user = dataSnapshot.getValue(User.class);
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

    public static void getUser(String id ,final NotifyDataChange<Person> notifyDataChange){
        DatabaseReference userRef = usersRef.child(id);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(Person.class);
                notifyDataChange.OnDataChanged(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Don't ignore errors!
            }
        };

        userRef.addListenerForSingleValueEvent(valueEventListener);
    }


    public static void stopNotifyToUserList() {
        if (userRefChildEventListener != null) {
            usersRef.removeEventListener(userRefChildEventListener);
            userRefChildEventListener = null;
        }
    }
}