package com.wambuacooperations.thearttic.posts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.wambuacooperations.thearttic.R;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class PostActivity extends AppCompatActivity {

    EditText postTitle,postDescription;
    ImageView imageBtn;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    static final int REQUEST_IMAGE_GET = 2;
    private Uri uri=null;
    String imageName= UUID.randomUUID().toString() +".jpg"; /**Create a unique name for the images*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        postTitle=findViewById(R.id.textTitle);
        postDescription=findViewById(R.id.textDesc);
        imageBtn=findViewById(R.id.imgBtn);
        mAuth=FirebaseAuth.getInstance();
        mCurrentUser=mAuth.getCurrentUser();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addPostImage(View view){
        /**Get the image from phone storage**/
        //Request for permission if it is not yet granted
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        } else {
            getPhoto();
        }


    }
    public void getPhoto(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }
    public void postContent(View view){
        Toast.makeText(this, "POSTING....", Toast.LENGTH_LONG).show();
        final String title=postTitle.getText().toString();
        final String description=postDescription.getText().toString();

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("dd-MM-yyyy");
        final String saveCurrentDate=currentDate.format(calendar.getTime());

        Calendar calendar1=Calendar.getInstance();
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm");
        final String saveCurrentTime=currentTime.format(calendar1.getTime());


        if (TextUtils.isEmpty(title)||TextUtils.isEmpty(description)){
            new AlertDialog.Builder(this)
                    .setMessage("Post is incomplete")
                    .show();
        }else{
            imageBtn.setDrawingCacheEnabled(true);
            imageBtn.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imageBtn.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child("post_images").child(imageName).putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, "Image could not be uploaded!", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> result=taskSnapshot.getStorage().getDownloadUrl();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String imageUrl=uri.toString();
                            Toast.makeText(PostActivity.this, "Successfully uploaded!", Toast.LENGTH_SHORT).show();
                            final DatabaseReference newPost= FirebaseDatabase.getInstance().getReference().child("Posts").push();
                            FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid())
                                    .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                   newPost.child("title").setValue(title);
                                   newPost.child("desc").setValue(description);
                                   newPost.child("postImage").setValue(imageUrl);
                                   newPost.child("uid").setValue(mCurrentUser.getUid());
                                   newPost.child("time").setValue(saveCurrentTime);
                                   newPost.child("date").setValue(saveCurrentDate);
                                   newPost.child("profilePhoto").setValue(snapshot.child("profilePhoto").getValue());
                                   newPost.child("displayName").setValue(snapshot.child("displayName").getValue())
                                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {
                                                   if (task.isSuccessful()){
                                                       Intent intent=new Intent(PostActivity.this, MainActivity.class);
                                                       startActivity(intent);
                                                       finish();
                                                   }else{
                                                       Toast.makeText(PostActivity.this, "Post upload failed!", Toast.LENGTH_SHORT).show();
                                                   }
                                               }
                                           });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                }
            });


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImage=data.getData();
        if(requestCode==2&&resultCode==RESULT_OK && data!=null){
            try{
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                imageBtn.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //Check if permission was granted.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto();
            }
        }
    }

}
