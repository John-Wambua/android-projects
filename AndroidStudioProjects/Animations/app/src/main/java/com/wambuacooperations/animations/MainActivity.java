package com.wambuacooperations.animations;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView bartImageView;
    ImageView homerImageView;

   // boolean bartIsShowing=true;

    public void fade(View view){
        bartImageView=(ImageView) findViewById(R.id.bartImageView);
        homerImageView=(ImageView) findViewById(R.id.homerImageView);

        /***
         * Fade in and fade out
         */

//        if (bartIsShowing) {
//            bartImageView.animate().alpha(0).setDuration(2000);
//            homerImageView.animate().alpha(1).setDuration(2000);
//            bartIsShowing=false;
//        }else{
//            bartImageView.animate().alpha(1).setDuration(2000);
//            homerImageView.animate().alpha(0).setDuration(2000);
//            bartIsShowing=true;
//        }

        /****
         * Translation animations (movements)
         */
        //bartImageView.animate().translationYBy(1000).setDuration(2000);
        //bartImageView.animate().translationXBy(-500).setDuration(2000);

        /****
         * Rotations
         */
        //bartImageView.animate().rotation(180).setDuration(1000);

        /****
         * Scale
         */
        bartImageView.animate().scaleX(0.5f).scaleY(0.5f).setDuration(2000);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bartImageView=(ImageView) findViewById(R.id.bartImageView);
        bartImageView.setX(-1000);
        bartImageView.animate().translationXBy(1000).rotation(360).setDuration(2000);
    }
}
