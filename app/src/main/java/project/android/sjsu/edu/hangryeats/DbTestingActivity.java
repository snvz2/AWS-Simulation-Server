package project.android.sjsu.edu.hangryeats;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

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

                //Run CRUD methods
                new storeTruck("Tacos 2", "3.13", "62.3").execute();

            }
        });
    }

    private class storeTruck extends AsyncTask<String, Integer, Integer>{
        private String truckName;
        String truckLon;
        String truckLat;
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

    private class updateTruck extends AsyncTask<Void, Integer, Integer>{
        @Override
        protected Integer doInBackground(Void... params){

            //Instantiate manager class (Currently only has Dynamo) and get credentials for mapper
            ManagerClass managerClass = new ManagerClass();
            CognitoCachingCredentialsProvider credentialsProvider = managerClass.getCredentials(DbTestingActivity.this); //Pass in the activity name
            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

            //Selects a truck based on primary key (id) to update
            Truck truckToUpdate = mapper.load(Truck.class, 3);
            truckToUpdate.setName("New truck name");

            mapper.save(truckToUpdate);

            return null;
        }
    }

    private class deleteTruck extends AsyncTask<Void, Integer, Integer>{
        @Override
        protected Integer doInBackground(Void... params){

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
    private class scanAllTrucks extends AsyncTask<Void, Integer, Integer>{
        @Override
        protected Integer doInBackground(Void... params){

            //Instantiate manager class (Currently only has Dynamo) and get credentials for mapper
            ManagerClass managerClass = new ManagerClass();
            CognitoCachingCredentialsProvider credentialsProvider = managerClass.getCredentials(DbTestingActivity.this); //Pass in the activity name
            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
            PaginatedScanList<Truck> result = mapper.scan(Truck.class, scanExpression);
            for(int x = 0; x < result.size(); x++){
                Log.d("CLASS", "Truck Name: " + result.get(x).getName() + ", Lat: " + result.get(x).getLat() + ", Lon: " + result.get(x).getLon());

            }
            return null;
        }
    }
      //WORK IN PROGRESS
//    private class queryTrucks extends AsyncTask<Void, Void, Double>{
//        @Override
//        protected Double doInBackground(Void... name){
//
//            //Instantiate manager class (Currently only has Dynamo) and get credentials for mapper
//            ManagerClass managerClass = new ManagerClass();
//            CognitoCachingCredentialsProvider credentialsProvider = managerClass.getCredentials(DbTestingActivity.this); //Pass in the activity name
//            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
//            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);
//
//            Truck truckToFind = new Truck();
//            truckToFind.setID("");
//
//            String queryString = "Best";
//
//            Condition rangeKeyCondition = new Condition()
//                    .withComparisonOperator(ComparisonOperator.BEGINS_WITH.toString())
//                    .withAttributeValueList(new AttributeValue().withS(queryString.toString()));
//
//            DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
//                    .withHashKeyValues(truckToFind)
//                    .withRangeKeyCondition("name", rangeKeyCondition)
//                    .withConsistentRead(false);
//
//            PaginatedQueryList<Truck> result = mapper.query(Truck.class, queryExpression);
//// Do something with result.
////            Log.d("query",Double.toString(result.get(0).getID()));
//
//            return null;
//        }
//
//
//        @Override
//        protected void onPostExecute(Double result) {
//            displayResult.setText(result.toString());
//        }
//    }


}
