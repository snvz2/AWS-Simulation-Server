package edu.sjsu.teamneon.hangrymobile;

import android.content.Context;
import android.os.AsyncTask;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

/**
 * Created by danieltam on 5/10/17.
 */

public class DynamoCRUD {

    //Updating truck name
    public static class updateTruckName extends AsyncTask<String, Integer, String>{

        private Context context;

        public updateTruckName(Context context) {
            this.context = context;
        }
        @Override
        protected String doInBackground(String... params){

            //Instantiate manager class (Currently only has Dynamo) and get credentials for mapper
            ManagerClass managerClass = new ManagerClass();
            CognitoCachingCredentialsProvider credentialsProvider =
                    managerClass.getCredentials(context); //Pass in the activity name
            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

            //Selects a truck based on primary key (id) to update
            FoodTruck truckToUpdate = mapper.load(FoodTruck.class, params[0]);//Params[0] holds the truck id
            truckToUpdate.setName(params[1]);

            mapper.save(truckToUpdate);

            return null;
        }
    }

    //Update truck latitude
    public static class updateTruckLat extends AsyncTask<String, Integer, String>{

        private Context context;

        public updateTruckLat(Context context) {
            this.context = context;
        }
        @Override
        protected String doInBackground(String... params){

            //Instantiate manager class (Currently only has Dynamo) and get credentials for mapper
            ManagerClass managerClass = new ManagerClass();
            CognitoCachingCredentialsProvider credentialsProvider =
                    managerClass.getCredentials(context); //Pass in the activity name
            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

            //Selects a truck based on primary key (id) to update
            FoodTruck truckToUpdate = mapper.load(FoodTruck.class, params[0]);//Params[0] holds the truck id
            truckToUpdate.setLat(params[1]);

            mapper.save(truckToUpdate);

            return null;
        }
    }

    //Update truck longitude
    public static class updateTruckLon extends AsyncTask<String, Integer, String>{

        private Context context;

        public updateTruckLon(Context context) {
            this.context = context;
        }
        @Override
        protected String doInBackground(String... params){

            //Instantiate manager class (Currently only has Dynamo) and get credentials for mapper
            ManagerClass managerClass = new ManagerClass();
            CognitoCachingCredentialsProvider credentialsProvider =
                    managerClass.getCredentials(context); //Pass in the activity name
            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

            //Selects a truck based on primary key (id) to update
            FoodTruck truckToUpdate = mapper.load(FoodTruck.class, params[0]);//Params[0] holds the truck id
            truckToUpdate.setLon(params[1]);

            mapper.save(truckToUpdate);

            return null;
        }
    }

    //Update truck description
    public static class updateTruckDescription extends AsyncTask<String, Integer, String>{

        private Context context;

        public updateTruckDescription(Context context) {
            this.context = context;
        }
        @Override
        protected String doInBackground(String... params){

            //Instantiate manager class (Currently only has Dynamo) and get credentials for mapper
            ManagerClass managerClass = new ManagerClass();
            CognitoCachingCredentialsProvider credentialsProvider =
                    managerClass.getCredentials(context); //Pass in the activity name
            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

            //Selects a truck based on primary key (id) to update
            FoodTruck truckToUpdate = mapper.load(FoodTruck.class, params[0]);//Params[0] holds the truck id
            truckToUpdate.setDescription(params[1]);

            mapper.save(truckToUpdate);

            return null;
        }
    }
    //Delete truck
    public static class deleteTruck extends AsyncTask<String, Integer, String> {

        private Context context;

        public deleteTruck(Context context) {
            this.context = context;
        }
        @Override
        protected String doInBackground(String... params){

            //Instantiate manager class (Currently only has Dynamo) and get credentials for mapper
            ManagerClass managerClass = new ManagerClass();
            CognitoCachingCredentialsProvider credentialsProvider =
                    managerClass.getCredentials(context); //Pass in the activity name
            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

            //Selects a truck based on primary key (id) to delete
            FoodTruck truckToDelete = mapper.load(FoodTruck.class, params[0]);

            mapper.delete(truckToDelete);

            return null;
        }
    }

    public static class storeTruck extends AsyncTask<String, Integer, Integer>{
        private String id;
        private String truckName;
        private String truckLon;
        private String truckLat;
        private Context context;

        public storeTruck(Context context) {
            this.context = context;
        }
        public storeTruck(String id,String name, String lon, String lat, Context context)
        {
            this.id = id;
            this.truckName = name;
            this.truckLon = lon;
            this.truckLat = lat;
            this.context = context;
        }

        @Override
        protected Integer doInBackground(String... params){

            //Instantiate manager class (Currently only has Dynamo) and get credentials for mapper
            ManagerClass managerClass = new ManagerClass();
            CognitoCachingCredentialsProvider credentialsProvider =
                    managerClass.getCredentials(context); //Pass in the activity name
            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

            //Takes in in order: id, name, long, lat
            FoodTruck newTruck = new FoodTruck();
            newTruck.setID(params[0]);
            newTruck.setName(params[1]);
            newTruck.setLon(params[2]);
            newTruck.setLat(params[3]);
            mapper.save(newTruck);

            return null;
        }
    }

    //Grab truck by id
    public static class retreiveTruck extends AsyncTask<String, Integer, FoodTruck>{

        private Context context;

        public retreiveTruck(Context context) {
            this.context = context;
        }
        @Override
        protected FoodTruck doInBackground(String... params){

            //Instantiate manager class (Currently only has Dynamo) and get credentials for mapper
            ManagerClass managerClass = new ManagerClass();
            CognitoCachingCredentialsProvider credentialsProvider =
                    managerClass.getCredentials(context); //Pass in the activity name
            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);

            //Selects a truck based on primary key (id) to update
            FoodTruck truckToUpdate = mapper.load(FoodTruck.class, params[0]);//Params[0] holds the truck id

            return truckToUpdate;
        }
    }
}
