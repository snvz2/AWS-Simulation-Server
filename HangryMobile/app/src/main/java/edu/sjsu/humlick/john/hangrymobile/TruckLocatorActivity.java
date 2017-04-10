package edu.sjsu.humlick.john.hangrymobile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class TruckLocatorActivity extends FragmentActivity implements OnMapReadyCallback {
    Button btnLoc;
    GPSTracker gps;
    private static final int PERMS_REQUEST_CODE = 123;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_locator);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnLoc = (Button) findViewById(R.id.gps);

        btnLoc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gps = new GPSTracker(TruckLocatorActivity.this);

                requestPerms();
                if(gps.canGetLocation() && hasPermissions()) {
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
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

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

    private void requestPerms(){
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions,PERMS_REQUEST_CODE);}

    }

    //may need tehis method later
/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;

        switch(requestCode){
            case PERMS_REQUEST_CODE:

                for (int res :grantResults){
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }
                break;
            default:
                allowed = false;
                break;
        }
        if (allowed){

        }
    }*/
}
