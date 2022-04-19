package com.wambuacooperations.hideandshowdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    TextView displayTextView;
    public void show(View view){
        displayTextView.setVisibility(View.VISIBLE);
    }
    public void hide(View view){
        displayTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayTextView=findViewById(R.id.displayTextView);
    }
}
