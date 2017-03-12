package project.android.sjsu.edu.hangryeats;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;


/**
 * Created by danieltam on 3/11/17.
 */

@DynamoDBTable(tableName = "Trucks")
public class Truck {
    private Number id;
    private String name;
    private Number lon;
    private Number lat;

    @DynamoDBHashKey(attributeName = "id")
    public Number getID() {
        return id;
    }

    public void setID(Number id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Number getLon() {
        return lon;
    }

    public void setLon(Number lon) {
        this.lon = lon;
    }

    public Number getLat() {
        return lat;
    }

    public void setLat(Number lat) {
        this.lat = lat;
    }
}
