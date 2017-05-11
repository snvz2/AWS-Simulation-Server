package edu.sjsu.teamneon.hangrymobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditTruckProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_truck_profile);
        Button btnSubmit = (Button)findViewById(R.id.submit);
        final EditText descriptionVal = (EditText) findViewById(R.id.description);
        final EditText truckNameVal = (EditText) findViewById(R.id.truckName1);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String des = String.valueOf(descriptionVal.getText());
                String name = String.valueOf(truckNameVal.getText());
                //TODO: Add to Dynamo Description Field
                new DynamoCRUD.updateTruckDescription(EditTruckProfile.this).execute("1", des);
                new DynamoCRUD.updateTruckName(EditTruckProfile.this).execute("1", name);
            }
        });
        Button btnWebview = (Button)findViewById(R.id.webview);
        btnWebview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditTruckProfile.this, TruckUIWeb.class));

            }
        });

    }
}
