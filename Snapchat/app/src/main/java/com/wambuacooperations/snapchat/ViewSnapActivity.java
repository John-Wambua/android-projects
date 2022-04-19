package com.wambuacooperations.snapchat;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class ViewSnapActivity extends AppCompatActivity {

     FirebaseAuth mAuth;
    ImageView snapImageView;
    TextView snapTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_snap);
        snapImageView=findViewById(R.id.snapImageView);
        snapTextView=findViewById(R.id.snapTextView);

        snapTextView.setText(getIntent().getStringExtra("message"));

        ImageDownloader imageDownloader=new ImageDownloader();
        Bitmap myImage;

        try {
            myImage=imageDownloader.execute(getIntent().getStringExtra("imageURL")).get();
            snapImageView.setImageBitmap(myImage);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

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

    /**Delete Snap when user presses back button**/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Delete from Firebase DB
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("snaps").child(getIntent().getStringExtra("snapKey")).removeValue();

        //Delete from firebase storage
        FirebaseStorage.getInstance().getReference().child("images").child(getIntent().getStringExtra("imageName")).delete();
    }
}
