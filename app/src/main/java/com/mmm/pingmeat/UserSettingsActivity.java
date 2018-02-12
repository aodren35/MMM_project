package com.mmm.pingmeat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class UserSettingsActivity extends AppCompatActivity {

    public static final int REQUEST_PROFILE = 22;
    public static final String USER_KEY = "ProfileActivity.USER_ID_EXTRA_KEY";

    // User info

    // Foodtruck pref



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
    }
}
