package com.wambuacooperations.emailfirebasesignin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LandingPageActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    ListView displayNamesListView;
    ArrayList<String> names=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        mAuth=FirebaseAuth.getInstance();

        displayNamesListView=findViewById(R.id.displayNamesListView);
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        displayNamesListView.setAdapter(arrayAdapter);

        FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String email=dataSnapshot.child("email").getValue().toString();
                names.add(email);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();

        menuInflater.inflate(R.menu.landing_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);

         if(item.getItemId()==R.id.logout){
             mAuth.signOut();
             finish();
             launchLogin();
             return  true;

         }else if(item.getItemId()==R.id.account){
            Intent intent =new Intent(LandingPageActivity.this,AccountActivity.class);
             String imageURL=getIntent().getStringExtra("imageURL");
             intent.putExtra("imageURL",imageURL);
             startActivity(intent);
             return true;
         }
         else {
             return false;
         }
    }
    @Override
    public void onBackPressed() {//Logout the user when they press the back button
        super.onBackPressed();
        mAuth.signOut();
        launchLogin();
    }
    public void launchLogin(){
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);

    }}
