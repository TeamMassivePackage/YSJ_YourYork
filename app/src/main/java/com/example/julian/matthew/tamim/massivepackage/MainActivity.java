package com.example.julian.matthew.tamim.massivepackage;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.julian.matthew.tamim.massivepackage.Model.CrimeModel;
import com.example.julian.matthew.tamim.massivepackage.Model.SchoolModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private GoogleMap mMap;
    private List<CrimeModel> crimeModelList;
    private List<SchoolModel> schoolModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //MapsInitializer.initialize(getApplicationContext());

        //CUSTOM BLUE TOOLBAR WITH ACTION BUTTONS
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        //SETTING UP GOOGLE MAP FRAGMENT
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //SETTING UP DRAWERLAYOUT AND TOGGLE
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //SET UP NAVIGATION DRAWER AND DRAWER ITEM SELECTED LISTENER
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //SET UP HTTP URL CONNECTION
        new JSONTask(R.string.CRIME).execute("https://data.police.uk/api/crimes-at-location?date=2015-05&lat=53.958576&lng=-1.087460");
        //new JSONTask(R.string.CRIME).execute("https://data.police.uk/api/crimes-street/all-crime?lat=53.958576&lng=-1.087460&date=2015-05");
        new JSONTask(R.string.SCHOOL, 'p').execute("https://data.yorkopendata.org/api/action/datastore_search?resource_id=8b8f1ad2-5faf-4238-a1d4-bdd4ce9a3401");

    }

    private void parseSchoolData(String returnedJson, char schoolType) {
        try {
            JSONObject parentObject = new JSONObject(returnedJson);
            schoolModelList = new ArrayList<>();
            if (parentObject.getString("success").equals("true")) {
                JSONObject resultObject = parentObject.getJSONObject("result");
                JSONArray recordsArray = resultObject.getJSONArray("records");
                for (int i = 0; i < recordsArray.length(); i++) {
                    JSONObject finalObject = recordsArray.getJSONObject(i);
                    SchoolModel schoolModel = new SchoolModel();
                    schoolModel.setId(finalObject.getInt("_id"));
                    schoolModel.setSchoolType(schoolType);
                    schoolModel.setWebsite(finalObject.getString("WEBSITE"));
                    schoolModel.setSchoolName(finalObject.getString("SCHNAME"));
                    schoolModel.setLocation(finalObject.getString("LV_DETAILS"));
                    schoolModel.setWard(finalObject.getString("WARD"));

                    //ADD SCHOOL OBJECT TO SCHOOL LIST
                    schoolModelList.add(schoolModel);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseCrimeData(String returnedJson) {

        try {
            JSONArray parentArray = new JSONArray(returnedJson);
            crimeModelList = new ArrayList<>();
            for (int i = 0; i < parentArray.length(); i++) {
                JSONObject finalObject = parentArray.getJSONObject(i);
                CrimeModel crimeModel = new CrimeModel();
                crimeModel.setId(finalObject.getInt("id"));
                crimeModel.setCategory(finalObject.getString("category"));
                crimeModel.setLocation_type(finalObject.getString("location_type"));
                JSONObject locationObject = finalObject.getJSONObject("location");
                crimeModel.setLatitude(locationObject.getString("latitude"));
                crimeModel.setLongitude(locationObject.getString("longitude"));
                crimeModel.setMonth(finalObject.getString("month"));

                JSONObject streetObject = locationObject.getJSONObject("street");
                crimeModel.setStreet_name(streetObject.getString("name"));

                //ADD crimeModel object to crimeModelList to make crime list
                crimeModelList.add(crimeModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //RETURN AN ARRAYLIST OF CRIME OBJECTS


    }

    private void showCrimeData() {
        for (int i = 0; i < crimeModelList.size(); i++) {
            Double locationLat = Double.parseDouble(crimeModelList.get(i).getLatitude());
            Double locationLng = Double.parseDouble(crimeModelList.get(i).getLongitude());
            String category = crimeModelList.get(i).getCategory();
            String streetName = crimeModelList.get(i).getStreet_name();
            String month = crimeModelList.get(i).getMonth();

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(locationLat, locationLng))
                    .title(category)
                    .snippet(streetName + "\nReported On: " + month)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Need to reference the API's or map here. Not entirely sure so will continue with this later

        switch (item.getItemId()) {
            case R.id.schools:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    //Code here that will start displaying data
                    return true;
                } else {
                    item.setChecked(false);
                    //Code here that will stop displaying data
                    return true;
                }
            case R.id.catchment:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    //Code here that will start displaying data
                    return true;
                } else {
                    item.setChecked(false);
                    //Code here that will stop displaying data
                    return true;
                }
            case R.id.crime:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    //Code here that will start displaying data
                    return true;
                } else {
                    item.setChecked(false);
                    //Code here that will stop displaying data
                    return true;
                }
            case R.id.calling:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    //Code here that will start displaying data
                    return true;
                } else {
                    item.setChecked(false);
                    //Code here that will stop displaying data
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng york = new LatLng(53.958576, -1.087460);
        //mMap.addMarker(new MarkerOptions().position(york).title("Marker in York"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(york));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
    }

    public class JSONTask extends AsyncTask<String, String, String> {
        int apiType;
        char schoolType;

        JSONTask(int type) {
            apiType = type;
        }

        JSONTask(int type, char schType) {
            apiType = type;
            schoolType = schType;
        }

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect(); //connecting directly to server

                InputStream stream = connection.getInputStream(); //response from server is input stream

                reader = new BufferedReader(new InputStreamReader(stream)); //buffered reader helps to read the input stream
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String returnedJson = buffer.toString();

                //FROM THIS POINT ON IT WILL DIFFER FOR THE OTHER APIs
                switch (apiType) {
                    case R.string.CRIME: {
                        //PARSE CRIME DATA
                        parseCrimeData(returnedJson);
                        break;
                    }
                    case R.string.SCHOOL: {
                        //PARSE SCHOOL DATA
                        parseSchoolData(returnedJson, schoolType);
                        break;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null; //IF IT DOESN'T MAKE A CONNECTION TO SERVER, RETURN NULL
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //CODE TO HANDLE THE MAIN THREAD UI
            switch (apiType) {
                case R.string.CRIME: {
                    //HANDLE ARRAYLIST OF CRIME OBJECTS - CHANGE THE MAP VIEW
                    Log.e("On Post ececute CRIME: ", "SHOWING CRIME DATA ------>>>>");
                    showCrimeData();
                    break;
                }
                case R.string.SCHOOL: {
                    //PARSE SCHOOL DATA
                    Log.e("On Post SCHOOL: ", "oighogihfriughirughlfghglskfjjkfghhfglkjdfhg");
                    break;
                }
            }
        }
    }

}
