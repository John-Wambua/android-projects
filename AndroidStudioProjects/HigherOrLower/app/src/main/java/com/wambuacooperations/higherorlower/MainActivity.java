package com.wambuacooperations.higherorlower;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText guessedNumberEditText;
    int max=20;
    int min=1;
    int correctNumber;

    public void createRandomNumber(){
        correctNumber=(int)(Math.random()*((max-min)+1))+min;
    }
    public void clear(){
        guessedNumberEditText.setText("");
    }


    public void higherOrLower(View view){
        guessedNumberEditText=(EditText) findViewById(R.id.guessedNumberEditText);
        
         if(guessedNumberEditText.getText().toString().isEmpty()||Integer.parseInt(guessedNumberEditText.getText().toString())==0||Integer.parseInt(guessedNumberEditText.getText().toString())>20){
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
             clear();
        }
         else{
             int guessedNumber=Integer.parseInt(guessedNumberEditText.getText().toString());
             String message;

             if(guessedNumber>correctNumber){
                 // Lower
                 message="Lower";
                 clear();
             }
             else if(guessedNumber<correctNumber){
                 //Higher
                 message="Higher";
                 clear();
             }
             else {
                 // Correct answer
                 message="You got it, Try again?";
                 clear();
                 createRandomNumber();
             }

             Toast.makeText(this,message, Toast.LENGTH_LONG).show();

         }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       createRandomNumber();

    }
}
