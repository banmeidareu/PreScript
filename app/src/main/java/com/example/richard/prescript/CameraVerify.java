package com.example.richard.prescript;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.widget.TextView;
import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CameraVerify extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int CAMERA_REQUEST = 101010;
    public final String err = CameraVerify.class.getSimpleName();
    private static final String AzureKey = "f89325aedcd340469b2f62bfeb9cb582";
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

    private class networkOCRTask extends AsyncTask<Bitmap, Integer, HttpResponse> {

        protected HttpResponse doInBackground (Bitmap... imageBitmap) {
            HttpClient httpclient = HttpClients.createDefault();

            try {
                URIBuilder builder = new URIBuilder("https://westus.api.cognitive.microsoft.com/vision/v1.0/ocr");

                builder.setParameter("language", "unk");
                builder.setParameter("detectOrientation ", "true");

                URI uri = builder.build();
                HttpPost request = new HttpPost(uri);
                request.setHeader("Content-Type", "application/octet-stream");
                request.setHeader("Ocp-Apim-Subscription-Key", AzureKey);

                ByteArrayOutputStream boas = new ByteArrayOutputStream();
                imageBitmap[0].compress(Bitmap.CompressFormat.PNG, 100, boas);
                byte[] imageBytes = boas.toByteArray();
                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                // Request body
                StringEntity reqEntity = new StringEntity(encodedImage);
                request.setEntity(new ByteArrayEntity(imageBytes));
                System.out.println("Before request");
                HttpResponse response = httpclient.execute(request);
                System.out.println("After request");
                HttpEntity entity = response.getEntity();
                System.out.println("AFter get Entity request");

                if (entity != null) {
                    System.out.println("REsponse");
                    //System.out.println(EntityUtils.toString(entity));
                    return response;
                }
            } catch (Exception e) {
                System.out.println("Exception occurred");
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute (HttpResponse response) {
            HttpEntity entity = response.getEntity();
            try {
                System.out.println ("POst execute");
                String jsonString = EntityUtils.toString(entity);
                System.out.println(jsonString);
                JSONObject jsonResult = new JSONObject(jsonString);
                JSONArray regions = jsonResult.getJSONArray("regions");
                if (regions != null) {
                    for (int i = 0; i < regions.length(); i++) {
                        JSONObject region = regions.getJSONObject(i);
                        JSONArray lines = region.getJSONArray("lines");
                        for (int j = 0; j < lines.length(); j++) {
                            JSONObject line = lines.getJSONObject(j);
                            JSONArray words = line.getJSONArray("words");
                            for (int k = 0; k < words.length(); k++) {
                                JSONObject word = words.getJSONObject(k);
                                String text = word.getString("text");
                                System.out.println (text);
                                prescription+= text + " ";
                            }
                        }
                    }
                    takePictureButton.setText("Confirm");
                    TextView OCRTextView = (TextView) findViewById(R.id.OCRTextView);
                    OCRTextView.setText (prescription);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TESTING
        TextView OCRTextView = (TextView) findViewById(R.id.OCRTextView);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            String OCRresult = null;



            new networkOCRTask().execute(imageBitmap);
            /*
            mTess.setImage(imageBitmap);
            OCRresult = mTess.getUTF8Text();
            prescription = OCRresult;
            */
            //OCRTextView.setText(OCRresult);
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setEnabled(true);
        }
        if (prescription.equals("")) {
            takePictureButton.setText("Take Picture");
            //TESTING
            //OCRTextView.setText("UNSUCCESSFUL");
        }

        System.out.println("F");

    }


    //@RequiresApi(api = Build.VERSION_CODES.M)
    public void TakePicture(View view) {
        if (takePictureButton.getText().equals("Take Picture")) {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //requestPermissions(new String[]{Manifest.permission.INTERNET}, CAMERA_REQUEST);
                System.out.println ("Checking permissions");
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    System.out.println ("requesting permissions");
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
                } else {
                    //Intent sendPictureIntent = new Intent(this, CameraDisplay.class);
                    //sendPictureIntent.putExtra("prescription", prescription);
                    //startActivity(sendPictureIntent);
                }
            //}*/
            System.out.println ("BEfore intent to take picture");

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
        final String packageName = context.getPackageName();
        try {
            final PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            final String[] declaredPermissions = packageInfo.requestedPermissions;
            if (declaredPermissions != null && declaredPermissions.length > 0) {
                for (String p : declaredPermissions) {
                    if (p.equals (permissionName)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions,
                                            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //can now use camera
                Log.d (err, "Got camera permissions");
            } else {
                // :( can't use camera
                Log.d (err, "Can't get camera permissions");
            }
        }
    }
}







