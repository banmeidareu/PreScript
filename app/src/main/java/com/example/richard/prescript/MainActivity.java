package com.example.richard.prescript;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.googlecode.tesseract.android.TessBaseAPI;
import java.io.File;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void addPrescription(View view) {
        Intent intent = new Intent (this, CameraVerify.class);
        startActivity (intent);
    }

    public void viewPrescriptions (View view) {
        Intent intent = new Intent (this, PrescriptionList.class);
        startActivity (intent);
    }

    public void viewCameraVerify (View view) {
        Intent intent = new Intent (this, CameraVerify.class);
        startActivity (intent);
    }


    public void viewMessageSubmit(View view) {
        Intent intent = new Intent(this, MessageSubmit.class);
        startActivity(intent);
    }
}


