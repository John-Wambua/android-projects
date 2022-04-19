package com.wambuacooperations.droidcafev1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DessertsActivity extends AppCompatActivity {

    TextView donutTitle,donutDescription;
    ImageView donutImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desserts);




        donutTitle=findViewById(R.id.dessert_title);
        donutDescription=findViewById(R.id.dessert_description);
        donutImage=findViewById(R.id.dessert_image);

        donutTitle.setText(getIntent().getStringExtra("title"));
        donutDescription.setText(getIntent().getStringExtra("description"));
        Glide.with(getApplicationContext())
                .load(getIntent().getIntExtra("image",0))
                .fitCenter()
                .into(donutImage);


    }
}
