package com.ddc;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ddc.Model.Address;
import com.ddc.Model.Parcel.Parcel;
import com.ddc.Model.Parcel.ParcelRepository;
import com.ddc.Model.Parcel.Parcel_Type;
import com.ddc.Model.Users.Person;
import com.ddc.Model.Users.User;
import com.ddc.Model.Users.UsersFirebase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static Person person;

    static { person = null; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        NavigationView navigationView = findViewById(R.id.nav_view);
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

        Bundle bundle = getIntent().getExtras();
        List lst = UsersFirebase.getUsersList();

        //////////////////////////////////
        List<User> users = new ArrayList<>();
        Address address = new Address("Israel", "רחובות", "רמבם", 12);
        Person person1 = new Person("+972509791362", "123", Calendar.getInstance(), address, "נתן", "מנור");
        users.add(person1);
        //////////////////////////////////


        String personID = bundle.getString("UserID");
        for (User person : users)
            if (person.getUserID().equals(personID))
                this.person = (Person) person;

        View headerView = navigationView.getHeaderView(0);
        TextView personName = headerView.findViewById(R.id.person_name_tv);
        personName.setText(this.person.getFirstName() + " " + this.person.getLastName());
        TextView personPhone = headerView.findViewById(R.id.person_phone_tv);
        personPhone.setText(this.person.getUserID());


        /////////////////////////////////
        ParcelRepository parcelRepository = new ParcelRepository(getApplication());
        parcelRepository.insert(new Parcel(Parcel_Type.Envelope, true, 3, address, "+972509791362", "123456", "123"));
        /////////////////////////////////


        writeLoginToPhoneMemory(person, this);
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
    private void writeLoginToPhoneMemory(Person person, Activity activity)
    {
        SharedPreferences.Editor editor = getSharedPreferences("com.DDC.LastLoginData", MODE_PRIVATE).edit();
        editor.putString("LastLoginTime", Calendar.getInstance().getTime().toString());
        editor.putString("LastLoginUserID", person.getUserID());
        editor.apply();
    }
}
