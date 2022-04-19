package com.wambuacooperations.eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    SeekBar seekbar;
    TextView timerTextView;
    Boolean counterActive=false;
    CountDownTimer countDownTimer;
    Button startButton;

    public void resetTimer(){
        seekbar.setEnabled(true);
        timerTextView.setText("0:30");
        countDownTimer.cancel();
        seekbar.setProgress(30);
        startButton.setText("GO");
        counterActive=false;

    }

    public void startCountDown(View view){

        if(counterActive){
           resetTimer();
        }else {

            startButton.setText("STOP");
            counterActive = true;
            seekbar.setEnabled(false);

            countDownTimer = new CountDownTimer(seekbar.getProgress() * 1000 + 100, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    updateTimer((int) millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.fire_truck_air_horn);
                    mediaPlayer.start();
                    resetTimer();
                }
            };
            countDownTimer.start();
        }
    }

    public void updateTimer(int progress){
            int minutes = progress / 60;
            int seconds = progress - (minutes * 60);
            String secondString = Integer.toString(seconds);

            if (secondString.equalsIgnoreCase("0")) {
                secondString = "00";
            } else if (seconds <= 9) {
                secondString = "0" + secondString;
            }

            //Set the time on the TextView
            timerTextView = findViewById(R.id.timerTextView);
            timerTextView.setText(Integer.toString(minutes) + ":" + secondString);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int max=600;
        int startingPosition=30;

        startButton=findViewById(R.id.buttonGo);
        seekbar=findViewById(R.id.setTimeSeekBar);
        seekbar.setMax(max);
        seekbar.setProgress(startingPosition);
        updateTimer(startingPosition);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTimer(progress);
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
