package com.wambuacooperations.guessthecelebrity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ImageView celebrityImageView;
    Button button0,button1,button2,button3;
    ArrayList<String> cityUrls=new ArrayList<String>();
    ArrayList<String> cityNames=new ArrayList<String>();



    public class CelebrityImageDownload extends AsyncTask<String,Void, Bitmap>{


        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url=new URL(urls[0]);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream=connection.getInputStream();
                Bitmap myBitmap=BitmapFactory.decodeStream(inputStream);

                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    public class DownloadTask extends AsyncTask<String ,Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            String result=null;
            URL url;
            HttpURLConnection connection=null;
            try {
                url=new URL(urls[0]);
                connection= (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream=connection.getInputStream();
                InputStreamReader reader=new InputStreamReader(inputStream);
                int data=reader.read();
                while(data!=-1){
                    char current= (char) data;
                    result+=current;
                    data=reader.read();
                }
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return "Failed";
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        celebrityImageView = findViewById(R.id.celebrityImageView);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);


        DownloadTask downloadTask=new DownloadTask();
        String result=null;

        try {
            result=downloadTask.execute("https://www.harpersbazaar.com/culture/travel-dining/g12244524/most-beautiful-city-in-world/").get();


            //Toast.makeText(this, "Cool", Toast.LENGTH_SHORT).show();
              String []splitResult=result.split("class=\"listicle-intro\"");

            //Log.i("result>>>>>>",splitResult[1]);
            Pattern pattern=Pattern.compile("slide-hed-text\">(.*?)<");
            Matcher matcher=pattern.matcher(splitResult[1]);

            while(matcher.find()){
                cityNames.add(matcher.group(1));
            }
            System.out.println(cityNames.toString());

            String [] splitsplitResult=splitResult[1].split("<div class=\"authors \">");
            pattern=Pattern.compile("srcset=\"(.*?)\"");
            matcher=pattern.matcher(splitsplitResult[0]);

            while(matcher.find()){
                cityUrls.add(matcher.group(1));
            }

            for(int i=0;i<cityUrls.size();i+=2){
                cityUrls.get(i);
            }
            System.out.println(cityUrls.toString());

           } catch (Exception e) {
            e.printStackTrace();
        }


//        CelebrityImageDownload celebrityImageDownload=new CelebrityImageDownload();
//        Bitmap celebrityImage;
//            try {
//                celebrityImage=celebrityImageDownload.execute("http://cdn.posh24.se/images/:profile/06195e4959053517d41b128915fcf5cb4").get();
//                celebrityImageView.setImageBitmap(celebrityImage);
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
//            }

    }

}
