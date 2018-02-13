package com.mmm.pingmeat.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Murakumo on 13/02/2018.
 */

// [START blog_user_class]
@IgnoreExtraProperties
public class Client
{

    public Integer user_id;
    public String username;
    public String avatar;

    public Client() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Client(Integer user_id,String username, String avatar) {
        this.user_id = user_id;
        this.username = username;
        this.avatar = avatar;
    }

}
// [END blog_user_class]
