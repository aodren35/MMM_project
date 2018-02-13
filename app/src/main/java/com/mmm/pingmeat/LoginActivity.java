package com.mmm.pingmeat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    EditText userText;
    EditText passwordText;
    RelativeLayout btnSignIn;
    TextView textNoAccount;

    RelativeLayout btnSignInGoogle;
    GoogleSignInClient mGoogleSignInClient;

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

    View.OnClickListener signInGoogleListner = new View.OnClickListener() {
        public void onClick(View v) { signInGoogle(); }
    };

    public void signInGoogle()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("lol", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginActivity.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("lol", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("lol", "signInWithCredential:success");
                            mUser = mAuth.getCurrentUser();
                            redirect();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("lol", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
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
        btnSignInGoogle = findViewById(R.id.googleSignInButton);
        btnSignInGoogle.setOnClickListener(signInGoogleListner);
        textNoAccount = findViewById(R.id.txtNoAccount);
        textNoAccount.setOnClickListener(createAccountListner);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id_google))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // signout for debug
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    protected void onStart()
    {
        super.onStart();



        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) { redirect();}
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
