package com.mmm.pingmeat.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name",name);
        return result;
    }

}