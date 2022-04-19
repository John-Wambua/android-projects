package com.wambuacooperations.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText amountEditText;

    public void convertToDollars(View view){
        amountEditText=(EditText) findViewById(R.id.amountEditText);
        double amount=Double.parseDouble(amountEditText.getText().toString());
        double newAmount= amount*0.0099;
        String finalAmount=String.format("%.2f", newAmount);

        Toast.makeText(this, "KSH "+amount+" is $"+finalAmount, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
