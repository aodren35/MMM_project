package com.mmm.pingmeat;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mmm.pingmeat.models.Foodtruck;
import com.mmm.pingmeat.models.Gerant;

import java.util.HashMap;
import java.util.Map;

public class NewFoodTruckActivity extends AppCompatActivity {


    private static final String TAG = "NewFoodTruckActivity";
    private static final String REQUIRED = "Required";

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private EditText mNameField;
    private EditText mFoodTypeField;
    private EditText mPriceField;
    private Double longitude;
    private Double latitude;
    private FloatingActionButton mSubmitButton;
    private GoogleMap googleMap;

    private void submitPost()
    {
        final String name = mNameField.getText().toString();
        final String type = mFoodTypeField.getText().toString();
        final String price = mPriceField.getText().toString();

        // name field is required
        if (TextUtils.isEmpty(name)) { mNameField.setError(REQUIRED); return; }

        // food type is required
        if (TextUtils.isEmpty(type)) { mFoodTypeField.setError(REQUIRED); return; }

        // price is required
        if (TextUtils.isEmpty(price)) { mPriceField.setError(REQUIRED); return; }

        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child("Gerant").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        Gerant ger = dataSnapshot.getValue(Gerant.class);

                        // [START_EXCLUDE]
                        if (ger == null) {
                            // User is null, error out
                            Log.e(TAG, "Gerent " + userId + " is unexpectedly null");
                            Toast.makeText(NewFoodTruckActivity.this,"Error: could not fetch user.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            writeNewFoodtruck(ger,name,type,price,longitude.floatValue(),latitude.floatValue());
                        }

                        // Finish this Activity, back to the stream
                        setEditingEnabled(true);
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
        // [END single_value_read]
    }

    private void setEditingEnabled(boolean enabled) {
        mNameField.setEnabled(enabled);
        mFoodTypeField.setEnabled(enabled);
        mPriceField.setEnabled(enabled);
        if (enabled) {
            mSubmitButton.setVisibility(View.VISIBLE);
        } else {
            mSubmitButton.setVisibility(View.GONE);
        }
    }

    // [START write_fan_out]
    private void writeNewFoodtruck(Gerant gerent, String name, String foodtype, String price, Float longitude, Float latitude) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("foodtruck").push().getKey();
        Foodtruck truck = new Foodtruck(name, gerent, latitude, longitude, foodtype, price, "");
        Map<String, Object> postValues = truck.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/foodtruck/" + key, postValues);
        mDatabase.updateChildren(childUpdates);
    }
    // [END write_fan_out]


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_food_truck);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        mNameField = findViewById(R.id.field_name);
        mFoodTypeField = findViewById(R.id.field_foodtype);
        mPriceField = findViewById(R.id.field_price);
        mSubmitButton = findViewById(R.id.fab_submit_post);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.field_mapview);

        // Getting a reference to the map
        supportMapFragment.getMapAsync(new OnMapReadyCallback()
        {
            @Override
            public void onMapReady(GoogleMap googleMap)
            {
                OnGMapReady(googleMap);
            }
        });

        // Setting a click event handler for the map
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                // Clears the previously touched position
                googleMap.clear();

                // Animating to the touched position
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                googleMap.addMarker(markerOptions);
            }
        });
    }

    public void OnGMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_home_drawer, menu);
        return true;
    }

}
