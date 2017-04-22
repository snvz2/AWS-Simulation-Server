package edu.sjsu.teamneon.hangrymobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TruckUI extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_ui);
        Button btnEnable = (Button)findViewById(R.id.enableLoc);
        Button btnDisable = (Button)findViewById(R.id.disableLoc);

        btnEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TruckUI.this, "Broadacasting Location!", Toast.LENGTH_SHORT).show();

            }
        });
        btnDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TruckUI.this, "Broadacasting Disabled!", Toast.LENGTH_SHORT).show();
            }
        });


    }

}
