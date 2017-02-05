package com.example.richard.prescript;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.googlecode.tesseract.android.TessBaseAPI;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.widget.TextView;

public class CameraVerify extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String prescription = "";
    Button takePictureButton;
    Button deleteButton;
    ImageView imageView;
    private TessBaseAPI mTess; //Tess API reference
    String datapath = ""; //path to folder containing language data file
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_verify);

        //mCamera = null;
        //mCameraView = null;
        //file = Uri.parse("");
        takePictureButton = (Button) findViewById(R.id.Button);
        deleteButton = (Button) findViewById(R.id.Button1);
        deleteButton.setEnabled(false);
        imageView = (ImageView) findViewById(R.id.ImageView);

        datapath = getFilesDir()+ "/tesseract/";

        checkFile(new File(datapath + "tessdata/"));

        //initialize Tesseract API
        String lang = "eng";
        mTess = new TessBaseAPI();
        mTess.init(datapath, lang);
        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }*/
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
        //TESTING
        TextView OCRTextView = (TextView) findViewById(R.id.OCRTextView);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            String OCRresult = null;
            mTess.setImage(imageBitmap);
            OCRresult = mTess.getUTF8Text();
            prescription = OCRresult;
            //TESTING
            //OCRTextView.setText(OCRresult);
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setEnabled(true);
        }
        if (prescription.equals("")) {
            takePictureButton.setText("Take Picture");
            //TESTING
            OCRTextView.setText("UNSUCCESSFUL");
        }

        System.out.println("F");

    }


    public void TakePicture(View view) {
        if (takePictureButton.getText().equals("Take Picture")) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
            takePictureButton.setText("Confirm");
        }
        else {
            takePictureButton.setText("Take Picture");
            Intent sendPictureIntent = new Intent(this, CameraDisplay.class);
            sendPictureIntent.putExtra("prescription", prescription);
            startActivity(sendPictureIntent);
        }
    }


    public void DeletePicture(View view) {
        //TESTING(2)
        TextView OCRTextView = (TextView) findViewById(R.id.OCRTextView);
        OCRTextView.setText("");
        imageView.setImageBitmap(null);
        deleteButton.setVisibility(View.INVISIBLE);
        deleteButton.setEnabled(false);
        takePictureButton.setText("Take Picture");
    }

    public void GoHome(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void copyFiles() {
        try {
            //location we want the file to be at
            String filepath = datapath + "/tessdata/eng.traineddata";

            //get access to AssetManager
            AssetManager assetManager = getAssets();

            //open byte streams for reading/writing
            InputStream instream = assetManager.open("tessdata/eng.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);

            //copy the file to the location specified by filepath
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkFile(File dir) {
        //directory does not exist, but we can successfully create it
        if (!dir.exists()&& dir.mkdirs()){
            copyFiles();
        }
        //The directory exists, but there is no data file in it
        if(dir.exists()) {
            String datafilepath = datapath+ "/tessdata/eng.traineddata";
            File datafile = new File(datafilepath);
            if (!datafile.exists()) {
                copyFiles();
            }
        }
    }

    public boolean hasPermissionInManifest (Context context, String permissionName) {
        return false;
    }
}







