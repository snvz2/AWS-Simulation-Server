package edu.sjsu.teamneon.hangrymobile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FoodTruckLocator extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    Button btnLoc;
    GPSTracker gps;
    private static final int PERMS_REQUEST_CODE = 123;
    private GoogleMap mMap;
    private ArrayList<Marker> truckList = new ArrayList<>();
    private RelativeLayout frameTruckList;
    private Marker locationMarker;
    private float defaultZoom;
    private static String currentLocation = null;

    //For list view
    private ArrayList<FoodTruck> infoArray = new ArrayList<FoodTruck>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Set resource values */
        defaultZoom = Float.parseFloat(getResources().getString(R.string.default_zoom));
        currentLocation = getResources().getString(R.string.current_location);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_truck_locator);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.truckMap);
        mapFragment.getMapAsync(this);
        /* Get the truck list frame layout handle */
        //frameTruckList = (RelativeLayout) findViewById(R.id.truckList);
        btnLoc = (Button) findViewById(R.id.gps);

        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePos();
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {
        // Clear all previously added markers from the map
        mMap.clear();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (locationMarker != null) {
            locationMarker.remove();
        }
        locationMarker = mMap.addMarker(
                new MarkerOptions().position(latLng).title(
                        currentLocation).icon(BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_AZURE)));
        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(defaultZoom);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        gps.locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void updatePos() {
        gps = new GPSTracker(FoodTruckLocator.this);

        gps.getLocation();
        requestPerms();
        if(gps.canGetLocation() && hasPermissions()) {

            //This try catch is to fix emulator GPS problem where first run doesn't provide coordinates
            try {
                onLocationChanged(gps.getLocation());
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(), "Emulator coordinates not set first run!", Toast.LENGTH_SHORT).show();
            }
            //gps.getLocation();
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            /* If we have prior markers, free them now */
            if (!truckList.isEmpty()) {
                truckList.clear();
            }
            /* Get food truck locations */
            new scanAllTrucks().execute();
            /* Add Markers for newly discovered food trucks */

/*
            Toast.makeText(
                    getApplicationContext(),
                    "Your Location is -\nLat: " + latitude + "\nLong: "
                            + longitude, Toast.LENGTH_LONG).show();*/

        } else {

            gps.showSettingsAlert();
            requestPerms();
        }
    }

/*
    private void CameraUpdate(){

    }*/


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

        /* Add a marker for test food truck and move camera */
        //mMap.addMarker(new MarkerOptions().position(testFoodTruck.getLatLng()).title(testFoodTruck.getName()));
        updatePos();
    }
    /**
     * Checks if Permissions are valid
     */
    private boolean hasPermissions(){
        int res = 0;
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        for (String perms : permissions){
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }
        return true;
    }

    /**
     * Displays dialog for user to enable locations permissions (ACCESS_FINE_LOCATION)
     */
    private void requestPerms(){
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions,PERMS_REQUEST_CODE);}

    }

    /**
     * Amazon AWS stuff goes down here
     */
    private class storeTruck extends AsyncTask<String, Integer, Integer>{
        private String truckName;
        private String truckLon;
        private String truckLat;
        public storeTruck(String name, String lon, String lat)
        {
            truckName = name;
            truckLon = lon;
            truckLat = lat;
        }
        @Override
        protected Integer doInBackground(String... params){

            //Instantiate manager class (Currently only has Dynamo) and get credentials for mapper
            ManagerClass managerClass = new ManagerClass();
            CognitoCachingCredentialsProvider credentialsProvider =
                    managerClass.getCredentials(FoodTruckLocator.this); //Pass in the activity name
            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

            //Get values through ID of field or Geo code
            //Takes in (name, lon, lat
            FoodTruck newTruck = new FoodTruck();
            newTruck.setLat(truckLat);
            newTruck.setLon(truckLon);
            newTruck.setName(truckName);
            mapper.save(newTruck);

            return null;
        }
    }

    private class updateTruck extends AsyncTask<Void, Integer, Integer>{
        @Override
        protected Integer doInBackground(Void... params){

            //Instantiate manager class (Currently only has Dynamo) and get credentials for mapper
            ManagerClass managerClass = new ManagerClass();
            CognitoCachingCredentialsProvider credentialsProvider =
                    managerClass.getCredentials(FoodTruckLocator.this); //Pass in the activity name
            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

            //Selects a truck based on primary key (id) to update
            FoodTruck truckToUpdate = mapper.load(FoodTruck.class, "2");
            truckToUpdate.setName("New truck");

            mapper.save(truckToUpdate);

            return null;
        }
    }

//    private class deleteTruck extends AsyncTask<Void, Integer, Integer> {
//        @Override
//        protected Integer doInBackground(Void... params){
//
//            //Instantiate manager class (Currently only has Dynamo) and get credentials for mapper
//            ManagerClass managerClass = new ManagerClass();
//            CognitoCachingCredentialsProvider credentialsProvider =
//                    managerClass.getCredentials(FoodTruckLocator.this); //Pass in the activity name
//            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
//            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);
//
//            //Selects a truck based on primary key (id) to delete
//            FoodTruck truckToDelete = mapper.load(FoodTruck.class, 6);
//
//            mapper.delete(truckToDelete);
//
//            return null;
//        }
//    }

    //Returns all the entry in the database
    private class scanAllTrucks extends AsyncTask<String, Void, Void>{
        private PaginatedScanList<FoodTruck> result;
        @Override
        protected Void doInBackground(String... params) {

            //Instantiate manager class (Currently only has Dynamo) and get credentials for mapper
            ManagerClass managerClass = new ManagerClass();
            CognitoCachingCredentialsProvider credentialsProvider =
                    managerClass.getCredentials(FoodTruckLocator.this); //Pass in the activity name
            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
            result = mapper.scan(FoodTruck.class, scanExpression);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            /* Remove all truck views */
            infoArray.clear();

            /* Iterate through all newly discovered trucks */
            for (FoodTruck truck: result) {
                if(truck.getIsTruck() == 0){ continue; } // Don't display to list view if account isnt a truck owner
                Log.wtf("See truck rating", "Rating: " + truck.getRating());
                Log.wtf("See truck rating", "Menu: " + truck.getMenu());

                //Test print the hash maps
                for(Map.Entry<String, Integer> entry : truck.getRating().entrySet()){
                    Log.wtf("Testing hashmap", entry.getKey() + " - " + entry.getValue());
                }

                LatLng latLng = new LatLng(
                        Double.parseDouble(truck.getLat()),
                        Double.parseDouble(truck.getLon()));
                /* Add truck markers to map */
                truckList.add(mMap.addMarker(
                        new MarkerOptions().position(latLng).title(
                                truck.getName())));

                //Scan each truck to list view adapter
                FoodTruck truckToAdd = new FoodTruck();
                truckToAdd.setName(truck.getName());
                truckToAdd.setLat(truck.getLat());
                truckToAdd.setLon(truck.getLon());
                infoArray.add(truckToAdd);
                populateListView();
                /* Create a truck view and add it to LinearLayout */
//                TextView truckView = new TextView(FoodTruckLocator.this);
//                truckView.setTextSize(30);
//                truckView.setId(id++);
                /* Create parameters for the new truck view */
//                RelativeLayout.LayoutParams params =
//                        new RelativeLayout.LayoutParams(
//                                RelativeLayout.LayoutParams.WRAP_CONTENT,
//                                RelativeLayout.LayoutParams.WRAP_CONTENT);

                /* If there was a prior truck view, this one goes below it */
//                if (previous != null) {
//                    params.addRule(RelativeLayout.BELOW, previous.getId());
//                }
//                truckView.setLayoutParams(params);
//                truckView.setText(truck.getName());
//                frameTruckList.addView(truckView);
//                previous = truckView;
            }
        }
    }

    private class TruckCustomAdapter extends ArrayAdapter<FoodTruck>{
        private int layout;
        public TruckCustomAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<FoodTruck> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder mainViewHolder;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.truckName = (TextView) convertView.findViewById(R.id.listName); //Attach names here
                viewHolder.truckName.setText(getItem(position).getName());
                viewHolder.truckInfoBtn = (Button) convertView.findViewById(R.id.listBtn);
                viewHolder.truckInfoBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        FoodTruck foodTruck = infoArray.get(position);
                        Intent truckProfileIntent = new Intent();
                        Bundle truckProfileBundle = new Bundle();
                        String key = getResources().getString(R.string.food_truck_object_key);
                        truckProfileBundle.putParcelable(key, foodTruck);
                        truckProfileIntent.putExtras(truckProfileBundle);
                        truckProfileIntent.setClass(FoodTruckLocator.this, FoodTruckProfile.class);
                        startActivity(truckProfileIntent);
                        //Toast.makeText(getContext(), "Button was clicked for list item " + position, Toast.LENGTH_SHORT).show();
                    }
                });
                convertView.setTag(viewHolder);
            }

            return convertView;
        }
    }
    public class ViewHolder {
        TextView truckName;
        Button truckInfoBtn;
    }

    private void populateListView(){

        ListView truckListView = (ListView) findViewById(R.id.listView); // Find list view id

        truckListView.setAdapter(new TruckCustomAdapter(this, R.layout.list_item, infoArray));

        truckListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the clicked on food truck and center the map on it
                FoodTruck foodTruck = infoArray.get(position);
                LatLng latLng = new LatLng(Float.parseFloat(foodTruck.getLat()),
                        Float.parseFloat(foodTruck.getLon()));
                CameraUpdate center = CameraUpdateFactory.newLatLngZoom(latLng, 10);
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(defaultZoom);
                mMap.moveCamera(center);
                mMap.animateCamera(zoom);
            }
        });

    }
}
