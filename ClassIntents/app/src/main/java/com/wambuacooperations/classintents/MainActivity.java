package com.wambuacooperations.classintents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> players;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=findViewById(R.id.listView);
        players=new ArrayList<String>(asList("Messi","Ronaldo","Griezmann","Pogba","Mbappe"));
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,players);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,SecondActivity.class);
                intent.putExtra("player",players.get(position));
                startActivity(intent);
            }
        });
    }
}
