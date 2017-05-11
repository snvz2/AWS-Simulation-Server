package edu.sjsu.teamneon.hangrymobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.widget.Toast.makeText;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class EditTruckProfile extends AppCompatActivity {
    FoodTruck foodTruck;
    GoogleSignInAccount acct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_truck_profile);

        //Intent Bundle
        final Bundle isTruckBundle = this.getIntent().getExtras();
        if (isTruckBundle != null) {
            String key = getResources().getString(R.string.google_sign_in_account_object_key);
            acct = isTruckBundle.getParcelable(key);
        }


        Button btnSubmit = (Button)findViewById(R.id.submit);

        final EditText descriptionVal = (EditText) findViewById(R.id.description);
        final EditText truckNameVal = (EditText) findViewById(R.id.truckName1);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String des = String.valueOf(descriptionVal.getText());
                String name = String.valueOf(truckNameVal.getText());
                Toast.makeText(
                        getApplicationContext(),
                        "Success! Profile Updated", Toast.LENGTH_LONG).show();
                Log.wtf("SUCESS!! PROFILE UPDATED ", " ");
                new DynamoCRUD.updateTruckDescription(EditTruckProfile.this).execute(acct.getId(), des);
                new DynamoCRUD.updateTruckName(EditTruckProfile.this).execute(acct.getId(), name);



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
