package com.mmm.pingmeat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mmm.pingmeat.models.Foodtruck;
import com.mmm.pingmeat.models.Gerant;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationListener {

    private GoogleMap mMap;
    private static final int LOCATION_REQUEST = 500;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mCurrentLocation;
    private LocationManager locationManager;
    private List<Foodtruck> listFoodTrunks;

    private TextView name;
    private TextView owner;
    private TextView price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("Debug", "Map onDestroy invoked");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("Debug", "Map OnPause invoked");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("Debug", "Map onResume invoked");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("Debug", "Map onStop invoked");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v("Debug", "Map onRestart invoked");
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        double i = 0.005;


        getUserLocation("MOI");
        createListFoodTrunks();

        for (Foodtruck foodTrunk : this.listFoodTrunks) {
            getLocationFoodTrunk(foodTrunk.latitude + i, foodTrunk.longitude + i, foodTrunk.name);
            i = i + 0.005;
        }

        // ajout du bouton localisation de l'appareil
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);
    }

    private void getUserLocation(final String str) {

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // autorise les différents type localisation de l'appareil
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1000, 0, this);

        // obtenir la dernière localisation de l'appareil
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            onLocationChanged(location);
                            // centre la caméra sur la position de l'appareil
                            LatLng userLocation = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(userLocation, 15);
                            mMap.addMarker(new MarkerOptions().position(userLocation)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                    .title(str));
                            mMap.moveCamera(update);
                        }
                    }
                });
    }


    public void onLocationChanged(Location location) {
        // GPS may be turned off
        if (location == null) {
            return;
        }
        // Report to the UI that the location was updated
        mCurrentLocation = location;
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        Foodtruck foodtruckSelected = null;
        if (marker.getTitle().equals("MOI")) {
            return false;
        } else {
            for (Foodtruck foodtruck : listFoodTrunks) {
                if (foodtruck.name.equals(marker.getTitle())) {
                    foodtruckSelected = foodtruck;
                    break;
                }
            }
            setInformationFoodTruck(foodtruckSelected);
        }
        return true;
    }

    private void setInformationFoodTruck(Foodtruck currentFoodTruck) {

        name = findViewById(R.id.name_food_truck);
        owner = findViewById(R.id.owner_food_truck);
        price = findViewById(R.id.price_food_truck);

        name.setText("Nom : " + currentFoodTruck.name);
        owner.setText("Propriétaire : " + currentFoodTruck.gerant.username);
        price.setText("Prix : " + currentFoodTruck.prix);

    }


    private void getLocationFoodTrunk(double lat, double lng, String markerName) {
        LatLng location = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(location).title(markerName));
    }

    private void createListFoodTrunks() {
        this.listFoodTrunks = new ArrayList<>();

        Foodtruck truck1 = new Foodtruck();
        Gerant gerant1 = new Gerant();
        gerant1.username = "Quang";
        truck1.latitude = (float) 48.110081;
        truck1.longitude = ((float) -1.679274);
        truck1.name = "Truck 1";
        truck1.prix = "Kebab 3€, Big Mac 3€";
        truck1.gerant = gerant1;
        listFoodTrunks.add(truck1);

        Foodtruck truck2 = new Foodtruck();
        Gerant gerant2 = new Gerant();
        gerant2.username = "One";
        truck2.latitude = (float) 48.110081;
        truck2.longitude = ((float) -1.679274);
        truck2.name = "Truck 2";
        truck2.prix = "Kebab 5€, Big Mac 5€";
        truck2.gerant = gerant2;
        listFoodTrunks.add(truck2);
    }
}
