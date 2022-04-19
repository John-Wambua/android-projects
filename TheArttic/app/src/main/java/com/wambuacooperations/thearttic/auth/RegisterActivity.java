package com.wambuacooperations.thearttic.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.wambuacooperations.thearttic.R;

public class RegisterActivity extends AppCompatActivity {

    EditText usernameInput,emailInput,passwordInput;
    TextView loginRedirect;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usernameInput=findViewById(R.id.usernameField);
        emailInput=findViewById(R.id.emailField);
        passwordInput=findViewById(R.id.passwordField);
        loginRedirect=findViewById(R.id.loginTxtView);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser!=null){
            //User is already registered
            //We therefore login the user directly
            moveToLogin();
        }
    }

    public void registerClicked(View view){

        final String username=usernameInput.getText().toString();
        String email=emailInput.getText().toString();
        String password=passwordInput.getText().toString();
        if (TextUtils.isEmpty(username)||TextUtils.isEmpty(email)|| TextUtils.isEmpty(password)) {
            new AlertDialog.Builder(this)
                    .setTitle("Registration Error!")
                    .setMessage("Please fill all the fields")
                    .show();

        }else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                /**Add user to DB**/
                                String user_id = mAuth.getCurrentUser().getUid();
                                FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Username").setValue(username);
                                FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Image").setValue("Default");
                                moveToProfile();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("REGISTRATION FAILURE", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }

    }

    public void loginRedirect(View view){
       moveToLogin();
    }

    public void moveToLogin(){
        //Move to next activity
        Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);

    }
    public void moveToProfile(){
        Intent profIntent=new Intent(RegisterActivity.this, ProfileActivity.class);
        profIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(profIntent);
    }
}
