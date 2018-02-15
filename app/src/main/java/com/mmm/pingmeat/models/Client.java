package com.mmm.pingmeat.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Murakumo on 13/02/2018.
 */

@IgnoreExtraProperties
public class Client
{

    private String id;
    private String username;
    private String email;
    private String avatarUrl;
    private List<Foodtruck> favorites;

    public Client()
    {

    }

    public Client(String username, String email, String avatar, List<Foodtruck> favorites) {
        this.username = username;
        this.email = email;
        this.avatarUrl = avatar;
        this.favorites = favorites;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public List<Foodtruck> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Foodtruck> favorites) {
        this.favorites = favorites;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username",username);
        result.put("email",email);
        result.put("avatar",avatarUrl);
        result.put("favorites",favorites);
        return result;
    }

}
