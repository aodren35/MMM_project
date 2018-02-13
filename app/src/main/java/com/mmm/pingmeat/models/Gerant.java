package com.mmm.pingmeat.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Murakumo on 13/02/2018.
 */

// [START blog_user_class]
@IgnoreExtraProperties
public class Gerant
{

    public Integer user_id;
    public String username;
    public String logo;

    public Gerant() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Gerant(Integer user_id,String username, String logo) {
        this.user_id = user_id;
        this.username = username;
        this.logo = logo;
    }

}
// [END blog_user_class]
