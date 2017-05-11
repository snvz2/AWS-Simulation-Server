package edu.sjsu.teamneon.hangrymobile;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class FoodTruckDynamoTest {

    private static final String id = "_testTruck123";
    private static final String description = "Freshly fried iguana on a stick!";
    private static final int isTruck = 255;
    private static final String lat = "56.0625";
    private static final String lon = "-128.3265";
    private static final String name = "Iguana Bob's Iguana Sticks";
    FoodTruck foodTruck;
    FoodTruck testFoodTruck;

    public class CurrentThreadExecutor implements Executor {
        public void execute(Runnable r) {
            r.run();
        }
    }

    @Test
    public void create_testFoodTruck() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        // Test constructor, getters, and setters
        foodTruck = new FoodTruck();
        assertNotNull(foodTruck);

        foodTruck.setID(id);
        assertNotNull(foodTruck.getID());
        assertTrue(id.equals(foodTruck.getID()));

        foodTruck.setDescription(description);
        assertNotNull(foodTruck.getDescription());
        assertTrue(description.equals(foodTruck.getDescription()));

        foodTruck.setIsTruck(isTruck);
        assertNotNull(foodTruck.getIsTruck());
        assertTrue(isTruck == foodTruck.getIsTruck());

        foodTruck.setLat(lat);
        assertNotNull(foodTruck.getLat());
        assertTrue(lat.equals(foodTruck.getLat()));

        foodTruck.setLon(lon);
        assertNotNull(foodTruck.getLon());
        assertTrue(lon.equals(foodTruck.getLon()));

        foodTruck.setName(name);
        assertNotNull(foodTruck.getName());
        assertTrue(name.equals(foodTruck.getName()));

        new DynamoCRUD.storeTruck(foodTruck.getID(),foodTruck.getName(), foodTruck.getLat(), foodTruck.getLon(), appContext).executeOnExecutor(new CurrentThreadExecutor());
        // We don't know how to wait for on this
        Thread.sleep(5000);
    }

    @Test
    public void get_testFoodTruck() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        // Get the truck back that we created
        new DynamoCRUD.retrieveTruck(appContext).executeOnExecutor(new CurrentThreadExecutor(), foodTruck.getID());
        Thread.sleep(5000);

        // compare the fields
        assertNotNull(testFoodTruck.getID());
        assertTrue(id.equals(testFoodTruck.getID()));

        assertNotNull(testFoodTruck.getDescription());
        assertTrue(description.equals(testFoodTruck.getDescription()));

        assertNotNull(testFoodTruck.getIsTruck());
        assertTrue(isTruck == testFoodTruck.getIsTruck());

        assertNotNull(testFoodTruck.getLat());
        assertTrue(lat.equals(testFoodTruck.getLat()));

        foodTruck.setLon(lon);
        assertNotNull(testFoodTruck.getLon());
        assertTrue(lon.equals(testFoodTruck.getLon()));

        assertNotNull(testFoodTruck.getName());
        assertTrue(name.equals(testFoodTruck.getName()));

    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("edu.sjsu.teamneon.hangrymobile", appContext.getPackageName());
    }
}
