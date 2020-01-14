package com.ddc.UI.home;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ddc.Model.Person.Person;
import com.ddc.Model.Person.PersonRepository;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private PersonRepository repository;
    private LiveData<List<Person>> allPerson;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = new PersonRepository(application);
        allPerson = repository.getAllPersons();
    }

    public void insert(Person person) {
        repository.insert(person);
    }

    public void update(Person person) {
        repository.update(person);
    }

    public void delete(Person person) {
        repository.delete(person);
    }

    public void deleteAllPerson() {
        repository.deleteAllPerson();
    }

    public LiveData<List<Person>> getAllnotes() {
        return allPerson;
    }
}