package com.wambuacooperations.thearttic.posts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wambuacooperations.thearttic.R;

public class SinglePostActivity extends AppCompatActivity {

    private ImageView singleImage;
    private TextView singleTitle,singleDesc;
    private Button deleteBtn;
    String post_key=null;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        singleImage=findViewById(R.id.singleImageview);
        singleTitle=findViewById(R.id.singleTitle);
        singleDesc=findViewById(R.id.singleDesc);

        post_key=getIntent().getStringExtra("PostID");
        deleteBtn=findViewById(R.id.deleteBtn);
        mAuth=FirebaseAuth.getInstance();
        deleteBtn.setVisibility(View.INVISIBLE);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                                FirebaseDatabase.getInstance().getReference().child("Posts").child(post_key).removeValue();
                                Intent mainIntent=new Intent(SinglePostActivity.this, MainActivity.class);
                                startActivity(mainIntent);

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Posts").child(post_key)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String post_title= (String) snapshot.child("title").getValue();
                        String post_desc= (String) snapshot.child("desc").getValue();
                        String post_image= (String) snapshot.child("postImage").getValue();
                        String post_uid= (String) snapshot.child("uid").getValue();

                        singleTitle.setText(post_title);
                        singleDesc.setText(post_desc);
                        Glide
                                .with(getApplicationContext())
                                .load(post_image)
                                .into(singleImage);
                        if (mAuth.getCurrentUser().getUid().equals(post_uid)){
                            deleteBtn.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
