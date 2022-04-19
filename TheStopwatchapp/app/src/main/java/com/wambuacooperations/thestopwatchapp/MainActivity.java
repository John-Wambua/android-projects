package com.wambuacooperations.thestopwatchapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int seconds=0;
    boolean running;
    TextView stopwatchTextView;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState!=null){
            seconds=savedInstanceState.getInt("seconds");
            running=savedInstanceState.getBoolean("running");
        }

        stopwatchTextView=findViewById(R.id.stopwatchTextView);
        runTimer();

    }

    public void onClickStart(View view){
        running=true;
    }
    public void onClickStop(View view){
        running=false;
    }
    public void onClickReset(View view){
        running=false;
        seconds=0;
    }

    private void runTimer(){
        handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours=seconds/3600;
                int minutes=(seconds%3600)/60;
                int secs=seconds%60;
                String secondsString=Integer.toString(secs);
                String minuteString=Integer.toString(minutes);
                String hourString =Integer.toString(hours);

                stopwatchTextView.setText(hourString+":"+formatString(minuteString,minutes)+":"+formatString(secondsString,secs));

                if(running){
                    seconds++;
                }

                handler.postDelayed(this,1000);
            }
        });

    }
    public String formatString(String time,int unit){
        if(time.equals("0")){
            time="00";
        }else if(unit<=9){
            time="0"+time;
        }
        return time;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putInt("seconds", seconds);
        // etc.
    }


}
