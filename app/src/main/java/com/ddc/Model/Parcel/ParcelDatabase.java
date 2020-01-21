package com.ddc.Model.Parcel;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ddc.Model.Convectors;

@Database(entities = {Parcel.class}, version = 2)
@TypeConverters({Convectors.class})
public abstract class ParcelDatabase extends RoomDatabase {

    private static ParcelDatabase instance;

    public static synchronized ParcelDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), ParcelDatabase.class,
                    "parcels_table")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract ParcelDAO dao();
}
