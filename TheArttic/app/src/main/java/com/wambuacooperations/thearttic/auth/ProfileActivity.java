package com.wambuacooperations.thearttic.auth;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.wambuacooperations.thearttic.R;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    ImageView profileImage;
    EditText profileUserName;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    String user_id;

    private final static int GALLERY_REQ​= 1;
    static final int REQUEST_IMAGE_GET = 1;
    String imageName= UUID.randomUUID().toString() +".jpg"; /**Create a unique name for the images*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        profileImage=findViewById(R.id.imagebutton);
        profileUserName=findViewById(R.id.profUserName);
        mAuth=FirebaseAuth.getInstance();

        user_id=mAuth.getCurrentUser().getUid();
    }

    public void getPhoto(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void imageClicked(View view){
        /**Get the image from phone storage**/
        //Request for permission if it is not yet granted
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            getPhoto();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImage=data.getData();
        if(requestCode==1&&resultCode==RESULT_OK && data!=null){
            try{
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                profileImage.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //Check if permission was granted.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto();
            }
        }
    }

    public void uploadImage(View view){
        /**Uploading the photo to firebase storage**/

        final String name=profileUserName.getText().toString().trim();
        if (TextUtils.isEmpty(name)){
            new AlertDialog.Builder(this)
                    .setMessage("Please the field to continue")
                    .show();
        }else {
            profileImage.setDrawingCacheEnabled(true);
            profileImage.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child("profile_images").child(imageName).putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(ProfileActivity.this, "Upload Failed!", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if(taskSnapshot.getMetadata()!=null){
                        if (taskSnapshot.getMetadata().getReference()!=null){
                            Task<Uri> result=taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    final String profileImage=uri.toString();
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).push();
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("displayName").setValue(name);
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("profilePhoto").setValue(profileImage).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(ProfileActivity.this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                                                        Intent login=new Intent(ProfileActivity.this, LoginActivity.class);
                                                        startActivity(login);
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
                    }
                }
            });
        }

    }
}
