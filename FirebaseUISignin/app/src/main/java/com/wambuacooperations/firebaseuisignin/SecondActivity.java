package com.wambuacooperations.firebaseuisignin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class SecondActivity extends AppCompatActivity {


    List<AuthUI.IdpConfig> providers;
    final int RC_SIGN_IN = 1822;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this,MainActivity.class));
            this.finish();
        }

    }

    public void loginRegisterClicked(View view) {
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build());

       showSignInOptions();
    }

    public void showSignInOptions(){
        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTosAndPrivacyPolicyUrls(
                                "https://example.com/terms.html",
                                "https://example.com/privacy.html")
                        .build(),
                RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            //IdpResponse response=IdpResponse.fromResultIntent(data);

            if (requestCode == RESULT_OK) {

                //We have signed in the user or we have a new user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d("onActivityResult",user.getEmail());
                if (user.getMetadata().getCreationTimestamp()==user.getMetadata().getLastSignInTimestamp()){
                    Toast.makeText(this, "Welcome new User", Toast.LENGTH_SHORT).show();
                }else{
                    // ...Returning user

                    Toast.makeText(this, "Welcome back again", Toast.LENGTH_SHORT).show();
                }
                Intent intent=new Intent(SecondActivity.this,MainActivity.class);
                startActivity(intent);
                this.finish();

            } else {
                //Sign in failed
                Toast.makeText(this, "Error!!!!!!!!!!", Toast.LENGTH_SHORT).show();
               // Toast.makeText(this, response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Request code does not match", Toast.LENGTH_SHORT).show();
        }
    }

}


