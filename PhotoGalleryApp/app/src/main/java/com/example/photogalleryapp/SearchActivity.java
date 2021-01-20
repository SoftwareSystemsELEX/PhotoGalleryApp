package com.example.photogalleryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    android.widget.Button cancel_button;
    android.widget.Button okay_button;
    EditText caption_input;
    EditText time_input;
    public String captionSearch = "";
    public String timeSearch = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        cancel_button = (android.widget.Button) findViewById(R.id.cancelButton);
        okay_button = (android.widget.Button) findViewById(R.id.okayButton);
        caption_input = (EditText) findViewById(R.id.captionInput);
        time_input = (EditText) findViewById(R.id.timeInput);

        caption_input.setText("");
        time_input.setText("");
        caption_input.setHint("Enter Caption");
        time_input.setHint("Enter Time");

        cancel_button.setOnClickListener(new View.OnClickListener() {
                                             public void onClick(View v) {
                                                 finish();
                                             }
                                         }
        );


        okay_button.setOnClickListener(new View.OnClickListener() {
                                           public void onClick(View v) {
                                               String tempCaption = caption_input.getText().toString();
                                               String tempTime = time_input.getText().toString();
                                               if(tempCaption.isEmpty() == false){
                                                   captionSearch = tempCaption;
                                               }
                                               if(tempTime.isEmpty() == false){
                                                   timeSearch = tempTime;
                                               }
                                               Intent intent = new Intent();
                                               intent.putExtra("CAPTION",captionSearch);
                                               intent.putExtra("TIME", timeSearch);
                                               setResult(RESULT_OK, intent);
                                               finish();
                                           }
                                       }
        );

    }

}

