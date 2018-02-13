package com.mmm.pingmeat.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Murakumo on 13/02/2018.
 */
@IgnoreExtraProperties
public class Foodtruck
{
    public Integer foodtruck_id;
    public String name;
    public Gerant gerant;
    public Float latitude;
    public Float longitude;
    public FoodType type;
    public String prix;
    public String logo;

    public Foodtruck()
    {

    }

    public Foodtruck(Integer foodtruck_id, String name, Gerant gerant, Float latitude, Float longitude, FoodType type, String prix, String logo) {
        this.foodtruck_id = foodtruck_id;
        this.name = name;
        this.gerant = gerant;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.prix = prix;
        this.logo = logo;
    }
}

