package com.mmm.pingmeat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by aodre on 14/02/2018.
 */

public class BaseActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseStorage storage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        doAuthorization();
    }

    public void doAuthorization() {
        mUser = mAuth.getCurrentUser();
        if(mUser == null) { startLoginActivity(); }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public FirebaseUser getUser(){
        return mUser;
    }
    public FirebaseStorage getStorage() {
        return storage;
    }
}
