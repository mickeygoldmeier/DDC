package com.ddc;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ddc.Model.Address;
import com.ddc.Model.NotifyDataChange;
import com.ddc.Model.Parcel.Parcel;
import com.ddc.Model.Parcel.ParcelRepository;
import com.ddc.Model.Parcel.Parcel_Type;
import com.ddc.Model.Users.Person;
import com.ddc.Model.Users.User;
import com.ddc.Model.Users.UsersFirebase;
import com.ddc.Model.Users.UsersManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static Person person;
    private List<User> users = new ArrayList<>();
    static { person = null; }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        final String personID = bundle.getString("UserID");
      //  UsersManager.getUserFromFirebase(personID);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_friends_parcels, R.id.nav_my_parcels, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        UsersFirebase.getUser(personID, new NotifyDataChange<Person>() {
            @Override
            public void OnDataChanged(Person obj) {
                person = obj;
                UsersManager.setUser(person);
                person = UsersManager.getUser();
                try {
                    Thread.sleep(2000);
                    person = (Person) UsersManager.getUser(personID);
                    View headerView = navigationView.getHeaderView(0);
                    TextView personName = headerView.findViewById(R.id.person_name_tv);
                    personName.setText(person.getFirstName() + " " + person.getLastName());
                    TextView personPhone = headerView.findViewById(R.id.person_phone_tv);
                    personPhone.setText(person.getUserID());
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
        users = UsersManager.getUsersList();


        for (User person : users)
            if (person.getUserID().equals(personID))
         //       this.person = (Person) person;





        writeLoginToPhoneMemory(personID, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // write the last login data on the phone memory using SharedPreferences
    private void writeLoginToPhoneMemory(String person, Activity activity)
    {
        SharedPreferences.Editor editor = getSharedPreferences("com.DDC.LastLoginData", MODE_PRIVATE).edit();
        editor.putString("LastLoginTime", Calendar.getInstance().getTime().toString());
        editor.putString("LastLoginUserID", person);
        editor.apply();
    }
}
