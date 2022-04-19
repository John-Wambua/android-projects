package com.wambuacooperations.snapchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.wambuacooperations.snapchat.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectUserActivity extends AppCompatActivity {

    ListView chooseUserListView;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String>  emails=new ArrayList<>();
    ArrayList<String>  keys=new ArrayList<>();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);
        chooseUserListView=findViewById(R.id.chooseUserListView);

        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,emails);
        chooseUserListView.setAdapter(arrayAdapter);

        /**Add emails from firebase database to the listView**/

        FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String email=dataSnapshot.child("email").getValue().toString();
                emails.add(email);
                keys.add(dataSnapshot.getKey());
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

        chooseUserListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**WE ADD TO FIREBASE DATABASE:*
                 * A new child called snaps with the following children:
                 * from, imageName,imageURL,message*/
                Map<String,String> snapMap=new HashMap<>();
                snapMap.put("from",FirebaseAuth.getInstance().getCurrentUser().getEmail());
                snapMap.put("imageName",getIntent().getStringExtra("imageName"));
                snapMap.put("imageURL",getIntent().getStringExtra("imageURL"));
                snapMap.put("message",getIntent().getStringExtra("message"));

                FirebaseDatabase.getInstance().getReference().child("users").child(keys.get(position)).child("snaps").push().setValue(snapMap);

                Intent intent=new Intent(getApplicationContext(),SnapsActivity.class);

                //Disable back button; Keep it where it is
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }
}
