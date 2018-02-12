package com.mmm.pingmeat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void displayMap(View view) {
        // Go to Google Map activity
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void goToUserSettings(View view) {
        // Go to user settings activity
        Intent intent = new Intent(this, UserSettingsActivity.class);
        startActivity(intent);
    }
}
