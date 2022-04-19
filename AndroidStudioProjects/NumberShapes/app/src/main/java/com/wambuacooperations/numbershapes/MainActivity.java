package com.wambuacooperations.numbershapes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText numberEditText;

    public void clear(){
        numberEditText.setText("");
    }

    class NumberShape{

        int number;

        NumberShape(int num){
            this.number=num;

        }
        public boolean isTriangular(){
            //An integer x is triangular if and only if 8x + 1 is a square
            long calc_num = (8*this.number)+1;
            long t = (long) Math.sqrt(calc_num);
            if (t*t==calc_num) {
                return true;
            }
            return false;
        }
        public boolean isSquare(){
            for (int i = 0; i < this.number / 2 + 2; i++)
            {
                if (i * i == this.number)
                {
                    return true;
                }
            }
            return false;

        }
    }

    public void testNumber(View view){
        numberEditText=(EditText) findViewById(R.id.numberEditText);

        if(numberEditText.getText().toString().isEmpty()||Integer.parseInt(numberEditText.getText().toString())<0){
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
        }
        else{
            int num=Integer.parseInt(numberEditText.getText().toString());

            NumberShape numberShape=new NumberShape(num);

            String message;
            if(numberShape.isTriangular()&&numberShape.isSquare()){
                message=" is both a triangular and square number.";
            }else if(numberShape.isTriangular()){
                message=" is a triangular number.";
            }else if(numberShape.isSquare()){
                message=" is a square number.";
            }else{
                message=" is neither triangular nor square";
            }

            Toast.makeText(this, numberShape.number+message, Toast.LENGTH_SHORT).show();

        }
        clear();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
