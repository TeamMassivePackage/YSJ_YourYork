package com.example.julian.matthew.tamim.massivepackage;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class PropertyActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);

        //CUSTOM BLUE TOOLBAR WITH ACTION BUTTONS
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        //SETTING UP DRAWERLAYOUT AND TOGGLE
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_property);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //SET UP NAVIGATION DRAWER AND DRAWER ITEM SELECTED LISTENER
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_property);
        navigationView.setNavigationItemSelectedListener(this);

        Spinner radius_spinner = (Spinner)findViewById(R.id.spinner_radius);
        ArrayAdapter<CharSequence> radiusAdapter = ArrayAdapter.createFromResource(this,
                R.array.radius_array, android.R.layout.simple_spinner_item);
        radiusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        radius_spinner.setAdapter(radiusAdapter);

        Spinner bedMin_spinner = (Spinner)findViewById(R.id.spinner_beds_min);
        ArrayAdapter<CharSequence> bedsMinAdapter = ArrayAdapter.createFromResource(this,
                R.array.bedsMin_array, android.R.layout.simple_spinner_item);
        bedsMinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bedMin_spinner.setAdapter(bedsMinAdapter);

        Spinner bedMax_spinner = (Spinner)findViewById(R.id.spinner_beds_max);
        ArrayAdapter<CharSequence> bedsMaxAdapter = ArrayAdapter.createFromResource(this,
                R.array.bedsMax_array, android.R.layout.simple_spinner_item);
        bedsMaxAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bedMax_spinner.setAdapter(bedsMaxAdapter);

        Spinner priceMin_spinner = (Spinner)findViewById(R.id.spinner_price_min);
        ArrayAdapter<CharSequence> priceMinAdapter = ArrayAdapter.createFromResource(this,
                R.array.priceMin_array, android.R.layout.simple_spinner_item);
        priceMinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceMin_spinner.setAdapter(priceMinAdapter);

        Spinner priceMax_spinner = (Spinner)findViewById(R.id.spinner_price_max);
        ArrayAdapter<CharSequence> priceMaxAdapter = ArrayAdapter.createFromResource(this,
                R.array.priceMax_array, android.R.layout.simple_spinner_item);
        priceMaxAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceMax_spinner.setAdapter(priceMaxAdapter);

        Spinner added_spinner = (Spinner)findViewById(R.id.spinner_added);
        ArrayAdapter<CharSequence> addedAdapter = ArrayAdapter.createFromResource(this,
                R.array.added_array, android.R.layout.simple_spinner_item);
        addedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        added_spinner.setAdapter(addedAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_property);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_house) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_property);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_property, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            return true;
        }
        return false;
    }
}
