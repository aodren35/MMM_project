package com.mmm.pingmeat.models;

import com.google.firebase.database.IgnoreExtraProperties;

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
}
