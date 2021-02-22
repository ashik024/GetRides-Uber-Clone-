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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

public class DriverProfile extends AppCompatActivity {

    EditText NameD;
    EditText PhoneD;
    EditText EmailD;
    TextView CarnumD;
    Button SaveD;

    FirebaseAuth auth;
    DatabaseReference driDatabase;
    String driId;

    String driName;
    String driPhone;
    String driEmail;
    String driImg;
    String carnum;
    Uri driimglink;

    String updatednameD;
    String updatedphoneD;
    String updatedemailD;

    Uri imageuriD;


    ImageView imageViewD;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);

        NameD = findViewById(R.id.nameD);
        PhoneD= findViewById(R.id.phoneD);
        EmailD= findViewById(R.id.emaiD);
        CarnumD = findViewById(R.id.carnumD);
        imageViewD= findViewById(R.id.profileD);

        SaveD= findViewById(R.id.saveD);



        auth =FirebaseAuth.getInstance();

        driId=auth.getCurrentUser().getUid();
        driDatabase= FirebaseDatabase.getInstance().getReference().child("user").child("driver").child(driId);

        getInfoD();

        imageViewD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);

            }
        });

        SaveD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateinfoD();





            }
        });
    }
    private void getInfoD() {

        driDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    Map<String,Object> map = (Map<String, Object>) snapshot.getValue();
                    if (map.get("Dname")!=null){
                        driName=map.get("Dname").toString();
                        NameD.setText(driName);
                    }
                    if (map.get("Dphone")!=null){
                        driPhone=map.get("Dphone").toString();
                        PhoneD.setText(driPhone);
                    }
                    if (map.get("Demail")!=null){
                        driEmail=map.get("Demail").toString();
                        EmailD.setText(driEmail);
                    }
                    if (map.get("DcarNumber")!=null){
                        carnum=map.get("DcarNumber").toString();
                        CarnumD.setText(carnum);
                    }
                    if (map.get("image")!=null){
                        driImg=map.get("image").toString();

//                        Uri uri= (Uri) map.get("image");
                        Glide.with(getApplicationContext()).load(driImg).into(imageViewD);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void updateinfoD() {

        updatednameD = NameD.getText().toString();
        updatedphoneD= PhoneD.getText().toString();
        updatedemailD= EmailD.getText().toString();


        Map driverUpdatedInfo = new HashMap();

        driverUpdatedInfo.put("Dname",updatednameD);
        driverUpdatedInfo.put("Dphone",updatedphoneD);
        driverUpdatedInfo.put("Demail",updatedemailD);

        driDatabase.updateChildren(driverUpdatedInfo).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(DriverProfile.this, "Updated", Toast.LENGTH_SHORT).show();
            }
        });

        if (imageuriD!=null){

            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("DriverImages").child(driId);
            Bitmap bitmap= null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),imageuriD);
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
                    Toast.makeText(DriverProfile.this, "error"+ e.getMessage(), Toast.LENGTH_SHORT).show();
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

                                    driimglink = task.getResult();
                                    Map image = new HashMap();
                                    image.put("image",driimglink.toString());
                                    driDatabase.updateChildren(image);
                                    Toast.makeText(DriverProfile.this, "Image Uploaded", Toast.LENGTH_SHORT).show();


                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(DriverProfile.this, "Uploading Failed", Toast.LENGTH_SHORT).show();
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
            imageuriD = finalimageUri;

            imageViewD.setImageURI(imageuriD);


        }
    }

}