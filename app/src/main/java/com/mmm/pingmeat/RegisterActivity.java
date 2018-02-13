package com.mmm.pingmeat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    EditText usernameText;
    EditText mailText;
    EditText passwordText;
    EditText confirmText;
    RadioButton gerantRadio;
    RadioButton clientRadio;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        usernameText = findViewById(R.id.editUsername);
        mailText = findViewById(R.id.editMail);
        passwordText = findViewById(R.id.editPassword);
        confirmText = findViewById(R.id.editConfirmPassword);
        gerantRadio = findViewById(R.id.radioG);
        clientRadio = findViewById(R.id.radioC);
        btnSignUp = findViewById(R.id.button_register);
        btnSignUp.setOnClickListener(registerListner);
    }

    public void onRadioButtonClicked(View view)
    {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioC:
                if (checked)
                    break;
            case R.id.radioG:
                if (checked)
                    break;
        }
    }

    View.OnClickListener registerListner = new View.OnClickListener() {
        public void onClick(View v) { signUp(); }
    };

    public void signUp()
    {
        String email = mailText.getText().toString();
        String password = passwordText.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // Log.d(TAG, "createUserWithEmail:success");
                            mUser = mAuth.getCurrentUser();
                            redirect();
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                            //        Toast.LENGTH_SHORT).show();
                            //redirect();
                        }

                        // ...
                    }
                });
    }

    public void redirect()
    {
        Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(i);
    }

}
