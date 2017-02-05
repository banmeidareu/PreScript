package com.example.richard.prescript;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class CameraVerify extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Button takePictureButton;
    Button confirmPictureButton;
    ImageView imageView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_verify);
        System.out.println("B");

        //mCamera = null;
        //mCameraView = null;
        //file = Uri.parse("");
        takePictureButton = (Button) findViewById(R.id.Button);
        confirmPictureButton = (Button) findViewById(R.id.confirmButton);
       // confirmPictureButton.setEnabled(false);
       // confirmPictureButton.setVisibility(View.INVISIBLE);
        imageView = (ImageView) findViewById(R.id.ImageView);

        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }*/
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        System.out.println("C");
    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePictureButton.setEnabled(true);
            }
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("E");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }

        confirmPictureButton.setEnabled(true);
        confirmPictureButton.setVisibility(View.VISIBLE);

        //sendPicture(view);
        System.out.println("F");

    }

    public void TakePicture(View view) {
        System.out.println("A");
        takePictureButton.setEnabled(false);
        takePictureButton.setVisibility(View.INVISIBLE);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }
        System.out.println("D");

    }

    public void sendPicture(View view) {
        System.out.println("G");
        Intent sendPictureIntent = new Intent(this, CameraDisplay.class);
        startActivity(sendPictureIntent);
        System.out.println("H");
    }


}

