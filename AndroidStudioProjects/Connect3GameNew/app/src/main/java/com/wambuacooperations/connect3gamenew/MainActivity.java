package com.wambuacooperations.connect3gamenew;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // 0: yellow 1: red 2: empty

    int activePlayer=0;
    int tappedCounter;
    boolean gamePlaying=true;
    int [] gameState={2,2,2,2,2,2,2,2,2};
    int [][] winningPositions={{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
    ImageView counter;
    TextView winnerTextView;
    Button playAgainButton;

    public void dropIn(View view){
        counter=(ImageView) view;
        tappedCounter=Integer.parseInt(counter.getTag().toString());

        if(gameState[tappedCounter]==2&&gamePlaying) {
            counter.setTranslationY(-2000);
            if (activePlayer == 0) {
                counter.setImageResource(R.drawable.yellow);
                activePlayer = 1;
            } else {
                counter.setImageResource(R.drawable.red);
                activePlayer = 0;
            }
            counter.animate().translationYBy(2000).rotation(3600).setDuration(100);

            gameState[tappedCounter] = activePlayer;

            for (int[] winningPosition : winningPositions) {
                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] && gameState[winningPosition[1]] == gameState[winningPosition[2]] && gameState[winningPosition[2]] != 2) {
                    // Game won
                    gamePlaying=false;
                    String message = "";
                    if (activePlayer == 1) {
                        message = "Yellow";
                    } else {
                        message = "Red";
                    }

                    winnerTextView=(TextView) findViewById(R.id.winnerTextView);
                    playAgainButton=(Button) findViewById(R.id.playAgainButton);

                    winnerTextView.setVisibility(View.VISIBLE);
                    playAgainButton.setVisibility(View.VISIBLE);
                    winnerTextView.setText(message + " has won!");
                    //Toast.makeText(this, message + " has won!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public void playAgain(View view){
        activePlayer=0;
        gamePlaying=true;

        winnerTextView=(TextView) findViewById(R.id.winnerTextView);
        playAgainButton=(Button) findViewById(R.id.playAgainButton);

        winnerTextView.setVisibility(View.INVISIBLE);
        playAgainButton.setVisibility(View.INVISIBLE);

        for(int i=0;i<gameState.length;i++){
            gameState[i]=2;
        }
        androidx.gridlayout.widget.GridLayout gridLayout = findViewById(R.id.gridLayout);
        for(int i=0; i<gridLayout.getChildCount(); i++) {
            ImageView counter = (ImageView) gridLayout.getChildAt(i);
            counter.setImageDrawable(null);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
