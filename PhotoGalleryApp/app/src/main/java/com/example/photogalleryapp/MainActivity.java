package com.example.photogalleryapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    android.widget.Button filter_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filter_button = (android.widget.Button) findViewById(R.id.filterbutton);

        filter_button.setOnClickListener(new View.OnClickListener() {
                                             public void onClick(View v) {
                                                 Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                                                 startActivity(intent);
                                             }
                                         }
        );
    }
}