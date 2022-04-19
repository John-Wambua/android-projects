package com.wambuacooperations.snapchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Snapshot;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SnapsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    ListView snapsListView;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> emails=new ArrayList<>();
    ArrayList<DataSnapshot> snaps=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snaps);
        mAuth=FirebaseAuth.getInstance();
        snapsListView= findViewById(R.id.snapsListView);
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,emails);
        snapsListView.setAdapter(arrayAdapter);

        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("snaps").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                emails.add(dataSnapshot.child("from").getValue().toString());
                snaps.add(dataSnapshot);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                //Remove snap from snaps and emails Array list when deleted

                int index=0;
                for(DataSnapshot snap: snaps){
                    if(snap.getKey()==dataSnapshot.getKey()){
                        snaps.remove(index);
                        emails.remove(index);
                    }
                    index++;
                }
                arrayAdapter.notifyDataSetChanged();

            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        snapsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataSnapshot snapshot=snaps.get(position);

                Intent intent=new Intent(SnapsActivity.this,ViewSnapActivity.class);
                intent.putExtra("imageName",snapshot.child("imageName").getValue().toString());
                intent.putExtra("imageURL",snapshot.child("imageURL").getValue().toString());
                intent.putExtra("message",snapshot.child("message").getValue().toString());
                intent.putExtra("snapKey",snapshot.getKey());

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.snaps,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId()==R.id.createSnap){

            Intent intent=new Intent(this,CreateSnapActivity.class);
            startActivity(intent);
            return true;
        }else if(item.getItemId()==R.id.logout){

            mAuth.signOut();
            finish();
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
    }
}
