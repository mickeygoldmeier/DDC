package com.ddc.Model.Parcel;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ParcelDAO {

    @Insert
    void insert(Parcel parcel);

    @Update
    void update(Parcel parcel);

    @Delete
    void delete(Parcel parcel);

    @Query("DELETE FROM parcels_table")
    void deleteAllParcels();

    @Query("SELECT * FROM parcels_table")
    LiveData<List<Parcel>> getAllParcels();
}
