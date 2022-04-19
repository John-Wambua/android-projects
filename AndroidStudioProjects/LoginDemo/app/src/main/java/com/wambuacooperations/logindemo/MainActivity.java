package com.wambuacooperations.logindemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText username,password;

    public void loginClick(View view){
        username=(EditText) findViewById(R.id.usernameEditText);
        password=(EditText) findViewById(R.id.passwordEditText);

        Log.i("username",username.getText().toString());
        Log.i("password",password.getText().toString());
        Toast.makeText(this, "Hey! Hey! Hey!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
