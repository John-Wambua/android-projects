package com.wambuacooperations.timestables;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView timesTableListView;
    SeekBar numberSeekBar;

    public void getTimesTableNumer(int timesTableNumber){
        timesTableListView=findViewById(R.id.timesTableListView);

        ArrayList<String> timesTable=new ArrayList<String>();
        for(int i=1; i<= 100; i++) {
            timesTable.add(Integer.toString(timesTableNumber*i));
        }
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,timesTable);
        timesTableListView.setAdapter(arrayAdapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int max=20;
        int startingPosition=10;

       numberSeekBar=findViewById(R.id.numberSeekBar);
       numberSeekBar.setMax(max);
       numberSeekBar.setProgress(startingPosition);
       getTimesTableNumer(startingPosition);

       numberSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               int min=1;
               int timesTableNumber;
               if(progress<min){
                   timesTableNumber=min;
                   seekBar.setProgress(timesTableNumber);
               }else{
                   timesTableNumber=progress;
               }
               getTimesTableNumer(timesTableNumber);
           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {

           }
       });








    }
}
