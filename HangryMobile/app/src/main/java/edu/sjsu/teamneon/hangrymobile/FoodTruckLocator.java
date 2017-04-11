package edu.sjsu.teamneon.hangrymobile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FoodTruckLocator extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    Button btnLoc;
    GPSTracker gps;
    private static final int PERMS_REQUEST_CODE = 123;
    private GoogleMap mMap;
    private FoodTruck testFoodTruck = new FoodTruck("Test truck", "Test Address");

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        mMap.animateCamera(cameraUpdate);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Fake Food Truck */
        testFoodTruck.setLatLng(new LatLng(37.336228,-121.881071));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_truck_locator);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.truckMap);
        mapFragment.getMapAsync(this);
        /* Populate the list with truck names */
        FrameLayout truckList = (FrameLayout) findViewById(R.id.truckList);
        TextView truckView = new TextView(this);
        truckView.setText(testFoodTruck.getName());
        truckList.addView(truckView);
        btnLoc = (Button) findViewById(R.id.gps);

        btnLoc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gps = new GPSTracker(FoodTruckLocator.this);

                requestPerms();
                if(gps.canGetLocation() && hasPermissions()) {
                    onLocationChanged(gps.getLocation());
                    gps.getLocation();
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    Toast.makeText(
                            getApplicationContext(),
                            "Your Location is -\nLat: " + latitude + "\nLong: "
                                    + longitude, Toast.LENGTH_LONG).show();

                } else {

                    gps.showSettingsAlert();
                    requestPerms();
                }
            }
        });
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
        mMap.addMarker(new MarkerOptions().position(testFoodTruck.getLatLng()).title(testFoodTruck.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(testFoodTruck.getLatLng()));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
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
}
