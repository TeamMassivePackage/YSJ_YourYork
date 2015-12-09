package com.example.julian.matthew.tamim.massivepackage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.julian.matthew.tamim.massivepackage.Model.PropertyModel;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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

public class PropertyListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private List<PropertyModel> propertyModelList;
    private ListView lvProperties;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_list);

        //CUSTOM BLUE TOOLBAR WITH ACTION BUTTONS
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        //SETTING UP DRAWERLAYOUT AND TOGGLE
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_property_list);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        //toggle.syncState();

        //SET UP NAVIGATION DRAWER AND DRAWER ITEM SELECTED LISTENER
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_property_list);
        navigationView.setNavigationItemSelectedListener(this);
        new JSONTask().execute("http://api.zoopla.co.uk/api/v1/property_listings.json?postcode=YO19&api_key=5a6jn94cwnbjgd6c6nmhtas3");

        lvProperties = (ListView)findViewById(R.id.lvProperties);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
        .cacheInMemory(true).cacheOnDisk(true)
        .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
        .defaultDisplayImageOptions(defaultOptions)
        .build();
        ImageLoader.getInstance().init(config); // Do it on Application start
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_property_list);
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
        if(id == R.id.nav_home){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_house) {
            // Handle the camera action
        } /*else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_property_list);
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

    public class JSONTask extends AsyncTask<String, String, List<PropertyModel>> {
        @Override
        protected List<PropertyModel> doInBackground(String... urls) {
            String returnedJson = receiveJSON(urls[0]);
            propertyModelList = new ArrayList<>();
            try {
                JSONObject parentObject = new JSONObject(returnedJson);
                JSONArray listingArray = parentObject.getJSONArray("listing");

                Gson gson = new Gson();
                for(int i = 0; i < listingArray.length(); i++){
                    JSONObject finalObject = listingArray.getJSONObject(i);
                    PropertyModel propertyModel = gson.fromJson(finalObject.toString(), PropertyModel.class);
                    propertyModelList.add(propertyModel);
                }
                return propertyModelList;
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<PropertyModel> s) {
            super.onPostExecute(s);
            Log.e("Property model list:", propertyModelList.toString());
            PropertyAdapter adapter = new PropertyAdapter(getApplicationContext(), R.layout.property_list_row, s);
            lvProperties.setAdapter(adapter);


        }
    }

    public class PropertyAdapter extends ArrayAdapter{

        private List<PropertyModel> propertyObjects;
        private int resource;
        private LayoutInflater inflater;
        public PropertyAdapter(Context context, int resource, List<PropertyModel> objects) {
            super(context, resource, objects);
            propertyObjects = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if(convertView == null){
                Log.e("Convertview is null","NUUUUUUUULLLLLLLLLLLLLLLLLLL");
                convertView = inflater.inflate(resource,null);

                holder = new ViewHolder();
                holder.iv_mainImage = (ImageView)convertView.findViewById(R.id.iv_mainImage);
                holder.iv_agentLogo = (ImageView)convertView.findViewById(R.id.iv_agentLogo);

                holder.tv_status = (TextView)convertView.findViewById(R.id.tv_status);
                holder.tv_address = (TextView)convertView.findViewById(R.id.tv_address);
                holder.tv_dateAdded = (TextView)convertView.findViewById(R.id.tv_dateAdded);
                holder.tv_price = (TextView)convertView.findViewById(R.id.tv_price);
                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder)convertView.getTag();
            }





            final ProgressBar progressBarMainImage = (ProgressBar)convertView.findViewById(R.id.progressBar);
            final ProgressBar progressBarAgentImage = (ProgressBar)convertView.findViewById(R.id.progressBar2);


            ImageLoader.getInstance().displayImage(propertyObjects.get(position).getImage_url(), holder.iv_mainImage, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBarMainImage.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progressBarMainImage.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBarMainImage.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    progressBarMainImage.setVisibility(View.GONE);
                }
            }); // Default options will be used
            ImageLoader.getInstance().displayImage(propertyObjects.get(position).getAgent_logo(), holder.iv_agentLogo, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBarAgentImage.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progressBarAgentImage.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBarAgentImage.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    progressBarAgentImage.setVisibility(View.GONE);
                }
            });

            if(propertyObjects.get(position).getStatus().equalsIgnoreCase("for_sale")){
                holder.tv_status.setText("For Sale");
            }
            else{
                holder.tv_status.setText(propertyObjects.get(position).getStatus());
            }
            holder.tv_address.setText(propertyObjects.get(position).getDisplayable_address());
            holder.tv_dateAdded.setText(propertyObjects.get(position).getFirst_published_date());
            holder.tv_price.setText("£"+propertyObjects.get(position).getPrice());

            return convertView;
        }

        class ViewHolder{
            ImageView iv_mainImage;
            TextView tv_status;
            TextView tv_address;
            TextView tv_dateAdded;
            ImageView iv_agentLogo;
            TextView tv_price;
        }
    }
}
