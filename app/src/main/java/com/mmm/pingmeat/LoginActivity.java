package com.mmm.pingmeat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    //region Variables

    private final static int requestIdGoogle = 1;
    private static final String TAG = "LoginActivity";

    // Auth
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    GoogleSignInClient mGoogleSignInClient;

    // Db
    DatabaseReference mDatabase;

    // UI
    EditText emailText;
    EditText passwordText;
    RelativeLayout signInButton;
    RelativeLayout googleSignInButton;
    TextView createAccountText;
    TextView reinitPassText;
    private ProgressBar progressBar;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // signout for debug
        FirebaseAuth.getInstance().signOut();

        // recupere l'instance de FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        // recupere l'instance de FirebaseDatabase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // recupere les composant UI
        emailText = findViewById(R.id.editUser);
        passwordText = findViewById(R.id.editPass);
        signInButton = findViewById(R.id.button_signin);
        signInButton.setOnClickListener(signInListner);
        googleSignInButton = findViewById(R.id.googleSignInButton);
        googleSignInButton.setOnClickListener(signInGoogleListner);
        createAccountText = findViewById(R.id.txtNoAccount);
        createAccountText.setOnClickListener(createAccountListner);
        reinitPassText = findViewById(R.id.textReinitPass);
        reinitPassText.setOnClickListener(reinitPasswordListner);
        progressBar = findViewById(R.id.progressBar);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mUser = mAuth.getCurrentUser();
        if(mUser != null) { redirect(); }

    }

    //region Sign In
    View.OnClickListener signInListner = new View.OnClickListener() {
        public void onClick(View v) { signIn(); }
    };

    public void signIn()
    {
        String email = emailText.getText().toString();
        String pass = passwordText.getText().toString();
        if (!email.equals(null) && !pass.equals(null) && !email.equals("") && !pass.equals("") ) {
            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                Toast.makeText(LoginActivity.this, "Authentication success",Toast.LENGTH_SHORT).show();
                                mUser = mAuth.getCurrentUser();
                                redirect();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else {
            Toast.makeText(LoginActivity.this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
        }
    }
    //endregion

    //region Sign In Google
    View.OnClickListener signInGoogleListner = new View.OnClickListener() {
        public void onClick(View v) { signInGoogle(); }
    };

    public void signInGoogle()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, requestIdGoogle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == requestIdGoogle) {
            // The Task returned from this call is always completed, no need to attach a listener.
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
            Toast.makeText(LoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
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
                            TryInsertIntoDb();
                            redirect();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("lol", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void TryInsertIntoDb()
    {

    }
    //endregion

    //region Sign Up
    View.OnClickListener createAccountListner = new View.OnClickListener() {
        public void onClick(View v) { createAccount(); }
    };

    public void createAccount()
    {
        Intent createAccount = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(createAccount);
    }
    //endregion

    //region Reinit Password
    View.OnClickListener reinitPasswordListner = new View.OnClickListener() {
        public void onClick(View v) { reinitPassword(); }
    };

    public void reinitPassword()
    {
        String email = emailText.getText().toString();
        if(email != null && !email.equals(""))
        {
            mAuth.sendPasswordResetEmail(email);
            Toast.makeText(LoginActivity.this, "Une réinitialisation de mot de passe à été envoyée sur votre mail.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(LoginActivity.this, "Veuillez renseigner un e-mail valide.", Toast.LENGTH_SHORT).show();
        }
    }
    //endregion

    private void redirect()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference("Gerant");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Intent i = null;
                for (DataSnapshot gerant : dataSnapshot.getChildren()) {
                    if (gerant.getKey().equals(mAuth.getUid())) {
                        i = new Intent(LoginActivity.this,HomeGerantActivity.class);
                        startActivity(i);
                        break;
                    }
                }

                if (i == null) {
                    i = new Intent(LoginActivity.this,HomeClientActivity.class);
                    startActivity(i);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
