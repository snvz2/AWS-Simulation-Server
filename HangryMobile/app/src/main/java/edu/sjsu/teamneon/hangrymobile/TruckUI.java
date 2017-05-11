package edu.sjsu.teamneon.hangrymobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
public class TruckUI extends AppCompatActivity {
    String googleAcctNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_ui);
        Button btnEnable = (Button)findViewById(R.id.enableLoc);
        Button btnDisable = (Button)findViewById(R.id.disableLoc);

        Button btnProfile = (Button)findViewById(R.id.editTruckProfile);

        Bundle extras = this.getIntent().getExtras();
        googleAcctNum = extras.getString("food_truck_object_info_key");
        Log.wtf("this is google acct", " " + googleAcctNum);



        final Button indicator = (Button)findViewById(R.id.indicator);
        final TextView textIndicator = (TextView)findViewById(R.id.textIndicator);
        btnEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                indicator.setActivated(true);
                textIndicator.setVisibility(View.VISIBLE);

            }
        });
        btnDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                indicator.setActivated(false);
                textIndicator.setVisibility(View.INVISIBLE);

            }
        });


        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(getApplicationContext(), EditTruckProfile.class);
                intent.putExtra("food_truck_object_info_key", googleAcctNum);
                startActivity(intent);
                //startActivity(new Intent(TruckUI.this, EditTruckProfile.class));


            }
        });



    }

}
