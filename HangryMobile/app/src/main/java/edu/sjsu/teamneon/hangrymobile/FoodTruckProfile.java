package edu.sjsu.teamneon.hangrymobile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RatingBar;
import android.widget.TextView;

import static edu.sjsu.teamneon.hangrymobile.R.id.ratingBar;

public class FoodTruckProfile extends AppCompatActivity {
    FoodTruck foodTruck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_truck_profile);
        //RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);


        /* Get the food truck instance we passed as a bundle */
        final Bundle foodTruckBundle = this.getIntent().getExtras();
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

        RatingBar ratingBar;
        TextView txtRatingValue;

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        txtRatingValue = (TextView) findViewById(R.id.averageRating);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                //txtRatingValue.setText(String.valueOf(rating));
                Log.wtf("Testing print", foodTruck.getID());
                Log.wtf("Testing print", foodTruck.getDescription());

            }
        });
    }



}
