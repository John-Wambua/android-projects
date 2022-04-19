package com.wambuacooperations.downloadingimages;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    public void downloadImage(View view){
        ImageDownloader imageDownloader=new ImageDownloader();
        Bitmap myImage;

        try {
            myImage=imageDownloader.execute("https://static.simpsonswiki.com/images/thumb/6/65/Bart_Simpson.png/200px-Bart_Simpson.png").get();
            imageView.setImageBitmap(myImage);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
    }

        public class ImageDownloader extends AsyncTask<String, Void, Bitmap>{

            @Override
            protected Bitmap doInBackground(String... urls) { //Returns an image : Bitmap
                try {
                    URL url =new URL(urls[0]);
                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();

                    InputStream inputStream=connection.getInputStream();

                    Bitmap bitmap= BitmapFactory.decodeStream(inputStream);

                    return  bitmap;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            };
        }
}
