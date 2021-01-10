package com.example.photogalleryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SearchActivity extends AppCompatActivity {

    android.widget.Button cancel_button;
    android.widget.Button okay_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        cancel_button = (android.widget.Button) findViewById(R.id.cancelButton);

        cancel_button.setOnClickListener(new View.OnClickListener() {
                                             public void onClick(View v) {
                                                 Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                                                 startActivity(intent);
                                             }
                                         }
        );

        okay_button = (android.widget.Button) findViewById(R.id.okayButton);

        okay_button.setOnClickListener(new View.OnClickListener() {
                                           public void onClick(View v) {
                                               Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                                               startActivity(intent);
                                           }
                                       }
        );

    }
}