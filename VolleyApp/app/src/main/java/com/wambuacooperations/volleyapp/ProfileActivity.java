package com.wambuacooperations.volleyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import static com.wambuacooperations.volleyapp.adapters.DevelopersAdapter.KEY_IMAGE;
import static com.wambuacooperations.volleyapp.adapters.DevelopersAdapter.KEY_NAME;
import static com.wambuacooperations.volleyapp.adapters.DevelopersAdapter.KEY_URL;

public class ProfileActivity extends AppCompatActivity {

    ImageView profileImageView;
    TextView userNameTextView;
    TextView developerUrl;
    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImageView=findViewById(R.id.profileImageView);
        userNameTextView=findViewById(R.id.usernameTextView);
        developerUrl=findViewById(R.id.developerUrl);

        Intent intent=getIntent();

        username=intent.getStringExtra(KEY_NAME);
        String image=intent.getStringExtra(KEY_IMAGE);
        final String profileUrl=intent.getStringExtra(KEY_URL);

        Glide.with(getApplicationContext())
                .load(image)
                .fitCenter()
                .into(profileImageView);
        userNameTextView.setText(username);
        developerUrl.setText(profileUrl);

        developerUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse(profileUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });

    }

    public void shareProfile(View view){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("*/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this awesome developer "+username+","+developerUrl);
        Intent chooser=Intent.createChooser(shareIntent,"Share via");
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }
}
