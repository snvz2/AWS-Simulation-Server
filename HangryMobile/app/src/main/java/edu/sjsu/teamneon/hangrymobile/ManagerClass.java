package edu.sjsu.teamneon.hangrymobile;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;

/**
 * Created by danieltam on 3/11/17.
 */

public class ManagerClass {

    //Amazon Cognito credentials provider
    public CognitoCachingCredentialsProvider getCredentials(Context context) {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                "us-east-1:a3817da0-9c99-4fc7-a61a-cc509fb5b8a0", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        return credentialsProvider;
    }
}