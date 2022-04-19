package com.wambuacooperations.joke;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import static com.wambuacooperations.joke.MessageService.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startClicked(View view){
        Intent intent=new Intent(this,MessageService.class);
        intent.putExtra(EXTRA_MESSAGE,getString(R.string.button_response));
        startService(intent);
    }
}
