package com.example.zaki_berouk.savedbythebell;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zaki_berouk.savedbythebell.adapter.EventAdapter;
import com.example.zaki_berouk.savedbythebell.db_utils.DBHelper;
import com.example.zaki_berouk.savedbythebell.model.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<Event> events = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //On crée une instance de DBHelper on l'ouvre et on appelle la méthode qui contient la requête SQL
        final DBHelper dbHelper = DBHelper.getInstance(this);

        try {
            dbHelper.createDataBase();
            dbHelper.openDataBase();
            events = dbHelper.getAllEvent();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //On remplit la list view
        EventAdapter adapter = new EventAdapter(getApplicationContext(), R.layout.event_card, events);
        ListView list_event = (ListView) findViewById(R.id.event_lv);
        list_event.setAdapter(adapter);


        dbHelper.close();

        //Un peu sale mais on a un bouton qui va ouvrir un alert dialog qui va contenir le
        //formulaire d'ajout d'event
        Button add_event = (Button) findViewById(R.id.add_event_btn);
        add_event.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                final View alertLayout = inflater.inflate(R.layout.event_add_form, null);

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(true);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //String name, String date, String category, String time, String descr
                    }
                });

                alert.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        final EditText date = (EditText) alertLayout.findViewById(R.id.dateEvent);
                        final EditText time = (EditText) alertLayout.findViewById(R.id.timeEvent);
                        final EditText name = (EditText) alertLayout.findViewById(R.id.nameEvent);
                        final EditText location = (EditText) alertLayout.findViewById(R.id.locationEvent);
                        final EditText description = (EditText) alertLayout.findViewById(R.id.descrEvent);

                        String dateEvent = date.getText().toString() + " " + time.getText().toString();;
                        String nameEvent = name.getText().toString();
                        String locationEvent = location.getText().toString();
                        String descrEvent = description.getText().toString();

                        //DB : table event(id, name, date, location, descr, departure_time)
                        //Event (model) :  name, date, descr, location, departure_time
                        //String name, String date, String location, String descr, int id

                        Event new_Event = new Event(nameEvent, dateEvent, locationEvent , descrEvent);

                        try {
                            dbHelper.openDataBase();
                            dbHelper.addEventinDB(nameEvent, dateEvent, locationEvent, descrEvent, events.size() + 1);
                            dbHelper.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        events.add(new_Event);
                        EventAdapter adapter = new EventAdapter(getApplicationContext(), R.layout.event_card, events);
                        ListView list_event = (ListView) findViewById(R.id.event_lv);
                        list_event.setAdapter(adapter);

                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
