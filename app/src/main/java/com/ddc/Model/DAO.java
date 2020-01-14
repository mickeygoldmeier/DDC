package com.ddc.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ddc.Model.Person.Person;

import java.util.List;

@Dao
public interface DAO {

    @Insert
    void insert(Person person);

    @Update
    void update(Person person);

    @Delete
    void delete(Person person);

    @Query("DELETE FROM persons_table")
    void deleteAllPersons();

    @Query("SELECT * FROM persons_table")
    LiveData<List<Person>> getAllPersons();
}
