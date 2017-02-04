package com.example.richard.prescript;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void addPerscription(View view) {
        System.out.println("hello");
    }

    public void openCamera (View view) {
        Intent intent = new Intent(this, CameraDisplay.class);
        startActivity(intent);
    }
}


