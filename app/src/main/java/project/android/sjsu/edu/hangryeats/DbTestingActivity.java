package project.android.sjsu.edu.hangryeats;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ScrollingTabContainerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;


import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ConditionalOperator;

import java.lang.*;
import java.util.Timer;
import java.util.TimerTask;

public class DbTestingActivity extends AppCompatActivity {


    Button addBtn;
    TextView displayResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBtn = (Button) findViewById(R.id.button);
        displayResult = (TextView) findViewById(R.id.textView2);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Takes in (TimerTask, first run, interval runs)
                timer.schedule(updateTruckCoords, 0, 10000); //Run every 10 seconds

            }
        });
    }

    final Handler handler = new android.os.Handler();
    Timer timer = new Timer();
    TimerTask updateTruckCoords = new TimerTask() {
        @Override
        public void run() {
            handler.post(new Runnable() {
                public void run() {
                    try {
                        new updateTruck().execute();
                    } catch (Exception e) {
                    }
                }
            });
        }
    };

    private class storeTruck extends AsyncTask<String, Integer, Integer> {
        private String truckName;
        String truckLon;
        String truckLat;

        public storeTruck(String name, String lon, String lat) {
            truckName = name;
            truckLon = lon;
            truckLat = lat;
        }

        @Override
        protected Integer doInBackground(String... params) {

            //Instantiate manager class (Currently only has Dynamo) and get credentials for mapper
            ManagerClass managerClass = new ManagerClass();
            CognitoCachingCredentialsProvider credentialsProvider = managerClass.getCredentials(DbTestingActivity.this); //Pass in the activity name
            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

            //Get values through ID of field or Geo code
            Truck newTruck = new Truck();
            newTruck.setLat(truckLat);
            newTruck.setLon(truckLon);
            newTruck.setName(truckName);
            mapper.save(newTruck);

            return null;
        }
    }

    private class updateTruck extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {

            //Instantiate manager class (Currently only has Dynamo) and get credentials for mapper
            ManagerClass managerClass = new ManagerClass();
            CognitoCachingCredentialsProvider credentialsProvider = managerClass.getCredentials(DbTestingActivity.this); //Pass in the activity name
            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

            //Selects a truck based on primary key (id) to update
            Truck truckToUpdate = mapper.load(Truck.class, "3");
            truckToUpdate.setName("New truck name");

            mapper.save(truckToUpdate);

            return null;
        }
    }

    private class deleteTruck extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {

            //Instantiate manager class (Currently only has Dynamo) and get credentials for mapper
            ManagerClass managerClass = new ManagerClass();
            CognitoCachingCredentialsProvider credentialsProvider = managerClass.getCredentials(DbTestingActivity.this); //Pass in the activity name
            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

            //Selects a truck based on primary key (id) to delete
            Truck truckToDelete = mapper.load(Truck.class, 6);

            mapper.delete(truckToDelete);

            return null;
        }
    }

    //Returns all the entry in the database
    private class scanAllTrucks extends AsyncTask<String, Integer, Integer> {
        @Override
        protected Integer doInBackground(String... params) {

            //Instantiate manager class (Currently only has Dynamo) and get credentials for mapper
            ManagerClass managerClass = new ManagerClass();
            CognitoCachingCredentialsProvider credentialsProvider = managerClass.getCredentials(DbTestingActivity.this); //Pass in the activity name
            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
            PaginatedScanList<Truck> result = mapper.scan(Truck.class, scanExpression);
            for (int x = 0; x < result.size(); x++) {
                //Log.d("CLASS", "Truck Name: " + result.get(x).getName() + ", Lat: " + result.get(x).getLat() + ", Lon: " + result.get(x).getLon());

            }
            return null;
        }
    }
    //WORK IN PROGRESS
//    private class queryTrucks extends AsyncTask<String, Void, Void>{
//          private Truck truckToFind = new Truck();
//          private PaginatedQueryList<Truck> result;
//        @Override
//        protected Void doInBackground(String... name){
//
//            //Instantiate manager class (Currently only has Dynamo) and get credentials for mapper
//            ManagerClass managerClass = new ManagerClass();
//            CognitoCachingCredentialsProvider credentialsProvider = managerClass.getCredentials(DbTestingActivity.this); //Pass in the activity name
//            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
//            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);
//
//            Condition condition = new Condition()
//                    .withComparisonOperator(ComparisonOperator.CONTAINS.toString())
//                    .withAttributeValueList(new AttributeValue().withS("Garage"));
//
////            DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
////                    .withAT
//
//            DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
//                    .withRangeKeyCondition()
//                    .withQueryFilterEntry("name", condition);
//
//            PaginatedQueryList<Truck> result = mapper.query(Truck.class, queryExpression);
//
//            return null;
//        }
//
//
//        @Override
//        protected void onPostExecute(Void v) {
//            displayResult.setText(result.toString());
//        }
//    }

    private class queryTrucks extends AsyncTask<String, Void, Void> {
        private Truck truckToFind = new Truck();
        private PaginatedQueryList<Truck> result;

        @Override
        protected Void doInBackground(String... name) {

            //Instantiate manager class (Currently only has Dynamo) and get credentials for mapper
            ManagerClass managerClass = new ManagerClass();
            CognitoCachingCredentialsProvider credentialsProvider = managerClass.getCredentials(DbTestingActivity.this); //Pass in the activity name
            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

            Condition condition = new Condition()
                    .withComparisonOperator(ComparisonOperator.CONTAINS.toString())
                    .withAttributeValueList(new AttributeValue().withS("Garage"));

//            DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
//                    .withAT

//            DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
//                    .withRangeKeyCondition()
//                    .withQueryFilterEntry("name", condition);

//            PaginatedQueryList<Truck> result = mapper.query(Truck.class, queryExpression);

            return null;
        }


        @Override
        protected void onPostExecute(Void v) {
           // displayResult.setText(result.toString());
        }
    }


    private class updateCoord extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {

//            //Instantiate manager class (Currently only has Dynamo) and get credentials for mapper
//            ManagerClass managerClass = new ManagerClass();
//            CognitoCachingCredentialsProvider credentialsProvider = managerClass.getCredentials(DbTestingActivity.this); //Pass in the activity name
//            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
//            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);
//
//            //Selects a truck based on primary key (id) to update
//            Truck truckToUpdate = mapper.load(Truck.class, 3);
//            truckToUpdate.setName("New truck name");

            //mapper.save(truckToUpdate);
            //timer.schedule(doAsynchronousTask, 0, 10000); //execute in every 10 ms


            return null;
        }

    }
}
