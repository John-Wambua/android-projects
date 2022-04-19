package com.wambuacooperations.snapchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class CreateSnapActivity extends AppCompatActivity {

    ImageView createSnapImageView;
    EditText messageEditText;
    String imageName= UUID.randomUUID().toString() +".jpg"; /**Create a unique name for the images*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_snap);
        createSnapImageView=findViewById(R.id.createSnapImageView);
        messageEditText=findViewById(R.id.messageEditText);

    }

    public void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }
    public void chooseImageClicked(View view){
        //ADD A PHOTO INTO THE APP

        //Request for permission if it is not yet granted
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            getPhoto();
        }
    }

    //When an image is selected
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImage=data.getData();

        if(requestCode==1&&resultCode==RESULT_OK && data!=null){
            try{
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                createSnapImageView.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //Check if permission was granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto();
            }
        }
    }
    public void nextClicked(View view){
        /**Uploading the photo to firebase storage**/

        // Get the data from the ImageView as bytes
        createSnapImageView.setDrawingCacheEnabled(true);
        createSnapImageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) createSnapImageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        //CREATE A FIREBASE STORAGE FOLDER WHERE ALL IMAGES ARE STORED

        final UploadTask uploadTask =  FirebaseStorage.getInstance().getReference().child("images").child(imageName).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(CreateSnapActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                exception.printStackTrace();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.

                FirebaseStorage.getInstance().getReference().child("images").child(imageName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                       Log.i("URL",uri.toString());
                        Intent intent=new Intent(getApplicationContext(),SelectUserActivity.class);
                        intent.putExtra("imageURL",uri.toString());
                        intent.putExtra("imageName",imageName);
                        intent.putExtra("message",messageEditText.getText().toString());
                        startActivity(intent);
                    }
                });
               //Log.i("URL",FirebaseStorage.getInstance().getReference().child("images").child(imageName).getDownloadUrl().toString());

            }
        });

    }
}
