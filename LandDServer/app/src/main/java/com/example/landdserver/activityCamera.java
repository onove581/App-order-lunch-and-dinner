package com.example.landdserver;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class activityCamera extends AppCompatActivity {
    public Button ok;
    public ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        img=findViewById(R.id.imgcamera);
        ok=findViewById(R.id.btncamera);
        if (ContextCompat.checkSelfPermission(activityCamera.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activityCamera.this,
                    new String[]

                            {Manifest.permission.CAMERA},100);
        }
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,100);
            }
        });
Button back=findViewById(R.id.btnbackhome);
back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent kd=new Intent(activityCamera.this,Home.class);
        startActivity(kd);
    }
});

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,@Nullable Intent data) {
        if (requestCode==100) {
            Bitmap csds = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(csds);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}