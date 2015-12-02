package com.example.julian.matthew.tamim.massivepackage;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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

import com.example.julian.matthew.tamim.massivepackage.Model.CatchmentModel;
import com.example.julian.matthew.tamim.massivepackage.Model.ColdCallingModel;
import com.example.julian.matthew.tamim.massivepackage.Model.CrimeModel;
import com.example.julian.matthew.tamim.massivepackage.Model.SchoolModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

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

    private ProgressDialog dialog;
    private Toolbar toolbar;
    private GoogleMap mMap;
    private List<CrimeModel> crimeModelList;
    private List<SchoolModel> schoolModelList;
    private List<ColdCallingModel> coldCallingModelList;
    private List<CatchmentModel> catchmentModelList;

    private List<Marker> crimeMarkers = new ArrayList<>();
    private List<Marker> primarySchoolMarkers = new ArrayList<>();
    private List<Marker> secondarySchoolMarkers = new ArrayList<>();
    private List<Polyline> coldCallingPolylineList = new ArrayList<>();
    private List<Polygon> primaryCatchmentPolygonList = new ArrayList<>();
    private List<Polygon> secondaryCatchmentPolygonList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading, Please Wait...");
        dialog.show();

        //GET PRIMARY AND SECONDARY JSON FROM FILES
        String schoolJson = loadJSONFromAsset(R.string.SCHOOL, "p");
        String secondarySchoolSJson = loadJSONFromAsset(R.string.SCHOOL, "s");


        //GET COLD CALLING JSON FROM FILE
        String coldCallingJson = loadJSONFromAsset(R.string.COLD_CALLING, null);


        //GET CATCHMENT JSON FROM FILES
        String primaryCatchmentJson = loadJSONFromAsset(R.string.CATCHMENT, "p");
        String secondaryCatchmentJson = loadJSONFromAsset(R.string.CATCHMENT, "s");

        /*for (String l: schoolJson.split(System.getProperty("line.separator"))){
            Log.e("line", l);
        }*/

        /*for (String l: secondaryCatchmentJson.split(System.getProperty("line.separator"))) {
            Log.e("line", l);
        }*/

        //PARSE PRIMARY AND SECONDARY SCHOOL DATA
        schoolModelList = new ArrayList<>();
        parseSchoolData(schoolJson, 'p');
        parseSchoolData(secondarySchoolSJson, 's');


        //PARSE COLD CALLING DATA
        coldCallingModelList = new ArrayList<>();
        parseColdCallingJson(coldCallingJson);


        //PARSE CATCHMENT DATA FOR PRIMARY AND SECONDARY
        catchmentModelList = new ArrayList<>();
        parseCatchmentData(primaryCatchmentJson, 'p');
        parseCatchmentData(secondaryCatchmentJson, 's');


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
    }

    private void parseCatchmentData(String returnedJson, char schoolType) {
        try {
            JSONObject parentObject = new JSONObject(returnedJson);
            JSONArray featuresArray = parentObject.getJSONArray("features");
            for(int i = 0; i < featuresArray.length(); i++){
                //Log.e("Loop i:", ""+i + ", length: " + featuresArray.length());
                CatchmentModel catchmentModel = new CatchmentModel();
                JSONObject finalObject = featuresArray.getJSONObject(i);
                JSONObject propertiresObject = finalObject.getJSONObject("properties");
                catchmentModel.setId(propertiresObject.getInt("OBJECTID"));
                catchmentModel.setSchoolName(propertiresObject.getString("SCHNAME"));
                catchmentModel.setWebsite(propertiresObject.getString("WEBSITE"));
                catchmentModel.setSchoolType(schoolType);

                JSONObject geometryObject = finalObject.getJSONObject("geometry");
                JSONArray coordinatesArray = geometryObject.getJSONArray("coordinates");
                String polygonType = geometryObject.getString("type");
                if(schoolType == 'p'){
                    //Log.e("This primary school:", ""+i + catchmentModel.getSchoolName());
                    List<CatchmentModel.Coordinates> coordinatesList = new ArrayList<>();
                    for(int j = 0; j < coordinatesArray.length(); j++){
                        JSONArray intermediateArray = coordinatesArray.getJSONArray(j);
                        if(polygonType.equalsIgnoreCase("Polygon")){
                            for(int k = 0; k < intermediateArray.length(); k++){
                                JSONArray finalArray = intermediateArray.getJSONArray(k);
                                Double lat = finalArray.getDouble(1);
                                Double lng = finalArray.getDouble(0);
                                CatchmentModel.Coordinates temp =  new CatchmentModel.Coordinates();
                                temp.setLat(lat);
                                temp.setLng(lng);
                                coordinatesList.add(temp);
                            }
                        }
                        else if(polygonType.equalsIgnoreCase("MultiPolygon")){
                            for(int k = 0; k < intermediateArray.length(); k++){
                                JSONArray intermediateArray2 = intermediateArray.getJSONArray(k);
                                for(int l = 0; l < intermediateArray2.length(); l++){
                                    JSONArray finalArray = intermediateArray2.getJSONArray(l);
                                    Double lat = finalArray.getDouble(1);
                                    Double lng = finalArray.getDouble(0);
                                    CatchmentModel.Coordinates temp = new CatchmentModel.Coordinates();
                                    temp.setLat(lat);
                                    temp.setLng(lng);
                                    coordinatesList.add(temp);
                                    //Log.e("School:", catchmentModel.getSchoolName() + " ->> " + temp.getLat()  + ", " +  temp.getLng());
                                }

                            }
                        }

                    }
                    catchmentModel.setCoordinatesList(coordinatesList);
                }
                else if(schoolType == 's'){
                    //Log.e("This secondary school:", ""+i + catchmentModel.getSchoolName());
                    List<CatchmentModel.Coordinates> coordinatesList = new ArrayList<>();
                    for(int j = 0; j < coordinatesArray.length(); j++){
                        JSONArray intermediateArray = coordinatesArray.getJSONArray(j);
                        if(polygonType.equalsIgnoreCase("Polygon")){
                            for(int k = 0; k < intermediateArray.length(); k++){
                                JSONArray finalArray = intermediateArray.getJSONArray(k);
                                Double lat = finalArray.getDouble(1);
                                Double lng = finalArray.getDouble(0);
                                CatchmentModel.Coordinates temp =  new CatchmentModel.Coordinates();
                                temp.setLat(lat);
                                temp.setLng(lng);
                                coordinatesList.add(temp);
                            }
                        }
                        else if(polygonType.equalsIgnoreCase("MultiPolygon")){
                            for(int k = 0; k < intermediateArray.length(); k++){
                                JSONArray intermediateArray2 = intermediateArray.getJSONArray(k);
                                for(int l = 0; l < intermediateArray2.length(); l++){
                                    JSONArray finalArray = intermediateArray2.getJSONArray(l);
                                    Double lat = finalArray.getDouble(1);
                                    Double lng = finalArray.getDouble(0);
                                    CatchmentModel.Coordinates temp = new CatchmentModel.Coordinates();
                                    temp.setLat(lat);
                                    temp.setLng(lng);
                                    coordinatesList.add(temp);
                                    //Log.e("School:", catchmentModel.getSchoolName() + " ->> " + temp.getLat()  + ", " +  temp.getLng());
                                }
                            }

                        }

                    }
                    catchmentModel.setCoordinatesList(coordinatesList);
                }



                //ADD FINAL CATCHMENT OBJECT TO CATCHMENT LIST
                catchmentModelList.add(catchmentModel);
            }
        } catch (Exception e) {
            Log.e("test", "exception " + e);
        }
    }

    public void showCatchmentData(){
        for(int i = 0; i < catchmentModelList.size(); i++){
            CatchmentModel cm = catchmentModelList.get(i);
            if(cm.getSchoolType() == 'p'){
                List<LatLng> pointList = new ArrayList<>();
                for(int j = 0; j < cm.getCoordinatesList().size(); j++){
                    pointList.add(new LatLng(cm.getCoordinatesList().get(j).getLat(), cm.getCoordinatesList().get(j).getLng()));
                }
                Polygon polygon = mMap.addPolygon(new PolygonOptions()
                        .addAll(pointList)
                        .strokeWidth(5)
                        .strokeColor(Color.RED)
                        .fillColor(Color.rgb(208,247,198)));
                primaryCatchmentPolygonList.add(polygon);
            }
            else if(cm.getSchoolType() == 's'){
                List<LatLng> pointList = new ArrayList<>();
                for(int j = 0; j < cm.getCoordinatesList().size(); j++){
                    pointList.add(new LatLng(cm.getCoordinatesList().get(j).getLat(), cm.getCoordinatesList().get(j).getLng()));
                }
                Polygon polygon = mMap.addPolygon(new PolygonOptions()
                        .addAll(pointList)
                        .strokeWidth(5)
                        .strokeColor(Color.RED)
                        .fillColor(Color.rgb(239,201,245)));
                secondaryCatchmentPolygonList.add(polygon);
            }

        }
    }

    private void parseColdCallingJson(String returnedJson) {
        try {
            JSONObject parentObject = new JSONObject(returnedJson);
            JSONArray featuresArray = parentObject.getJSONArray("features");
            for(int i = 0; i < featuresArray.length(); i++){
                ColdCallingModel coldCallingModel = new ColdCallingModel();

                JSONObject finalObject = featuresArray.getJSONObject(i);
                JSONObject propertiresObject = finalObject.getJSONObject("properties");
                coldCallingModel.setId(propertiresObject.getInt("OBJECTID"));
                coldCallingModel.setZones(propertiresObject.getString("ZONES"));
                coldCallingModel.setWard(propertiresObject.getString("WARD"));

                JSONObject geometryObject = finalObject.getJSONObject("geometry");
                JSONArray coordinatesArray = geometryObject.getJSONArray("coordinates");
                List<ColdCallingModel.Coordinates> coordinatesList = new ArrayList<>();
                for(int j = 0; j < coordinatesArray.length(); j++){
                    JSONArray intermediateArray = coordinatesArray.getJSONArray(j);
                    for(int k = 0; k < intermediateArray.length(); k++){
                        JSONArray finalArray = intermediateArray.getJSONArray(k);
                        Double lat = finalArray.getDouble(1);
                        Double lng = finalArray.getDouble(0);
                        ColdCallingModel.Coordinates temp = new ColdCallingModel.Coordinates();
                        temp.setLat(lat);
                        temp.setLng(lng);
                        coordinatesList.add(temp);
                    }
                }

                coldCallingModel.setCoordinatesList(coordinatesList);

                //ADD FINAL COLD COLLING OBJECT TO LIST
                coldCallingModelList.add(coldCallingModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showColdCallingData(){
        for(int i = 0; i < coldCallingModelList.size(); i++){
            ColdCallingModel ccm = coldCallingModelList.get(i);
            List<LatLng> pointList = new ArrayList<>();
            for(int j = 0; j < ccm.getCoordinatesList().size(); j++){

                //Log.e("Cold Calling data:", ccm.getZones() + " - " + ccm.getCoordinatesList().get(j).getLat() + ", " + ccm.getCoordinatesList().get(j).getLng());
                pointList.add(new LatLng(ccm.getCoordinatesList().get(j).getLat(), ccm.getCoordinatesList().get(j).getLng()));
            }
            Polyline polygon = mMap.addPolyline(new PolylineOptions()
                    .addAll(pointList)
                    .width(5)
                    .color(Color.BLUE));
                    //.fillColor(Color.rgb(152, 213, 237)));
            coldCallingPolylineList.add(polygon);
        }

        //polygon.setPoints(pointList);
    }

    public String loadJSONFromAsset(int type, String schoolType) {
        String bufferedReaderFile = null;
        switch (type) {
            case R.string.SCHOOL: {
                if (schoolType.equalsIgnoreCase("p")) {
                    bufferedReaderFile = "Primary_Schools.geojson";
                } else if (schoolType.equalsIgnoreCase("s")) {
                    bufferedReaderFile = "Secondary_Schools.geojson";
                }

                break;
            }
            case R.string.COLD_CALLING:{
                bufferedReaderFile = "Cold_Calling_Controlled_Zones.geojson";
                break;
            }
            case R.string.CATCHMENT: {
                if (schoolType.equalsIgnoreCase("p")) {
                    bufferedReaderFile = "Primary_school_catchments.geojson";
                } else if (schoolType.equalsIgnoreCase("s")) {
                    bufferedReaderFile = "Secondary_school_catchments.geojson";
                }

                break;
            }
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(bufferedReaderFile), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String receiveJSON(String urls) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        URL url = null;
        try {
            url = new URL(urls);
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
            return returnedJson;
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
        return null; //RETURN NULL IF IT DOESNT WORK
    }

    private void parseSchoolData(String returnedJson, char schoolType) {
        try {
            JSONObject parentObject = new JSONObject(returnedJson);


            JSONArray featuresArray = parentObject.getJSONArray("features");
            for (int i = 0; i < featuresArray.length(); i++) {
                JSONObject finalObject = featuresArray.getJSONObject(i);
                SchoolModel schoolModel = new SchoolModel();

                JSONObject propertiesObject = finalObject.getJSONObject("properties");
                schoolModel.setId(propertiesObject.getInt("OBJECTID_1"));
                schoolModel.setSchoolType(schoolType);
                schoolModel.setSchoolName(propertiesObject.getString("SCHNAME"));
                schoolModel.setWard(propertiesObject.getString("WARD"));
                schoolModel.setLocation(propertiesObject.getString("LV_DETAILS"));
                schoolModel.setWebsite(propertiesObject.getString("WEBSITE"));

                JSONObject geometryObject = finalObject.getJSONObject("geometry");
                JSONArray coordinatesArray = geometryObject.getJSONArray("coordinates");
                for (int j = 0; j < coordinatesArray.length(); j++) {
                    JSONArray finalArray = coordinatesArray.getJSONArray(j);
                    for (int k = 0; k < finalArray.length(); k++) {
                        LatLng temp = new LatLng(finalArray.getDouble(1), finalArray.getDouble(0));
                        schoolModel.setCoordinates(temp);
                    }
                }
                schoolModelList.add(schoolModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showSchoolDataJSON() {
        for (int i = 0; i < schoolModelList.size(); i++) {
            if (schoolModelList.get(i).getCoordinates() != null) {
                //Log.e("Show school data json:", schoolModelList.get(i).getSchoolName() + " >> " + schoolModelList.get(i).getLocation() + " --- " + schoolModelList.get(i).getCoordinates());
                if (schoolModelList.get(i).getSchoolType() == 'p') {
                    Marker schoolM = mMap.addMarker(new MarkerOptions()
                            .position(schoolModelList.get(i).getCoordinates())
                            .title(schoolModelList.get(i).getSchoolName())
                            .snippet(schoolModelList.get(i).getLocation())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_school_red_600_18dp)));
                    primarySchoolMarkers.add(schoolM);
                } else if (schoolModelList.get(i).getSchoolType() == 's') {
                    Marker schoolM = mMap.addMarker(new MarkerOptions()
                            .position(schoolModelList.get(i).getCoordinates())
                            .title(schoolModelList.get(i).getSchoolName())
                            .snippet(schoolModelList.get(i).getLocation())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_school_deep_purple_a700_18dp)));
                    secondarySchoolMarkers.add(schoolM);
                }

            } else {
                Log.e("Null school result:", schoolModelList.get(i).getSchoolName() + " >> " + schoolModelList.get(i).getLocation() + " --- " + schoolModelList.get(i).getCoordinates());
            }
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
    }

    private void showCrimeDataJSON() {
        for (int i = 0; i < crimeModelList.size(); i++) {
            Double locationLat = Double.parseDouble(crimeModelList.get(i).getLatitude());
            Double locationLng = Double.parseDouble(crimeModelList.get(i).getLongitude());
            String category = crimeModelList.get(i).getCategory();
            String streetName = crimeModelList.get(i).getStreet_name();
            String month = crimeModelList.get(i).getMonth();

            Marker crimeM = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(locationLat, locationLng))
                    .title(category)
                    .snippet(streetName + "\nReported On: " + month)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            crimeMarkers.add(crimeM);
        }
    }

    private void toggleMapMarkers(int type, String show, int schoolType) {
        switch (type) {
            case R.string.CRIME: {
                if (show.equals("s")) {
                    for (Marker m : crimeMarkers) {
                        m.setVisible(true);

                    }
                    break;
                } else if (show.equals("h")) {
                    for (Marker m : crimeMarkers) {
                        m.setVisible(false);
                    }
                    break;
                }
            }
            case R.string.SCHOOL: {
                switch (schoolType){
                    case R.string.PRIMARY: {
                        if (show.equals("s")) {
                            for (Marker m : primarySchoolMarkers) {
                                m.setVisible(true);
                            }
                            break;
                        } else if (show.equals("h")) {
                            for (Marker m : primarySchoolMarkers) {
                                m.setVisible(false);
                            }
                            break;
                        }
                        return;
                    }
                    case R.string.SECONDARY: {
                        if (show.equals("s")) {
                            for (Marker m : secondarySchoolMarkers) {
                                m.setVisible(true);
                            }
                            break;
                        } else if (show.equals("h")) {
                            for (Marker m : secondarySchoolMarkers) {
                                m.setVisible(false);
                            }
                            break;
                        }
                        break;
                    }
                }
                break;
            }

            case R.string.CATCHMENT: {
                switch (schoolType){
                    case R.string.PRIMARY: {
                        if (show.equals("s")) {
                            for (Polygon m : primaryCatchmentPolygonList) {
                                m.setVisible(true);
                            }
                            break;
                        } else if (show.equals("h")) {
                            for (Polygon m : primaryCatchmentPolygonList) {
                                m.setVisible(false);
                            }
                            break;
                        }
                        break;
                    }
                    case R.string.SECONDARY: {
                        if (show.equals("s")) {
                            for (Polygon m : secondaryCatchmentPolygonList) {
                                m.setVisible(true);
                            }
                            break;
                        } else if (show.equals("h")) {
                            for (Polygon m : secondaryCatchmentPolygonList) {
                                m.setVisible(false);
                            }
                            break;
                        }
                        break;
                    }
                }
                break;
            }
            case R.string.COLD_CALLING: {
                if (show.equals("s")) {
                    for (Polyline p: coldCallingPolylineList) {
                        p.setVisible(true);
                    }
                    break;
                } else if (show.equals("h")) {
                    for (Polyline p: coldCallingPolylineList) {
                        p.setVisible(false);
                    }
                    break;
                }
            }
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

        if (id == R.id.nav_house) {
            // Handle the camera action
            Intent intent = new Intent(getApplicationContext(),PropertyActivity.class);
            startActivity(intent);
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

        switch (item.getItemId()) {
            case R.id.primary:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    //Code here that will start displaying data
                    toggleMapMarkers(R.string.SCHOOL, "s", R.string.PRIMARY);
                    return true;
                } else {
                    item.setChecked(false);
                    //Code here that will stop displaying data
                    toggleMapMarkers(R.string.SCHOOL, "h", R.string.PRIMARY);
                    return true;
                }
            case R.id.secondary:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    //Code here that will start displaying data
                    toggleMapMarkers(R.string.SCHOOL, "s", R.string.SECONDARY);
                    return true;
                } else {
                    item.setChecked(false);
                    //Code here that will stop displaying data
                    toggleMapMarkers(R.string.SCHOOL, "h", R.string.SECONDARY);
                    return true;
                }
            case R.id.primaryCatchment:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    //Code here that will start displaying data
                    toggleMapMarkers(R.string.CATCHMENT, "s", R.string.PRIMARY);
                    return true;
                } else {
                    item.setChecked(false);
                    //Code here that will stop displaying data
                    toggleMapMarkers(R.string.CATCHMENT, "h", R.string.PRIMARY);
                    return true;
                }

            case R.id.secondaryCatchment:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    //Code here that will start displaying data
                    toggleMapMarkers(R.string.CATCHMENT, "s", R.string.SECONDARY);
                    return true;
                } else {
                    item.setChecked(false);
                    //Code here that will stop displaying data
                    toggleMapMarkers(R.string.CATCHMENT, "h", R.string.SECONDARY);
                    return true;
                }
            case R.id.crime:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    //Code here that will start displaying data
                    toggleMapMarkers(R.string.CRIME, "s", 0);
                    return true;
                } else {
                    item.setChecked(false);
                    //Code here that will stop displaying data
                    toggleMapMarkers(R.string.CRIME, "h",0);
                    return true;
                }
            case R.id.calling:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    //Code here that will start displaying data
                    toggleMapMarkers(R.string.COLD_CALLING, "s",0);
                    return true;
                } else {
                    item.setChecked(false);
                    //Code here that will stop displaying data
                    toggleMapMarkers(R.string.COLD_CALLING, "h",0);
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
        new JSONTask(R.string.CRIME).execute("https://data.police.uk/api/crimes-at-location?date=2015-05&lat=53.958576&lng=-1.087460");
        //new JSONTask(R.string.CRIME).execute("https://data.police.uk/api/crimes-street/all-crime?lat=53.958576&lng=-1.087460&date=2015-05");
        // Add a marker in Sydney and move the camera
        LatLng york = new LatLng(53.958576, -1.087460);
        //mMap.addMarker(new MarkerOptions().position(york).title("Marker in York"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(york));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        showSchoolDataJSON();
        showColdCallingData();
        showCatchmentData();

    }

    public class JSONTask extends AsyncTask<String, String, Void> {
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
        protected Void doInBackground(String... urls) {

            String returnedJson = receiveJSON(urls[0]);

            //FROM THIS POINT ON IT WILL DIFFER FOR THE OTHER APIs
            switch (apiType) {
                case R.string.CRIME: {
                    //PARSE CRIME DATA
                    parseCrimeData(returnedJson);
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);

            dialog.dismiss();
            //CODE TO HANDLE THE MAIN THREAD UI
            switch (apiType) {
                case R.string.CRIME: {
                    //HANDLE ARRAYLIST OF CRIME OBJECTS - CHANGE THE MAP VIEW
                    Log.e("On Post ececute CRIME: ", "SHOWING CRIME DATA ------>>>>");
                    showCrimeDataJSON();
                    break;
                }
            }
        }
    }

}
