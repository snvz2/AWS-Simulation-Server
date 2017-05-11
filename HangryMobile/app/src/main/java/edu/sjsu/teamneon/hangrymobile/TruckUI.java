package edu.sjsu.teamneon.hangrymobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.Timer;
import java.util.TimerTask;

public class TruckUI extends AppCompatActivity {

    GPSTracker gps;
    GoogleSignInAccount acct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_ui);

        final Bundle isTruckBundle = this.getIntent().getExtras();
        if (isTruckBundle != null) {
            String key = getResources().getString(R.string.google_sign_in_account_object_key);
            acct = isTruckBundle.getParcelable(key);
        }


        gps = new GPSTracker(TruckUI.this);
        Button btnEnable = (Button)findViewById(R.id.enableLoc);
        Button btnDisable = (Button)findViewById(R.id.disableLoc);

        Button btnProfile = (Button)findViewById(R.id.editTruckProfile);

        final Button indicator = (Button)findViewById(R.id.indicator);
        final TextView textIndicator = (TextView)findViewById(R.id.textIndicator);
        btnEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                indicator.setActivated(true);
                textIndicator.setVisibility(View.VISIBLE);

//                timer.cancel();
//                timer.purge();
//                updateTruckCoords.cancel();
//                timer.schedule(updateTruckCoords, 0, 1000); //Run every 10 seconds

                timer = new Timer();
                MyTask task=new MyTask();
                timer.schedule(task,0, 1000);

            }
        });
        btnDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timer.cancel();
                timer.purge();

                indicator.setActivated(false);
                textIndicator.setVisibility(View.INVISIBLE);

            }
        });


        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TruckUI.this, EditTruckProfile.class));

            }
        });

    }

    final Handler handler = new android.os.Handler();
    Timer timer = new Timer();
//    TimerTask updateTruckCoords = new TimerTask() {
//        @Override
//        public void run() {
//            handler.post(new Runnable() {
//                public void run() {
//                    try {
//                        gps.getLocation();
//                        new DynamoCRUD.updateTruckLatLon(TruckUI.this).execute(acct.getId(), Double.toString(gps.getLatitude()), Double.toString(gps.getLongitude()));
//                    } catch (Exception e) {
//                    }
//                }
//            });
//        }
//    };

    private class MyTask extends TimerTask {
        public void run() {
            try {
                gps.getLocation();
                new DynamoCRUD.updateTruckLatLon(TruckUI.this).execute(acct.getId(), Double.toString(gps.getLatitude()), Double.toString(gps.getLongitude()));
            } catch (Exception e) {
            }
        }
    }


}
