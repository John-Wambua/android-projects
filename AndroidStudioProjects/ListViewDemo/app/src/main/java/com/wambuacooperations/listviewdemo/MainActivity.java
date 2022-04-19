package com.wambuacooperations.listviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public class MainActivity extends AppCompatActivity {

    ListView myListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myListView=findViewById(R.id.myListView);

        final ArrayList<String> myFriends=new ArrayList<String>();
        myFriends.add("Marto");
        myFriends.add("TP");
        myFriends.add("Rodney");
        myFriends.add("Carol");
        myFriends.add("Debby");
        myFriends.add("Rachael");

       // OR
        // final ArrayList<String> myFriends=new ArrayList<String>(asList("Marto","..."));

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,myFriends);
        myListView.setAdapter(arrayAdapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), myFriends.get(position), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
