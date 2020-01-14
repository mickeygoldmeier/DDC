package com.ddc.Model.Person;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ddc.Model.Action;
import com.ddc.Model.DAO;
import com.ddc.Model.NotifyDataChange;
import com.ddc.Model.RepositoryState;

import java.util.List;

public class PersonRepository {

    private DAO dao;
    private LiveData<List<Person>> allPersons;
    protected RepositoryState state;

    // main constructor
    public PersonRepository(Application application) {
        PersonDatabase database = PersonDatabase.getInstance(application);
        dao = database.dao();

        updateState();
        // the data need to be taking from the Room
        if (state == RepositoryState.DATABASE)
            allPersons = dao.getAllPersons();
        // the data need to be taking from the Firebase
        else {
            PersonFirebase.notifyToUserList(new NotifyDataChange<List<Person>>() {
                @Override
                public void OnDataChanged(List<Person> obj) {
                    ((MutableLiveData)allPersons).setValue(obj);
                }

                @Override
                public void onFailure(Exception exception) {

                }
            });
            allPersons = PersonFirebase.personLiveData;
        }
    }


    // decide if the data need to be imported from the phone or from the Firebase
    public void updateState() {
        state = RepositoryState.FIREBASE;
    }


    // functions
    public void insert(Person person) {
        new InsertPersonAsyncTask(dao, state).execute(person);
    }

    public void update(Person person) {
        new UpdatePersonAsyncTask(dao, state).execute(person);
    }

    public void delete(Person person) {
        new DeletePersonAsyncTask(dao, state).execute(person);
    }

    public void deleteAllPerson() {
        new DeleteAllPersonAsyncTask(dao, state).execute();
    }

    public LiveData<List<Person>> getAllPersons() {
        return allPersons;
    }


    // inner classes
    public static class InsertPersonAsyncTask extends AsyncTask<Person, Void, Void> {
        private DAO dao;
        private RepositoryState repositorystate;

        public InsertPersonAsyncTask(DAO dao, RepositoryState repositorystate) {
            this.dao = dao;
            this.repositorystate = repositorystate;
        }

        @Override
        protected Void doInBackground(Person... people) {
            if(repositorystate == RepositoryState.FIREBASE)
            {
                PersonFirebase.addUser(people[0], new Action<String>() {
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
            }
            else
                dao.insert(people[0]);
            return null;
        }
    }

    public static class UpdatePersonAsyncTask extends AsyncTask<Person, Void, Void> {
        private DAO dao;
        private RepositoryState repositorystate;

        public UpdatePersonAsyncTask(DAO dao, RepositoryState repositorystate) {
            this.dao = dao;
            this.repositorystate = repositorystate;
        }

        @Override
        protected Void doInBackground(Person... people) {
            if(repositorystate == RepositoryState.FIREBASE)
            {
                // TODO: להוסיף פונקציה של עדכון של האנשים
            }
            else
                dao.update(people[0]);
            return null;
        }
    }

    public static class DeleteAllPersonAsyncTask extends AsyncTask<Person, Void, Void> {
        private DAO dao;
        private RepositoryState repositorystate;

        public DeleteAllPersonAsyncTask(DAO dao, RepositoryState repositorystate) {
            this.dao = dao;
            this.repositorystate = repositorystate;
        }

        @Override
        protected Void doInBackground(Person... people) {
            if (repositorystate == RepositoryState.FIREBASE)
            {
                // TODO: להוסיף פונקציה שמוחקת את כל האנשים
            }
            else
                dao.deleteAllPersons();
            return null;
        }
    }

    public static class DeletePersonAsyncTask extends AsyncTask<Person, Void, Void> {
        private DAO dao;
        private RepositoryState repositorystate;

        public DeletePersonAsyncTask(DAO dao, RepositoryState repositorystate) {
            this.dao = dao;
            this.repositorystate = repositorystate;
        }

        @Override
        protected Void doInBackground(Person... people) {
            if(repositorystate == RepositoryState.FIREBASE)
            {
                PersonFirebase.removeUser(people[0].getUserID(), new Action<String>() {
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
            }
            else
                dao.delete(people[0]);
            return null;
        }
    }
}
