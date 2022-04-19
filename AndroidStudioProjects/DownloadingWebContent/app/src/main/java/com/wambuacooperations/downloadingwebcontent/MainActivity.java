package com.wambuacooperations.downloadingwebcontent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public class DoInBackground extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {

            String result="";
            URL url;
            HttpURLConnection urlConnection=null;   //Like a browser

            try {
                url=new URL(urls[0]); // Converting our string to a url object
                //Create a URL connection
                urlConnection= (HttpURLConnection) url.openConnection();
                //create an input stream to gather the data as it is coming in
                InputStream inputStream=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(inputStream); // to read from the input stream
                int data=reader.read();

                while(data!=-1){
                    char current= (char) data;

                    result+=current;

                    data=reader.read();
                }

                return  result;


            } catch (Exception e) {// Because not all strings can be turned into an url
                e.printStackTrace();
                return "failed";
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DoInBackground task=new DoInBackground();
        String result =null;
        try {
            result=task.execute("https://zappycode.com/").get();
        } catch (Exception e){
            e.printStackTrace();
        }
        Log.i("result",result);
    }
}
