package com.mmm.pingmeat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mmm.pingmeat.models.Client;
import com.mmm.pingmeat.models.Gerant;

public class RegisterActivity extends AppCompatActivity {


    //region Variables

    // Auth
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    // Db
    FirebaseDatabase fireDb;
    DatabaseReference mDatabase;

    // UI
    EditText usernameText;
    EditText mailText;
    EditText passwordText;
    EditText confirmText;
    RadioButton gerantRadio;
    RadioButton clientRadio;
    RelativeLayout registerButton;

    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setTitle("S'enregistrer");
        setContentView(R.layout.activity_register);
        // recupere l'instance de FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        // recupere l'instance de FirebaseDatabase
        fireDb = FirebaseDatabase.getInstance();
        mDatabase = fireDb.getInstance().getReference();
        // recupere les elements de la vue
        usernameText = findViewById(R.id.editUsername);
        mailText = findViewById(R.id.editMail);
        passwordText = findViewById(R.id.editPassword);
        confirmText = findViewById(R.id.editConfirmPassword);
        gerantRadio = findViewById(R.id.radioG);
        clientRadio = findViewById(R.id.radioC);
        registerButton = findViewById(R.id.button_signup);
        registerButton.setOnClickListener(registerListner);
    }

    View.OnClickListener registerListner = new View.OnClickListener() {
        public void onClick(View v) { signUp(); }
    };

    public void signUp()
    {
        final String email = mailText.getText().toString();
        final String password = passwordText.getText().toString();
        String confirm = confirmText.getText().toString();
        if(!password.equals(confirm)) {
            Toast.makeText(RegisterActivity.this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Register", "createUserWithEmail:success");
                            mAuth.signInWithEmailAndPassword(email,password);
                            mUser = mAuth.getCurrentUser();
                            mUser.sendEmailVerification();
                            InsertUserInDb();
                            redirect();
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w("Register", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "L'enregistrement de l'utilisateur à échoué.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void InsertUserInDb()
    {
        String userId = mAuth.getUid();
        if(userId != null) {
            String username = usernameText.getText().toString();
            String email = mailText.getText().toString();
            //String password = passwordText.getText().toString();
            if (clientRadio.isChecked()) {
                Client c = new Client(username, email, "");
                mDatabase.child("Client").child(userId).setValue(c);
            } else {
                Gerant g = new Gerant(username, email, "");
                mDatabase.child("Gerant").child(userId).setValue(g);
            }
        }
    }

    public void redirect()
    {
        Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(i);
    }

}
