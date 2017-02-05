package com.example.richard.prescript;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;


public class CameraDisplay extends AppCompatActivity {

    String s;
    TextView textView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_display);

        s = "";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            s = extras.getString("prescription");
        }

        s = "RX: 9913781\nTake 1 capsule three times daily for fourteen days\nDesloratadine 200 MG";

        //setContentView(R.layout.activity_camera_display);
        textView = (TextView) findViewById(R.id.verify_message);
        textView.setText(s);
    }

    protected String parseDose(String s) {
        String[] words = s.split("\\s+");    // splits s into it's words
        String dosage = "0"; //default dosage
        for (int i = 0; i < words.length; i++) {
            if (words[i] == "Take" || words[i] == "take") { // if matches TAKE

                dosage = words[i];
            }
        }
        return dosage;
    }

    protected String parseTimes(String s) {
        String[] words = s.split("\\s+");    // splits s into it's words
        String times = "0"; //default dosage
        for (int i = 0; i < words.length; i++) {
            if (words[i] == "ti" || words[i] == "time") { // if matches TAKE

                times = words[i - 1] + " " + words[i];
            }
        }
        return times;
    }

    protected String parseDuration(String s) {
        String[] words = s.split("\\s+");    // splits s into it's words
        String duration = "0"; //default dosage
        for (int i = 0; i < words.length; i++) {
            if (words[i] == "da" || words[i] == "day") { // if matches TAKE

                duration = words[i - 1] + " " + words[i];
            }
        }
        return duration;
    }

    protected String parseName(String s) {
        String[] words = s.split("\\s+");    // splits s into it's words
        String name = "0"; //default dosage
        for (int i = 0; i < words.length; i++) {
            if (words[i] == "MG") { // if matches TAKE

                name = words[i - 2] + " " + words[i - 1] + " " + words[i];
            }
        }
        return name;
    }

    protected String fullString(String s) {
        String dose = parseDose(s);
        String times = parseTimes(s);
        String duration = parseDuration(s);
        String name = parseName(s);

        String full = dose + " " + name + " " + times + " " + duration + "\n";

        return full;

    }

    public void delete_photo (View view) {
        textView.setText("");
    }

    public void cancel_photo (View view) {
        Intent sendPictureIntent = new Intent(this, CameraVerify.class);
        startActivity(sendPictureIntent);
    }

    public void confirm_photo (View view) {
        //Save to SQL Database
    }

    public String[] threeStrings (String s){
                String[] myStrings ={parseName(s),parseDose(s),parseTimes(s) +" " + parseDuration(s)};

    }

   /* public File TempFile(Context context, String url) {
        File file = new File(context.getFilesDir(), filename);


    }*/

}


