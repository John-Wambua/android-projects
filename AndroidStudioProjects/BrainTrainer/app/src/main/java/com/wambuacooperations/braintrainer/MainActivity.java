package com.wambuacooperations.braintrainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.invoke.ConstantCallSite;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button goButton;
    TextView sumTextView,resultTextView,scoreTextView,timerTextView;
    ArrayList<Integer> answers=new ArrayList<Integer>();
    Button button0,button1,button2,button3,playAgainButton;
    ConstraintLayout secondConstraintLayout;

    int locationOfCorrectAnswer;
    int gameNumberLimit=20;
    int score=0;
    int numberOfGames=0;
    boolean gamePlaying;

    public void startGame(View view){
        goButton.setVisibility(View.INVISIBLE);


    }

    public void playAgain(View view){

        playAgainButton.setVisibility(View.INVISIBLE);
        resultTextView.setVisibility(View.INVISIBLE);

        score=0;
        numberOfGames=0;
        timerTextView.setText("30s");
        scoreTextView.setText(Integer.toString(score) + "/" + Integer.toString(numberOfGames));

        updateGame();

        new CountDownTimer(30100,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(Long.toString(millisUntilFinished/1000)+"s");
                gamePlaying=true;
            }

            @Override
            public void onFinish() {
                //game is finished
                gamePlaying=false;
                resultTextView.setText("Done!");
                playAgainButton.setVisibility(View.VISIBLE);

            }
        }.start();

    }

    public void checkAnswer(View view){
            if (Integer.toString(locationOfCorrectAnswer).equals(view.getTag().toString())) {
                resultTextView.setVisibility(View.VISIBLE);
                resultTextView.setText("Correct!");
                score++;

            } else {
                resultTextView.setVisibility(View.VISIBLE);
                resultTextView.setText("Wrong:(");
            }
            numberOfGames++;
            scoreTextView.setText(Integer.toString(score) + "/" + Integer.toString(numberOfGames));
            updateGame();
        }

    public void updateGame(){

            Random rand = new Random();
            int num1 = rand.nextInt(gameNumberLimit + 1);
            int num2 = rand.nextInt(gameNumberLimit + 1);

            sumTextView.setText(Integer.toString(num1) + "+" + Integer.toString(num2));
            locationOfCorrectAnswer = rand.nextInt(4);

            answers.clear();

            for (int i = 0; i < 4; i++) {
                if (i == locationOfCorrectAnswer) {
                    answers.add(num1 + num2);
                } else {
                    int wrongAnswer = rand.nextInt((gameNumberLimit * 2) + 1);
                    while (wrongAnswer == num1 + num2) {
                        wrongAnswer = rand.nextInt((gameNumberLimit * 2) + 1);
                    }
                    answers.add(wrongAnswer);
                }
            }


            button0.setText(Integer.toString(answers.get(0)));
            button1.setText(Integer.toString(answers.get(1)));
            button2.setText(Integer.toString(answers.get(2)));
            button3.setText(Integer.toString(answers.get(3)));

        }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        goButton=findViewById(R.id.goButton);
        sumTextView=findViewById(R.id.sumTextView);
        button0=findViewById(R.id.button0);
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);
        resultTextView=findViewById(R.id.resTextView);
        scoreTextView=findViewById(R.id.scoreTextView);
        timerTextView=findViewById(R.id.timerTextView);
        playAgainButton=findViewById(R.id.playAgainButton);

        playAgain(findViewById(R.id.timerTextView));
        goButton.setVisibility(View.INVISIBLE);

    }
}
