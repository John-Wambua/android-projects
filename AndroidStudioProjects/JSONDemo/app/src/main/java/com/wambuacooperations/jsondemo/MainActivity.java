package com.wambuacooperations.jsondemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            String results="";
            URL url;
            HttpURLConnection connection=null;

            try {
                url=new URL(urls[0]);
                connection= (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in=connection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1){
                    char current= (char) data;

                    results+=current;

                    data=reader.read();
                }
                return results;

            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
        }

        //Do something after doInBackground has finished running
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject=new JSONObject(s);

                String weatherInfo=jsonObject.getString("weather");

                Log.i("WEATHER INFO>>>",weatherInfo);

                JSONArray array=new JSONArray(weatherInfo);

                for(int i=0;i<array.length();i++){
                    JSONObject jsonPart=array.getJSONObject(i);
                    Log.i("main",jsonPart.getString("main"));
                    Log.i("description",jsonPart.getString("description"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask task=new DownloadTask();
       // String result=null;

        task.execute("https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22");

       // Log.i("Results>>>>>",result);
    }
}
