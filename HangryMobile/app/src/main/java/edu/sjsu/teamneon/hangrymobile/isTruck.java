package edu.sjsu.teamneon.hangrymobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.Map;

public class isTruck extends AppCompatActivity {

    private GoogleSignInAccount acct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_truck);
        final Bundle isTruckBundle = this.getIntent().getExtras();
        if (isTruckBundle != null) {
            String key = getResources().getString(R.string.google_sign_in_account_object_key);
            acct = isTruckBundle.getParcelable(key);
        }
        /* If we don't have an account, something went very, very wrong */
        if (acct == null) {
            return;
        }

        Button btnIsTruck = (Button)findViewById(R.id.truck10);
        Button btnIsUser = (Button)findViewById(R.id.user10);
        btnIsTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String key = getResources().getString(R.string.google_sign_in_account_object_key);
                Intent isTruckIntent = new Intent();
                Bundle isTruckBundle = new Bundle();
                isTruckBundle.putParcelable(key, acct);
                isTruckIntent.putExtras(isTruckBundle);
                isTruckIntent.setClass(isTruck.this, TruckUI.class);
                startActivity(isTruckIntent);

                //Takes in in order: id, name, long, lat
                new DynamoCRUD.storeTruck(acct.getId(),acct.getDisplayName(), "0","0", isTruck.this).execute();
                //new DynamoCRUD.retrieveTruck(isTruck.this).execute(acct.getId());

            }
        });

        btnIsUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(isTruck.this, FoodTruckLocator.class));
            }
        });
    }

        //Add new truck based on what the user clicks.
//    private class addNewTruck extends AsyncTask<Void, Integer, Void> {
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            Log.wtf("Testing", "Added a new truck");
//            FoodTruck newTruck = new FoodTruck();
//            newTruck.setID(acct.getId());
//            newTruck.setIsTruck(false);//Depends on what the activity sends back
//
//            mapper.save(newTruck);
//
//            return null;
//        }
// }
}
