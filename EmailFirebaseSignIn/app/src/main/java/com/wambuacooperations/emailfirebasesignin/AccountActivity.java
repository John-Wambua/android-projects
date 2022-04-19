package com.wambuacooperations.emailfirebasesignin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AccountActivity extends AppCompatActivity {

    ListView accountListView;
    ArrayList<String> accountDetails=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    FirebaseAuth mAuth;
    ImageView accountProfileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        accountListView = findViewById(R.id.accountListView);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, accountDetails);
        accountListView.setAdapter(arrayAdapter);
        mAuth = FirebaseAuth.getInstance();
        accountProfileImage = findViewById(R.id.Account_profile_image);




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            for (UserInfo profile : user.getProviderData()) {
//                // Id of the provider (ex: google.com)
//                String providerId = profile.getProviderId();
//
//                // UID specific to the provider
//                String uid = profile.getUid();
//
//                // Name, email address, and profile photo Url
//                String name = profile.getDisplayName();
//                String email = profile.getEmail();
//                Uri photoUrl = profile.getPhotoUrl();
//                Log.i("NAME",name);
//                Log.i("email",email);
////                Log.i("photoURL",photoUrl.toString());
//                Log.i("uid",uid);
//            }
//        }
            FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String firstName=dataSnapshot.child("firstName").getValue().toString();
                    String lastName=dataSnapshot.child("lastName").getValue().toString();
                    String email=dataSnapshot.child("email").getValue().toString();
                    //String imageURL=dataSnapshot.child("imageURL").getValue().toString();

                    Log.i("FIRST NAME",firstName);
                    Log.i("LAST NAME",lastName);
                    Log.i("EMAIL",email);
                    //Log.i("imageURL",imageURL);

                    accountDetails.add(firstName);
                    accountDetails.add(lastName);
                    accountDetails.add(email);
                    arrayAdapter.notifyDataSetChanged();


//                    ImageDownloader imageDownloader = new ImageDownloader();
//                    Bitmap myImage;
//
//                    try {
//                        myImage = imageDownloader.execute(imageURL).get();
//                        accountProfileImage.setImageBitmap(myImage);
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        }
               }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }


    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) { //Returns an image : Bitmap
            try {
                URL url =new URL(urls[0]);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();

                InputStream inputStream=connection.getInputStream();

                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);

                return  bitmap;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        };
    }
}
