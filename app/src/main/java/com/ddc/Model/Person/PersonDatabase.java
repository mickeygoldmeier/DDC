package com.ddc.Model.Person;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ddc.Model.Convectors;
import com.ddc.Model.DAO;

@Database(entities = {Person.class}, version = 1)
@TypeConverters({Convectors.class})
public abstract class PersonDatabase extends RoomDatabase {

    public abstract DAO dao();

    // singleton
    private static PersonDatabase instance;
    public static synchronized PersonDatabase getInstance(Context context){
        if(instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(), PersonDatabase.class,
                    "persons_table")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
