package com.mmm.pingmeat.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Murakumo on 13/02/2018.
 */
@IgnoreExtraProperties
public class FoodType
{
    public Integer foodtype_id;
    public String name;

    public FoodType() {

    }

    public FoodType(Integer foodtype_id, String name) {
        this.foodtype_id = foodtype_id;
        this.name = name;
    }
}