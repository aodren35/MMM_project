package com.mmm.pingmeat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void diaplayMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);

        /*Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        mapIntent.setPackage("com.google.android.apps.maps");
        mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mapIntent);*/
    }

    public void goToUserSettings(String userId, View view) {
        // Go to user settings activity
        Intent intent = new Intent(MainActivity.this, UserSettingsActivity.class);
        intent.putExtra(UserSettingsActivity.USER_KEY, userId);

        startActivityForResult(intent, UserSettingsActivity.REQUEST_PROFILE);

    }
}
