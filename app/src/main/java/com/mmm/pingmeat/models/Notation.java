package com.mmm.pingmeat.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Murakumo on 13/02/2018.
 */
@IgnoreExtraProperties
public class Notation
{
    public Client client;
    public Foodtruck foodtruck;
    public String note;
    public String comment;

    public Notation() {
    }

    public Notation(Client client, Foodtruck foodtruck, String note, String comment) {
        this.client = client;
        this.foodtruck = foodtruck;
        this.note = note;
        this.comment = comment;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("client",client);
        result.put("foodtruck",foodtruck);
        result.put("note",note);
        result.put("comment",comment);
        return result;
    }
}
