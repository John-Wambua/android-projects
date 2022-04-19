package com.wambuacooperations.timerdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /***
         * Timers - 1st method
         */
//        final Handler handler=new Handler();
//        Runnable runnable=new Runnable() {
//            @Override
//            public void run() {
//                Log.i("Hey it's me","two seconds have passed");
//                handler.postDelayed(this,2000);
//            }
//        };
//        handler.post(runnable);

        /***
         *  Timers - 2nd method: Using Countdown timer
         */
        new CountDownTimer(10000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                //Run after each countdown interval is reached
                Log.i("Seconds left",Long.toString(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                // After the end of the countdown
                Log.i("Countdown finished","No more countdowns");
            }
        }.start();
    }
}
