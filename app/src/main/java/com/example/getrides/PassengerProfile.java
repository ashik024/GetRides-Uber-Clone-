package com.example.getrides;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PassengerProfile extends AppCompatActivity {

    EditText NameP;
    EditText PhoneP;
    EditText EmailP;

    Button SaveP;

    FirebaseAuth auth;
    DatabaseReference pasDatabase;
    String pasId;

    String pasName;
    String pasPhone;
    String pasEmail;
    String pasImg;
    Uri pasimglink;

    String updatedname;
    String updatedphone;
    String updatedemail;

    Uri imageuri;


    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_profile);

        NameP = findViewById(R.id.nameP);
        PhoneP= findViewById(R.id.phoneP);
        EmailP= findViewById(R.id.emailP);
        imageView= findViewById(R.id.profile);

        SaveP= findViewById(R.id.saveP);

        auth =FirebaseAuth.getInstance();

        pasId=auth.getCurrentUser().getUid();
        pasDatabase= FirebaseDatabase.getInstance().getReference().child("user").child("passenger").child(pasId);

        getInfo();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
                
            }
        });

        SaveP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateinfo();





            }
        });
    }

    private void getInfo() {

         pasDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    Map<String,Object> map = (Map<String, Object>) snapshot.getValue();
                    if (map.get("Pname")!=null){
                        pasName=map.get("Pname").toString();
                        NameP.setText(pasName);
                    }
                    if (map.get("Pphone")!=null){
                        pasPhone=map.get("Pphone").toString();
                        PhoneP.setText(pasPhone);
                    }
                    if (map.get("Pemail")!=null){
                        pasEmail=map.get("Pemail").toString();
                        EmailP.setText(pasEmail);
                    }
                    if (map.get("image")!=null){
                        pasImg=map.get("image").toString();

//                        Uri uri= (Uri) map.get("image");
                        Glide.with(getApplicationContext()).load(pasImg).into(imageView);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void updateinfo() {

        updatedname = NameP.getText().toString();
        updatedphone= PhoneP.getText().toString();
        updatedemail= EmailP.getText().toString();


        Map PassengerUpdatedInfo = new HashMap();

        PassengerUpdatedInfo.put("Pname",updatedname);
        PassengerUpdatedInfo.put("Pphone",updatedphone);
        PassengerUpdatedInfo.put("Pemail",updatedemail);

        pasDatabase.updateChildren(PassengerUpdatedInfo).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(PassengerProfile.this, "Updated", Toast.LENGTH_SHORT).show();
            }
        });

        if (imageuri!=null){

            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("PassengerImages").child(pasId);
            Bitmap bitmap= null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),imageuri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG,20,byteArrayOutputStream);
            byte[] data= byteArrayOutputStream.toByteArray();
            UploadTask uploadTask = storageReference.putBytes(data);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PassengerProfile.this, "error"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });



            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    if (!taskSnapshot.equals(null)){

                        Task<Uri> imageuri =taskSnapshot.getMetadata().getReference().getDownloadUrl();

//                        Uri uri = taskSnapshot.getMetadata().getReference().getDownloadUrl().getResult();
//
//                        Map image = new HashMap();
//                        image.put("image",imageuri.toString());
//                        pasDatabase.updateChildren(image);
//                        Toast.makeText(PassengerProfile.this, "Image Uploaded", Toast.LENGTH_SHORT).show();

                        imageuri.addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {

                                if (task.isSuccessful()){

                                     pasimglink = task.getResult();
                                    Map image = new HashMap();
                                    image.put("image",pasimglink.toString());
                                    pasDatabase.updateChildren(image);
                                    Toast.makeText(PassengerProfile.this, "Image Uploaded", Toast.LENGTH_SHORT).show();


                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(PassengerProfile.this, "Uploading Failed", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }else{
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1 && resultCode== Activity.RESULT_OK)  {

            final Uri finalimageUri = data.getData();
            imageuri = finalimageUri;

            imageView.setImageURI(imageuri);


        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(PassengerProfile.this,PassengerMap.class);
//        finish();
//        overridePendingTransition(0, 0);
//        startActivity(intent);
//        overridePendingTransition(0, 0);
//
//
//    }
}