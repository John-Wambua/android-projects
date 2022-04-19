package com.wambuacooperations.droidcafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class OrderActivity extends AppCompatActivity {

    TextView orderedItemTextView;
    String radioMessage="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        orderedItemTextView=findViewById(R.id.orderedItemTextView);

        Intent intent=getIntent();
        String orderedItem=intent.getStringExtra("orderItem");
        orderedItemTextView.setText(orderedItem);
    }

    public void onRadioButtonClicked(View view){

        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()){
            case R.id.radio_sameDay:
                if(checked)
                    radioMessage=getString(R.string.same_day_messenger_service);
                break;

            case R.id.radio_nextDay:
                if(checked)
                    radioMessage=getString(R.string.next_day_ground_delivery);
                break;

            case R.id.radio_pickUp:
                if(checked)
                    radioMessage=getString(R.string.pick_up);
                break;
        }
        Toast.makeText(this, radioMessage, Toast.LENGTH_SHORT).show();
    }
}
