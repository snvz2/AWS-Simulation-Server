package project.android.sjsu.edu.hangryeats;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;


/**
 * Created by danieltam on 3/11/17.
 * Truck mapper class
 */

@DynamoDBTable(tableName = "Trucks")
public class Truck {
    private String id;
    private String name;
    private String lon;
    private String lat;

    @DynamoDBHashKey(attributeName = "id")
    @DynamoDBAutoGeneratedKey
    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "name")
    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "lon")
    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    @DynamoDBAttribute(attributeName = "lat")
    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
