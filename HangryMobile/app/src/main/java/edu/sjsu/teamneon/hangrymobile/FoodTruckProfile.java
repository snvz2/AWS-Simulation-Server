package edu.sjsu.teamneon.hangrymobile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class FoodTruckProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_truck_profile);

        /* Get the food truck instance we passed as a bundle */
        FoodTruck foodTruck = null;
        Bundle foodTruckBundle = this.getIntent().getExtras();
        if (foodTruckBundle != null) {
            String key = getResources().getString(R.string.food_truck_object_key);
            foodTruck = foodTruckBundle.getParcelable(key);
        }
        /* If we don't have a food truck, something went very, very wrong */
        if (foodTruck == null) {
            return;
        }
        TextView foodTruckName = (TextView) findViewById(R.id.truckName);
        foodTruckName.setText(foodTruck.getName());
    }
}
