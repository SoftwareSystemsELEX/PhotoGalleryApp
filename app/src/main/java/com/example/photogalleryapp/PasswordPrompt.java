package com.example.photogalleryapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import androidx.appcompat.app.AppCompatActivity;

public class PasswordPrompt extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.PhotoGalleryApp.MESSAGE";
    String  encryted_password = null;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.password);
        Button sumbitButton = (Button) findViewById(R.id.submit);

        sumbitButton.setOnClickListener((View.OnClickListener) v -> {
            EditText passwordEditText = (EditText) findViewById(R.id.editTextPassword);
            try {
               encryted_password =  toHexString(getSHA(passwordEditText.getText().toString()));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            if(encryted_password.equals("b6ac44e3f415a3a8475647092d3ea45fda2c4480219b7a660cb67a32dcf0882d")){
                Intent intent = new Intent(PasswordPrompt.this,MainActivity.class);
                startActivity(intent);

            }
        });
    }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public static byte[] getSHA(String input) throws NoSuchAlgorithmException
        {
            // Static getInstance method is called with hashing SHA
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // digest() method called
            // to calculate message digest of an input
            // and return array of byte
            return md.digest(input.getBytes(StandardCharsets.UTF_8));
        }

        public static String toHexString(byte[] hash)
        {
            // Convert byte array into signum representation
            BigInteger number = new BigInteger(1, hash);

            // Convert message digest into hex value
            StringBuilder hexString = new StringBuilder(number.toString(16));

            // Pad with leading zeros
            while (hexString.length() < 32)
            {
                hexString.insert(0, '0');
            }
            return hexString.toString();
        }



























}