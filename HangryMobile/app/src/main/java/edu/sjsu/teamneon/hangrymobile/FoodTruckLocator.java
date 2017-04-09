package edu.sjsu.teamneon.hangrymobile;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FoodTruckLocator extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FoodTruck testFoodTruck = new FoodTruck("Test truck", "Test Address");

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

        /* Add a marker for test food truck and move camera */
        mMap.addMarker(new MarkerOptions().position(testFoodTruck.getLatLng()).title(testFoodTruck.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(testFoodTruck.getLatLng()));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
    }
}
