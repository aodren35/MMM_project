package com.mmm.pingmeat.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mmm.pingmeat.R;
import com.mmm.pingmeat.models.Foodtruck;

import java.util.ArrayList;
import java.util.List;


public class FoodTrucksFavoris extends Fragment {

    private ListView myFoodTruckFavoris;



    // Db
    private FirebaseDatabase fireDb;
    private DatabaseReference mDatabase;

    public List<String> listFoodTrucks;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // récupère données dans ma bdd
        fireDb = FirebaseDatabase.getInstance();
        mDatabase = fireDb.getInstance().getReference("foodtruck");
        listFoodTrucks = new ArrayList<>();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot foodtruckSnapshot : dataSnapshot.getChildren()) {
                    Foodtruck foodtruck = foodtruckSnapshot.getValue(Foodtruck.class);
                    listFoodTrucks.add(foodtruck.name);
                    Log.i("TII", foodtruck.name);
                    String[] listFoddTruckArray = new String[listFoodTrucks.size()];
                    listFoddTruckArray = listFoodTrucks.toArray(listFoddTruckArray);
                    //Création d'un SimpleAdapter qui se chargera de mettre les items présent dans notre listIem
                    ArrayAdapter<String> mSchedule = new ArrayAdapter<String> (getActivity(),  android.R.layout.simple_list_item_1,
                            (listFoddTruckArray));
                    myFoodTruckFavoris.setAdapter(mSchedule);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        View view = inflater.inflate(R.layout.fragment_food_trucks_favoris, container, false);

        //Récupération de la listview créée dans le fichier xml
        myFoodTruckFavoris = (ListView) view.findViewById(R.id.foodtrucks_favoris);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Mes food trucks favoris");
    }

}
