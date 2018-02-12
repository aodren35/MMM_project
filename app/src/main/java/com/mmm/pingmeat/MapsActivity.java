package com.mmm.pingmeat;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GoogleApiClient myGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double i = 0.005;

        getUserLocation(48.110081, -1.679274, "ME HERE");

        for (String foodTrunk : listFoodTrunks()) {
            getLocationFoodTrunk(48.110081 + i, -1.679274 + i, foodTrunk);
            i = i + 0.005;
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);
        /*myGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LOCATION_SERVICE.API)*/
    }

    private void getUserLocation(double lat, double lng, String markerName) {
        LatLng location = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 15);
        mMap.addMarker(new MarkerOptions().position(location).title(markerName));
        mMap.moveCamera(update);
    }

    private void getLocationFoodTrunk(double lat, double lng, String markerName) {
        LatLng location = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(location);
        mMap.addMarker(new MarkerOptions().position(location).title(markerName));
    }

    private List<String> listFoodTrunks() {
        List<String> listFoodTrunks = new ArrayList<>();
        listFoodTrunks.add("Toto");
        listFoodTrunks.add("Titi");
        listFoodTrunks.add("Tata");
        listFoodTrunks.add("Tuto");
        return listFoodTrunks;
    }
}
