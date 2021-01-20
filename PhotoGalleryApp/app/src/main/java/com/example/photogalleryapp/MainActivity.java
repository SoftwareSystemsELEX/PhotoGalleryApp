package com.example.photogalleryapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import androidx.core.content.FileProvider;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Widget extensions
    android.widget.Button filter_button;
    android.widget.Button snap_button;
    android.widget.Button scroll_button1;
    android.widget.Button scroll_button2;
    ImageView captureScreen;
    EditText captionText;
    android.widget.Button editCaptionButton;

    //Private variables
    private ArrayList<String> photos = null;
    private int index = 0;
    private int inc = 0;
    private boolean imageDisplayed = false;

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int SEARCH_ACTIVITY_REQUEST = 2;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Retrieving data from other activities
        //Intent intent = getIntent();
        //String Caption = intent.getStringExtra("CAPTION");
        //String Time = intent.getStringExtra("TIME");
        Intent refresh = new Intent(MainActivity.this, MainActivity.class);

        filter_button = (Button) findViewById(R.id.filterbutton);
        snap_button = (Button) findViewById(R.id.snapButton);
        captureScreen = (ImageView) findViewById(R.id.imageView);
        scroll_button1 = (Button) findViewById(R.id.next);
        scroll_button2 = (Button) findViewById(R.id.prev);
        captionText = (EditText) findViewById(R.id.editCaption);
        editCaptionButton = (Button) findViewById(R.id.editCaptionButton);

        photos = findPhotos();
            if (photos.size() == 0) {
                displayPhoto(null);
            } else {
                displayPhoto(photos.get(index));
            }

        //Filter screen to search for a specific image
        filter_button.setOnClickListener(new View.OnClickListener() {
                                             public void onClick(View v) {
                                                 Intent filter = new Intent(MainActivity.this, SearchActivity.class);
                                                 startActivityForResult(filter, SEARCH_ACTIVITY_REQUEST);
                                             }
                                         }
        );

        snap_button.setOnClickListener(new View.OnClickListener() {
                                           public void onClick(View v) {
                                               Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                               File photoFile = null;
                                               try {
                                                   photoFile = createImageFile();
                                               } catch (IOException e) {
                                                   e.printStackTrace();
                                               }
                                               if(photoFile != null) {
                                                   Uri photoURI = FileProvider.getUriForFile(MainActivity.this,
                                                           "com.example.photogalleryapp.fileprovider",
                                                           photoFile);
                                                   takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                                   startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                               }
                                           }
                                       }
        );

        //scroll photos next
        scroll_button1.setOnClickListener(new View.OnClickListener() {
                                             public void onClick(View v) {
                                                 scrollPhotos(0);
                                             }
                                         }
        );

        //scroll photos previous
        scroll_button2.setOnClickListener(new View.OnClickListener() {
                                              public void onClick(View v) {
                                                  scrollPhotos(1);
                                              }
                                          }
        );

        //Edit the caption of the current photo
        editCaptionButton.setOnClickListener(new View.OnClickListener() {
                                              public void onClick(View v) {
                                                  if(inc == 0){
                                                      captionText.setText("");
                                                      captionText.setHint("Enter New Caption");
                                                      editCaptionButton.setText("Save Caption");
                                                      inc++;
                                                  }else if(inc == 1){
                                                      String caption = captionText.getText().toString();
                                                       String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                                       String imageFileName = "JPEG_" + timeStamp + "_" + caption + "_";
                                                       File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                                                       File from = new File(photos.get(index));
                                                       File to = new File(storageDir, imageFileName);
                                                       from.renameTo(to);
                                                       inc = 0;
                                                       editCaptionButton.setText("Edit Caption");
                                                       startActivity(refresh);
                                                  }


                                              }
                                          }
        );

    }

    private ArrayList<String> findPhotos() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.photogalleryapp/files/Pictures");
        ArrayList<String> photos = new ArrayList<String>();
        File[] fList = file.listFiles();
        if (fList != null) {
            for (File f : fList) {
                photos.add(f.getPath());
            }
        }
        return photos;
    }

    public void scrollPhotos(int option) {
        switch (option) {
            case 0:
                if (index > 0) {
                    index--;
                }
                break;
            case 1:
                if (index < (photos.size() - 1)) {
                    index++;
                }
            break;
            default:
                break;
        }
        displayPhoto(photos.get(index));
    }

    private void displayPhoto(String path) {
        ImageView iv = (ImageView) findViewById(R.id.imageView);
        TextView tv = (TextView) findViewById(R.id.timeDisplay);
        EditText et = (EditText) findViewById(R.id.editCaption);
        if (path == null || path =="") {
            iv.setImageResource(R.mipmap.ic_launcher);
            et.setText("");
            tv.setText("");
        } else {
            iv.setImageBitmap(BitmapFactory.decodeFile(path));
            String[] attr = path.split("_");
            et.setText(attr[3]);
            tv.setText(attr[1] + "_" + attr[2]);
        }
    }


    private File createImageFile() throws IOException{
        String caption = captionText.getText().toString();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_" + caption + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            captureScreen.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
            imageDisplayed = true;
        }else if(requestCode == SEARCH_ACTIVITY_REQUEST && resultCode == RESULT_OK){
            //Intent intent = getIntent();
            String Caption = data.getStringExtra("CAPTION");
            String Time = data.getStringExtra("TIME");
            if(Caption.isEmpty() == false || Time.isEmpty() == false){
                for(int i = 0; i < photos.size(); i++) {
                    String path = photos.get(i);
                    String[] attr = path.split("_");
                    if(attr[3].equals(Caption) || attr[1].equals(Time) || attr[2].equals(Time))
                    {
                        displayPhoto(photos.get(i));
                        break;
                    }
                }
            }
        }
        if(imageDisplayed == true) {
            Intent refresh = new Intent(MainActivity.this, MainActivity.class);
            startActivity(refresh);
            imageDisplayed = false;
        }


    }
}





