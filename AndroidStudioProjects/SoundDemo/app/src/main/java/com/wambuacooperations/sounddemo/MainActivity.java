package com.wambuacooperations.sounddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    SeekBar volumeSeekBar,scrubSeekBar;
    AudioManager audioManager;

    public void play(View view){

        mediaPlayer.start();
    }

    public void pause(View view){

        mediaPlayer.pause();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer=MediaPlayer.create(this,R.raw.old_car_engine_daniel_simion);
        audioManager= (AudioManager) getSystemService(AUDIO_SERVICE);

        int maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);// to get the current volume of the media player

        //VOLUME SEEK BAR
        volumeSeekBar=(SeekBar) findViewById(R.id.volumeSeekBar);
        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setProgress(currentVolume); //To set initial volume ot the current volume

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("Seekbar changed",Integer.toString(progress));

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //SCRUB SEEKBAR
        scrubSeekBar=(SeekBar) findViewById(R.id.scrubSeekBar);
        scrubSeekBar.setMax(mediaPlayer.getDuration());

        scrubSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("Scrub Seekbar change",Integer.toString(progress));
                mediaPlayer.seekTo(progress); //To control audio play
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Update scrub seekbar to new value of current position
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                scrubSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        },0,300);
    }
}
