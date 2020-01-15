package com.ddc.UI.FriendsParcels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ddc.Model.Parcel.Parcel;
import com.ddc.Model.Parcel.ParcelRepository;

import java.util.List;

public class FriendsParcelsViewModel extends AndroidViewModel {

    private ParcelRepository repository;
    private LiveData<List<Parcel>> allParcel;

    public FriendsParcelsViewModel(@NonNull Application application) {
        super(application);
        repository = new ParcelRepository(application);
        allParcel = repository.getAllParcels();
    }

    public void insert(Parcel parcel) {
        repository.insert(parcel);
    }

    public void update(Parcel parcel) {
        repository.update(parcel);
    }

    public void delete(Parcel parcel) {
        repository.delete(parcel);
    }

    public void deleteAllParcel() {
        repository.deleteAllParcel();
    }

    public LiveData<List<Parcel>> getAllParcels() {
        return allParcel;
    }
}