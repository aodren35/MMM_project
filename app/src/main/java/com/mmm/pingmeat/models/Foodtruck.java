package com.mmm.pingmeat.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Murakumo on 13/02/2018.
 */
@IgnoreExtraProperties
public class Foodtruck
{
    //public Integer foodtruck_id;
    public String name;
    public Gerant gerant;
    public Float latitude;
    public Float longitude;
    public String type;
    public String prix;
    public String logo;

    public Foodtruck()
    {

    }

    public Foodtruck(String name, Gerant gerant, Float latitude, Float longitude, String type, String prix, String logo) {
        this.name = name;
        this.gerant = gerant;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.prix = prix;
        this.logo = logo;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name",name);
        result.put("gerant",gerant);
        result.put("latitude",latitude);
        result.put("longitude",longitude);
        result.put("type",type);
        result.put("prix",prix);
        result.put("logo",logo);
        return result;
    }

}

