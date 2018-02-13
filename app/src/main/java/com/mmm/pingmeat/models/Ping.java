package com.mmm.pingmeat.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Murakumo on 13/02/2018.
 */
@IgnoreExtraProperties
public class Ping
{
    public Integer ping_id;
    public Client client;
    public Float longitude;
    public Float latitude;
    public FoodType[] types;

    public Ping() {
    }

    public Ping(Integer ping_id, Client client, Float longitude, Float latitude, FoodType[] types) {
        this.ping_id = ping_id;
        this.client = client;
        this.longitude = longitude;
        this.latitude = latitude;
        this.types = types;
    }
}