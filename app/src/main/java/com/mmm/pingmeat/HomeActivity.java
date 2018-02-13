package com.mmm.pingmeat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Camille on 13/02/2018.
 */

public class HomeActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void pingMeat(View view) {
        // Go to maps activity
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }


}
