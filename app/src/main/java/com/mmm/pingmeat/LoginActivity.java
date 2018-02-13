package com.mmm.pingmeat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    EditText userText;
    EditText passwordText;
    RelativeLayout btnSignIn;
    TextView textNoAccount;

    View.OnClickListener signInListner = new View.OnClickListener() {
        public void onClick(View v) { signIn(); }
    };

    public void signIn()
    {
        String email = userText.getText().toString();
        String pass = passwordText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("debug", "signInWithEmail:success");
                            mUser = mAuth.getCurrentUser();
                            redirect();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("debug", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    View.OnClickListener createAccountListner = new View.OnClickListener() {
        public void onClick(View v) { createAccount(); }
    };

    public void createAccount()
    {
        Intent createAccount = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(createAccount);
    }

    private void redirect()
    {
        boolean v = true;
        if(v)
        {
            Intent i = new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(i);
        }
        else
        {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        userText = findViewById(R.id.editUser);
        passwordText = findViewById(R.id.editPass);
        btnSignIn = findViewById(R.id.button_signin);
        btnSignIn.setOnClickListener(signInListner);
        textNoAccount = findViewById(R.id.txtNoAccount);
        textNoAccount.setOnClickListener(createAccountListner);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mUser = mAuth.getCurrentUser();
        if(mUser != null) { redirect(); }
    }

        /* access user infos
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
if (user != null) {
    // Name, email address, and profile photo Url
    String name = user.getDisplayName();
    String email = user.getEmail();
    Uri photoUrl = user.getPhotoUrl();

    // Check if user's email is verified
    boolean emailVerified = user.isEmailVerified();

    // The user's ID, unique to the Firebase project. Do NOT use this value to
    // authenticate with your backend server, if you have one. Use
    // FirebaseUser.getToken() instead.
    String uid = user.getUid();
}
     */


}
