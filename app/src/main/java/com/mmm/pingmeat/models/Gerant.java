package com.mmm.pingmeat.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Murakumo on 13/02/2018.
 */
@IgnoreExtraProperties
public class Gerant
{

    public String username;
    public String email;
    //public String password;
    public String logo;

    public Gerant() {
    }

    public Gerant(String username, String email, String logo) {
        this.username = username;
        this.email = email;
        this.logo = logo;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username",username);
        result.put("email",email);
        result.put("avatar",logo);
        return result;
    }
}
