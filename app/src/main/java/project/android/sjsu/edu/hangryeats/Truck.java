package project.android.sjsu.edu.hangryeats;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;


/**
 * Created by danieltam on 3/11/17.
 * Truck mapper class
 */

@DynamoDBTable(tableName = "Trucks")
public class Truck {
    private double id;
    private String name;
    private double lon;
    private double lat;

    @DynamoDBHashKey(attributeName = "id")
    public double getID() {
        return id;
    }

    public void setID(double id) {
        this.id = id;
    }

    @DynamoDBIndexRangeKey(attributeName = "name")
    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    @DynamoDBIndexHashKey(attributeName = "lon")
    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @DynamoDBAttribute(attributeName = "lat")
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
