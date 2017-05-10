package edu.sjsu.teamneon.hangrymobile;

import android.content.Intent;
import android.os.Bundle;
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
public class TruckUI extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_ui);
        Button btnEnable = (Button)findViewById(R.id.enableLoc);
        Button btnDisable = (Button)findViewById(R.id.disableLoc);
        Button btnWebview = (Button)findViewById(R.id.webview);

        Button btnProfile = (Button)findViewById(R.id.editTruckProfile);

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

        btnWebview.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
                startActivity(new Intent(TruckUI.this, TruckUIWeb.class));

            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TruckUI.this, EditTruckProfile.class));


            }
        });

    }

}
