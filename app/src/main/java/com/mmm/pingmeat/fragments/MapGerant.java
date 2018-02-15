package com.mmm.pingmeat.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mmm.pingmeat.R;
import com.mmm.pingmeat.models.Foodtruck;
import com.mmm.pingmeat.models.Gerant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aodre on 14/02/2018.
 */

public class MapGerant extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationListener {


    private GoogleMap mMap;
    MapView mapView;
    private static final int LOCATION_REQUEST = 500;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mCurrentLocation;
    private LocationManager locationManager;
    private List<Foodtruck> listFoodTrunks;

    private TextView name;
    private TextView owner;
    private TextView price;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map_gerant, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mapView = (MapView) view.findViewById(R.id.map_gerant);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Map gérant");
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

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        getUserLocation("MOI");

        createListFoodTrunks();

        for (Foodtruck foodTrunk : this.listFoodTrunks) {
            getLocationFoodTrunk(foodTrunk.latitude + i, foodTrunk.longitude + i, foodTrunk.name);
            i = i + 0.005;
        }

        // ajout du bouton localisation de l'appareil
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);
    }

    private void getUserLocation(final String str) {

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // autorise les différents type localisation de l'appareil
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1000, 0, this);

        // obtenir la dernière localisation de l'appareil
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            onLocationChanged(location);
                            // centre la caméra sur la position de l'appareil
                            LatLng userLocation = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(userLocation, 15);
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
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

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

        name = getActivity().findViewById(R.id.name_food_truck);
        owner = getActivity().findViewById(R.id.owner_food_truck);
        price = getActivity().findViewById(R.id.price_food_truck);

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
