package com.ddc.Model.Parcel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.ddc.Model.Action;
import com.ddc.Model.NotifyDataChange;
import com.ddc.Model.RepositoryState;
import com.ddc.Utils.ConnectionCheck;

import java.util.List;

public class ParcelRepository {

    private ParcelDAO parcelDao;
    private LiveData<List<Parcel>> allParcels;
    protected RepositoryState state;
    private Context appContext;
    private String Id;

    // main constructor
    public ParcelRepository(Application application) {
        ParcelDatabase database = ParcelDatabase.getInstance(application);
        parcelDao = database.dao();
        SharedPreferences sharedPreferences = application.getApplicationContext().getSharedPreferences("com.DDC.LastLoginData", Context.MODE_PRIVATE);

        Id = sharedPreferences.getString("LastLoginUserID", null);

        appContext = application.getApplicationContext();
        updateState();
        allParcels = parcelDao.getAllParcels();
        // the data need to be taking from the Firebase

        ParcelFirebase.notifyToUserParcelList(Id, new NotifyDataChange<List<Parcel>>() {
            @Override
            public void OnDataChanged(List<Parcel> obj) {
                new DeleteAllParcelAsyncTask(parcelDao, RepositoryState.DATABASE).execute();
                for (Parcel parcel : obj)
                    try {
                        new InsertParcelAsyncTask(parcelDao, RepositoryState.DATABASE).execute(parcel);
                    } catch (Exception e) {
                    }
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
    }


    // decide if the data need to be imported from the phone or from the Firebase
    public void updateState() {
        if (ConnectionCheck.isOnline(appContext)) {
            state = RepositoryState.BOTH;
            synchronizeData();
        } else {
            state = RepositoryState.DATABASE;
            synchronizeData();
        }
    }


    // synchronize the Room and the Firebase
    private void synchronizeData() {
        try {
            // upload all the data from the room to the Firebase
            if (state == RepositoryState.FIREBASE) {
                for (Parcel parcel : parcelDao.getAllParcels().getValue()) {
                    ParcelFirebase.addParcel(parcel, null);
                }

                // download the parcels from the Firebase to the Room
            } else {
                parcelDao.deleteAllParcels();
                for (Parcel parcel : ParcelFirebase.getParcelList()) {
                    parcelDao.insert(parcel);
                }
            }
        } catch (Exception e) {
        }
    }


    // functions
    public void insert(Parcel parcel) {
        new InsertParcelAsyncTask(parcelDao, state).execute(parcel);
    }

    public void update(Parcel parcel) {
        new UpdateParcelAsyncTask(parcelDao, state).execute(parcel);
    }

    public void delete(Parcel parcel) {
        new DeleteParcelAsyncTask(parcelDao, state).execute(parcel);
    }

    public void deleteAllParcel() {
        new DeleteAllParcelAsyncTask(parcelDao, state).execute();
    }

    public LiveData<List<Parcel>> getAllParcels() {
        return allParcels;
    }


    // inner classes
    public static class InsertParcelAsyncTask extends AsyncTask<Parcel, Void, Void> {
        private ParcelDAO parcelDao;
        private RepositoryState repositorystate;

        public InsertParcelAsyncTask(ParcelDAO parcelDao, RepositoryState repositorystate) {
            this.parcelDao = parcelDao;
            this.repositorystate = repositorystate;
        }

        @Override
        protected Void doInBackground(Parcel... parcels) {
            if(repositorystate == RepositoryState.FIREBASE)
            {
                ParcelFirebase.addParcel(parcels[0], new Action<String>() {
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
                parcelDao.insert(parcels[0]);
            return null;
        }
    }

    public static class UpdateParcelAsyncTask extends AsyncTask<Parcel, Void, Void> {
        private ParcelDAO parcelDao;
        private RepositoryState repositorystate;

        public UpdateParcelAsyncTask(ParcelDAO parcelDao, RepositoryState repositorystate) {
            this.parcelDao = parcelDao;
            this.repositorystate = repositorystate;
        }

        @Override
        protected Void doInBackground(Parcel... parcels) {
            if (repositorystate == RepositoryState.FIREBASE || repositorystate == RepositoryState.BOTH)
            {
                ParcelFirebase.updateParcel(parcels[0], new Action<String>() {
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
            if (repositorystate == RepositoryState.DATABASE || repositorystate == RepositoryState.BOTH)
                parcelDao.update(parcels[0]);
            return null;
        }
    }

    public static class DeleteAllParcelAsyncTask extends AsyncTask<Parcel, Void, Void> {
        private ParcelDAO parcelDao;
        private RepositoryState repositorystate;

        public DeleteAllParcelAsyncTask(ParcelDAO parcelDao, RepositoryState repositorystate) {
            this.parcelDao = parcelDao;
            this.repositorystate = repositorystate;
        }

        @Override
        protected Void doInBackground(Parcel... parcels) {
            if (repositorystate == RepositoryState.FIREBASE)
            {
                // TODO: להוסיף פונקציה שמוחקת את כל האנשים
            }
            else
                parcelDao.deleteAllParcels();
            return null;
        }
    }

    public static class DeleteParcelAsyncTask extends AsyncTask<Parcel, Void, Void> {
        private ParcelDAO parcelDao;
        private RepositoryState repositorystate;

        public DeleteParcelAsyncTask(ParcelDAO parcelDao, RepositoryState repositorystate) {
            this.parcelDao = parcelDao;
            this.repositorystate = repositorystate;
        }

        @Override
        protected Void doInBackground(Parcel... parcels) {
            if(repositorystate == RepositoryState.FIREBASE)
            {
                ParcelFirebase.removeParcel(parcels[0], new Action<List<Parcel>>() {
                    @Override
                    public void onSuccess(List<Parcel> obj) {

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
                parcelDao.delete(parcels[0]);
            return null;
        }
    }
}
