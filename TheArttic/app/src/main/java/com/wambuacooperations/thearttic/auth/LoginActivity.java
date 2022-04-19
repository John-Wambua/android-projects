package com.wambuacooperations.thearttic.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wambuacooperations.thearttic.posts.MainActivity;
import com.wambuacooperations.thearttic.R;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail,loginPassword;
    FirebaseAuth mAuth;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        loginEmail=findViewById(R.id.login_email);
        loginPassword=findViewById(R.id.login_password);
        mAuth=FirebaseAuth.getInstance();
    }

    public void loginClicked(View view){

        Toast.makeText(this, "PROCESSING....", Toast.LENGTH_LONG).show();
        String email=loginEmail.getText().toString().trim();
        String password=loginPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
            new AlertDialog.Builder(this)
                    .setTitle("Login Error!")
                    .setMessage("Please fill all the fields")
                    .show();
        }else{

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                             
                                checkUserExistence();
//                                Toast.makeText(LoginActivity.this, "SignIn Successful!", Toast.LENGTH_SHORT).show();
                                
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LoginActivity.this, "Login Failed!, User not found", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }

    }

    private void checkUserExistence() {
        final String userID=mAuth.getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userID)){
                    Intent mainPage=new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(mainPage);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "User not Registered", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void signUpRedirect(View view){
        Intent signUp=new Intent(this, RegisterActivity.class);
        startActivity(signUp);
        finish();
    }
}
