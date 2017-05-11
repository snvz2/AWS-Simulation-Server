package edu.sjsu.teamneon.hangrymobile;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RatingBar;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import java.util.Map;

import static edu.sjsu.teamneon.hangrymobile.R.id.ratingBar;


public class FoodTruckProfile extends AppCompatActivity {
    FoodTruck foodTruck;
    TextView txtRatingValue;
    String userRating;
    RatingBar ratingBar;

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

        TextView foodTruckDescription = (TextView) findViewById(R.id.description);
        foodTruckDescription.setText(foodTruck.getDescription());

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        txtRatingValue = (TextView) findViewById(R.id.averageRating);
        txtRatingValue.setText(String.valueOf(foodTruck.getAvgRating()));
        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                txtRatingValue.setText(String.valueOf(foodTruck.getAvgRating()));
                userRating = String.valueOf(Math.round(rating));
                Log.wtf("Testing",userRating);

                new updateTruck().execute();


            }
        });
    }

    private class updateTruck extends AsyncTask<Void, Integer, String> {
        @Override
        protected String doInBackground(Void... params){

            //Instantiate manager class (Currently only has Dynamo) and get credentials for mapper
            ManagerClass managerClass = new ManagerClass();
            CognitoCachingCredentialsProvider credentialsProvider =
                    managerClass.getCredentials(FoodTruckProfile.this); //Pass in the activity name
            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

            //Selects a truck based on primary key (id) to update
            FoodTruck truckToUpdate = mapper.load(FoodTruck.class, foodTruck.getID());

            //Increment Hashmap here
            truckToUpdate.getDbRating().put(userRating, truckToUpdate.getDbRating().get(userRating) + 1);

            mapper.save(truckToUpdate);

            return truckToUpdate.getAvgRating();
        }

        protected void onPostExecute(String avgRating) {
            txtRatingValue.setText(avgRating);
            ratingBar.setIsIndicator(true);
        }

    }

}

