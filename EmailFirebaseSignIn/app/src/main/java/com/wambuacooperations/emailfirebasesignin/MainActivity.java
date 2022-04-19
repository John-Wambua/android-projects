package com.wambuacooperations.emailfirebasesignin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText firstName,lastName,email,password,confirmPassword;
    ImageView profileImage;
    String imageName= UUID.randomUUID().toString() +".jpg"; /**Create a unique name for the images*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileImage=findViewById(R.id.profile_image);
        firstName=findViewById(R.id.firstNameEditText);
        lastName=findViewById(R.id.lastNameEditText);
        email=findViewById(R.id.emailEditText);
        password=findViewById(R.id.passwordEditText);
        confirmPassword=findViewById(R.id.confirmPasswordEditText);
        mAuth=FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null){
            launchLanding();
        }

    }

    public void registerClicked(View view) {
        if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || firstName.getText().toString().isEmpty() || lastName.getText().toString().isEmpty()) {
            new AlertDialog.Builder(this)
                    .setMessage("Please Fill all the fields!")
                    .show();
        } else if (!(password.getText().toString().equals(confirmPassword.getText().toString()))) {
            new AlertDialog.Builder(this)
                    .setMessage("Passwords do not match")
                    .show();
        } else {
            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                //ADD USER TO THE DATABASE
                                FirebaseDatabase.getInstance().getReference().child("users").child(task.getResult().getUser().getUid()).child("firstName").setValue(firstName.getText().toString());
                                FirebaseDatabase.getInstance().getReference().child("users").child(task.getResult().getUser().getUid()).child("lastName").setValue(lastName.getText().toString());
                                FirebaseDatabase.getInstance().getReference().child("users").child(task.getResult().getUser().getUid()).child("email").setValue(email.getText().toString());

                                /**ADD THE IMAGE TO FIREBASE STORAGE*
                                 *(UPLOAD DATA FROM MEMORY: FIREBASE DOCS)
                                 */


                                // Get the data from the ImageView as bytes
                                profileImage.setDrawingCacheEnabled(true);
                                profileImage.buildDrawingCache();
                                Bitmap bitmap = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] data = baos.toByteArray();


                                UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child("images").child(imageName).putBytes(data);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle unsuccessful uploads
                                        Toast.makeText(MainActivity.this, "Profile image upload failed", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                        // ...
                                        Toast.makeText(MainActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                                        FirebaseStorage.getInstance().getReference().child("images").child(imageName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                               Log.i("imageURL",uri.toString());
                                                FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("imageURL").setValue(uri.toString());

                                                launchLogin();
                                            }
                                        });
                                    }
                                });

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }


    }


    public void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }


    //When an image is selected
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImage=data.getData();

        if(requestCode==1&&resultCode==RESULT_OK && data!=null){
            try{
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                profileImage.setImageBitmap(bitmap);
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
    public void profileImageClicked(View view){

        //ADD A PHOTO INTO THE APP

        //Request for permission if it is not yet granted
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            getPhoto();
        }
    }



    public void textClicked(View view){
        Intent intent =new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    public void launchLanding(){
        Intent intent =new Intent(this,LandingPageActivity.class);
        startActivity(intent);
    }
    public void launchLogin(){
        Intent intent =new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

}
