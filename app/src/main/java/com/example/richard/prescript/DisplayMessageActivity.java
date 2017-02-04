package com.example.richard.prescript;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        String message = intent.getStringExtra (CameraDisplay.EXTRA_MESSAGE);
        TextView textview = new TextView (this);
        textview.setTextSize(40);
        textview.setText (message);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_message);
        layout.addView (textview);
    }
}
