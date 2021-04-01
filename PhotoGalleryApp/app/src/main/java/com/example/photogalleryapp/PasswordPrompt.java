package com.example.photogalleryapp;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class PasswordPrompt extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.PhotoGalleryApp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.password);
        Button sumbitButton = (Button) findViewById(R.id.submit);

        sumbitButton.setOnClickListener((View.OnClickListener) v -> {
            EditText passwordEditText = (EditText) findViewById(R.id.editTextPassword);
            if(passwordEditText.getText().toString().equals("sunday")){
                Intent intent = new Intent(PasswordPrompt.this,MainActivity.class);
                startActivity(intent);

            }
        });
    }
}