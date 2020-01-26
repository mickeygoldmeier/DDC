package com.ddc.Model.Parcel;

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

public class HistoryParcelFirebase {

    static DatabaseReference parcelsRef;
    static List<Parcel> userparcelList;
    private static ChildEventListener parcelRefChildEventListener;

    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        parcelsRef = database.getReference("HistoryPackages");
        userparcelList = new ArrayList<>();
    }

    public static void addParcel(final Parcel parcel, final Action<String> action) {
        String phone = parcel.getRecipientPhone();
        String key = parcel.getParcelID();
        parcelsRef.child(phone + "/" + key).setValue(parcel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                action.onSuccess(parcel.getParcelID());
                action.onProgress("upload parcel data", 100);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                action.onFailure(e);
                action.onProgress("error upload parcel data", 100);

            }
        });
    }

    public static void removeParcel(Parcel parcel, final Action<String> action) {
        final String Phone = parcel.getRecipientPhone();
        final String key = parcel.getParcelID();

        parcelsRef.child(Phone + "/" + key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Parcel value = dataSnapshot.getValue(Parcel.class);
                if (value == null)
                    action.onFailure(new Exception("parcel not found ..."));
                else {
                    parcelsRef.child(Phone + "/" + key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            for (Parcel parcel : userparcelList) {
                                if (key.equals(parcel.getParcelID())) {
                                    userparcelList.remove(parcel);
                                    break;
                                }
                            }
                            action.onSuccess(value.getParcelID());
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

    public static void updateParcel(final Parcel toUpdate, final Action<String> action) {
        final String key = toUpdate.getParcelID();
        final String phone = toUpdate.getRecipientPhone();
        parcelsRef.child(phone + '/' + key).setValue(toUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                action.onSuccess(key);
                action.onProgress("updated parcel data", 100);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                action.onFailure(e);
            }
        });
    }



    public static void notifyToUserParcelList(String userId, final NotifyDataChange<List<Parcel>> notifyDataChange) {
        if (notifyDataChange != null) {
            if (parcelRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify parcel list"));
                return;
            }
            userparcelList.clear();
            DatabaseReference userparcelRef = parcelsRef.child(userId);
            parcelRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Parcel parcel = dataSnapshot.getValue(Parcel.class);
                    boolean flag = true;
                    for (int i = 0; i < userparcelList.size(); i++) {
                        if (userparcelList.get(i).getParcelID().equals(parcel.getParcelID())) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag)
                        userparcelList.add(parcel);

                    notifyDataChange.OnDataChanged(userparcelList);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {


                    Parcel parcel = dataSnapshot.getValue(Parcel.class);
                    boolean flag = true;
                    for (int i = 0; i < userparcelList.size(); i++) {
                        if (userparcelList.get(i).getParcelID().equals(parcel.getParcelID())) {
                            userparcelList.set(i, parcel);
                            flag = false;
                            break;
                        }
                    }
                    if (flag)
                        userparcelList.add(parcel);


                    notifyDataChange.OnDataChanged(userparcelList);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    String parcelid = dataSnapshot.getKey();

                    for (int i = 0; i < userparcelList.size(); i++) {
                        if (userparcelList.get(i).getParcelID() == parcelid) {
                            userparcelList.remove(i);
                            break;
                        }
                    }
                    notifyDataChange.OnDataChanged(userparcelList);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }
            };
            userparcelRef.addChildEventListener(parcelRefChildEventListener);
        }
    }



    public static void stopNotifyToParcelList() {
        if (parcelRefChildEventListener != null) {
            parcelsRef.removeEventListener(parcelRefChildEventListener);
            parcelRefChildEventListener = null;
        }
    }


    public static List<Parcel> getUserparcelList() {
        return userparcelList;
    }
}