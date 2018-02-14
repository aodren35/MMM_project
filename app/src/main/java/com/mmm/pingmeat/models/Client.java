package com.mmm.pingmeat.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Murakumo on 13/02/2018.
 */

@IgnoreExtraProperties
public class Client
{

    public String username;
    public String email;
    //public String password;
    public String avatar;

    public Client() {
    }

    public Client(String username, String email, String avatar) {
        this.username = username;
        this.email = email;
        this.avatar = avatar;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username",username);
        result.put("email",email);
        result.put("avatar",avatar);
        return result;
    }

}
