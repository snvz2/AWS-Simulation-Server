package edu.sjsu.teamneon.hangrymobile;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class FoodTruckDynamoUnitTest {
    FoodTruck foodTruck;
    @Test
    public void create_testFoodTruck() throws Exception {
        foodTruck = new FoodTruck();
        foodTruck.setID("_testTruck123");

    }
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
}