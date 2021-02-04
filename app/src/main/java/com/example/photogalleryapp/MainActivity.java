package com.example.photogalleryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    public LocationManager locationManager;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int SEARCH_ACTIVITY_REQUEST_CODE = 2;
    String mCurrentPhotoPath;
    private ArrayList<String> photos = null;
    private int index = 0;
    public static final String EXTRA_MESSAGE = "com.example.PhotoGalleryApp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getLocation();
        photos = findPhotos(new Date(Long.MIN_VALUE), new Date(), "","","");
        if (photos.size() == 0) {
            try {
                displayPhoto(null);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                displayPhoto(photos.get(index));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


    }

    /** Called when the user taps the Send button */
    public void search(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, SEARCH_ACTIVITY_REQUEST_CODE);
    }

    public void takePhoto(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this, "com.example.photogalleryapp.fileprovider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        //}
    }

    private ArrayList<String> findPhotos(Date startTimestamp, Date endTimestamp, String keywords,String latitude, String longitude) {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "Android/data/com.example.photogalleryapp/files/Pictures");
        ArrayList<String> photos = new ArrayList<String>();
        File[] fList = file.listFiles();
        if (fList != null) {
            for (File f : fList) {
                if (((startTimestamp == null && endTimestamp == null) || (f.lastModified() >= startTimestamp.getTime()
                        && f.lastModified() <= endTimestamp.getTime())
                ) && (keywords == "" || f.getPath().contains(keywords))&&(latitude== "" || f.getPath().contains(latitude))&&(longitude == "" || f.getPath().contains(longitude)))
                    photos.add(f.getPath());
            }
        }
        return photos;
    }

    public void scrollPhotos(View v) throws ParseException {
//        updatePhoto(photos.get(index), ((EditText) findViewById(R.id.Caption)).getText().toString());
        updatePhoto(photos.get(index), ((EditText) findViewById(R.id.Caption)).getText().toString(),((TextView) findViewById(R.id.Location)).getText().toString());
        switch (v.getId()) {
            case R.id.Left:
                if (index > 0) {
                    index--;
                }
                break;
            case R.id.Right:
                if (index < (photos.size() - 1)) {
                    index++;
                }
                break;
            default:
                break;
        }
        displayPhoto(photos.get(index));
    }

    private void displayPhoto(String path) throws ParseException {

        ImageView iv = (ImageView) findViewById(R.id.imageView);
        TextView tv = (TextView) findViewById(R.id.Timestamp);
        TextView loc = (TextView) findViewById(R.id.Location);
        EditText et = (EditText) findViewById(R.id.Caption);

        if (path == null || path == "") {
            iv.setImageResource(R.mipmap.ic_launcher);
            et.setText("");
            tv.setText("");
        } else {
            iv.setImageBitmap(BitmapFactory.decodeFile(path));
            String[] attr = path.split("_");

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date dat = format.parse(attr[2].substring(0, 4) + "-" + attr[2].substring(4, 6) + "-" + attr[2].substring(6, 8) + " " + attr[3].substring(0, 2) + ":" + attr[3].substring(2, 4) + ":" + attr[3].substring(4, 6));
            String d = new SimpleDateFormat(
                    "yyyy‐MM‐dd HH:mm:ss", Locale.getDefault()).format(dat);
            tv.setText(d);
            et.setText(attr[1]);
            loc.setText(attr[4]);

        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        TextView loc = (TextView) findViewById(R.id.Location);
        String location = loc.getText().toString();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName ="_CAPTION_" + timeStamp + "_" + location +"_";// + tt.getText().toString().toString()+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg",storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void updatePhoto(String path, String caption,String latLong) {
        String[] attr = path.split("_");
        if (attr.length >= 3) {
            File to = new File(attr[0]+"_"+caption + "_" + attr[2] + "_" + attr[3]+ "_"+ latLong+"_"+attr[5]);
            File from = new File(path);
            from.renameTo(to);
            mCurrentPhotoPath = to.getPath();
            Collections.replaceAll(photos,from.getPath(),mCurrentPhotoPath);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DateFormat format = new SimpleDateFormat("yyyy‐MM‐dd HH:mm:ss");
                Date startTimestamp , endTimestamp;
                try {
                    String from = (String) data.getStringExtra("STARTTIMESTAMP");
                    String to = (String) data.getStringExtra("ENDTIMESTAMP");
                    startTimestamp = format.parse(from);
                    endTimestamp = format.parse(to);
                } catch (Exception ex) {
                    startTimestamp = null;
                    endTimestamp = null;
                }
                String keywords = (String) data.getStringExtra("KEYWORDS");
                String latitude = (String) data.getStringExtra("LATITUDE");
                String longitude = (String) data.getStringExtra("LONGITUDE");
                photos.add(mCurrentPhotoPath);
                index = 0;
                photos = findPhotos(startTimestamp, endTimestamp, keywords,latitude,longitude);
                if (photos.size() == 0) {
                    try {
                        displayPhoto(null);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        displayPhoto(photos.get(index));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            getLocation();
            photos.add(mCurrentPhotoPath);
            ImageView mImageView = (ImageView) findViewById(R.id.imageView);
            mImageView.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
            photos = findPhotos(new Date(Long.MIN_VALUE), new Date(), "","","");
            try {
                displayPhoto(photos.get(index));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        try{
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, MainActivity.this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(this, ""+location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show();
        TextView loc = (TextView) findViewById(R.id.Location);
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        String strLat = String. format("%.2f", lat);
        String strLon = String. format("%.2f", lon);

        loc.setText("Lat:"+ strLat + " Long:" + strLon);
        updatePhoto(photos.get(index), ((EditText) findViewById(R.id.Caption)).getText().toString(),((TextView) findViewById(R.id.Location)).getText().toString());
        return;
    }

}





