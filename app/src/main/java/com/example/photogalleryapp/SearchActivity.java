package com.example.photogalleryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView startTime = findViewById(R.id.starttime);
        TextView startTime_val = findViewById(R.id.starttimeVal);
        TextView endTime = findViewById(R.id.endtime);
        TextView endTime_val = findViewById(R.id.endtimeVal);
        TextView keyword = findViewById(R.id.Keyword);

        Button cancel = (Button)findViewById(R.id.Cancel);
        Button ok = (Button)findViewById(R.id.OK);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


}