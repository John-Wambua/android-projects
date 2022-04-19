package com.wambuacooperations.droidcafe;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String message="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),OrderActivity.class);
                startActivity(intent);
            }
        });
    }

    public void itemClicked(View view){
        String tag= (String) view.getTag();

        message="You have ordered "+tag+"!";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        moveToOrder();

    }
    public void moveToOrder(){
        if(message==""||message==null) {
            message = "No item added to cart";
        }

            Intent intent = new Intent(MainActivity.this, OrderActivity.class);

            intent.putExtra("orderItem", message);
            startActivity(intent);
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()){
           case R.id.order:
               moveToOrder();
               break;
           case R.id.callUs:
               Intent intent =new Intent(Intent.ACTION_DIAL);
               intent.setData(Uri.parse("tel:+254702663063"));
               if(intent.resolveActivity(getPackageManager())!=null){
                   startActivity(intent);
               }
               break;
           case R.id.aboutUS:
               Intent searchIntent=new Intent(Intent.ACTION_WEB_SEARCH);
               searchIntent.putExtra(SearchManager.QUERY,"http://www.artcaffe.co.ke/");
               if(searchIntent.resolveActivity(getPackageManager())!=null){
                   startActivity(searchIntent);
               }
               break;
           case R.id.locateUs:

               Intent locationIntent = new Intent(android.content.Intent.ACTION_VIEW,
                       Uri.parse("http://maps.google.com/maps?daddr=-1.342999,36.765368"));
               startActivity(locationIntent);
       }


        return super.onOptionsItemSelected(item);
    }
}
