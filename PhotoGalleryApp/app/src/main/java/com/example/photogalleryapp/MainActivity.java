package com.example.photogalleryapp;

//import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

//import android.Manifest;
import android.annotation.SuppressLint;
//import android.content.ActivityNotFoundException;
import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.graphics.drawable.BitmapDrawable;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
//import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
//import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
//import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity  implements LocationListener {
    public LocationManager locationManager;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int SEARCH_ACTIVITY_REQUEST_CODE = 2;
    String mCurrentPhotoPath;
    private ArrayList<String> photos = null;
    private int index = 0;
    public static final String EXTRA_MESSAGE = "com.example.PhotoGalleryApp.MESSAGE";
    public Button shareButton;
    public ImageView ima;
    TextView weatherDisplay;


    class Weather extends AsyncTask<String, Void, String> { //First stream means URL, void is nothing, third string means return type will be String

        public void searchWeather(String location){
            String content;
            Weather weather = new Weather();
            weatherDisplay = findViewById(R.id.weather);

            try {
                content = weather.execute("https://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=84b00d028b446f75b2dfcf13744a9964").get();
                //First check data is retreived successfully
                Log.i("content", content);

                //JSON
                JSONObject jsonObject = new JSONObject(content);
                String weatherData = jsonObject.getString("weather");
                String mainTemperature = jsonObject.getString("main"); //Temperature

                Log.i("weatherData", weatherData);
                //weather data is in array
                JSONArray array = new JSONArray(weatherData);

                String main = "";
                String description = "";
                String temperatureKelvin = "";

                for (int i = 0; i < array.length(); i++) {
                    JSONObject weatherPart = array.getJSONObject(i);
                    main = weatherPart.getString("main");
                    description = weatherPart.getString("description");
                }

                JSONObject mainPart = new JSONObject(mainTemperature);
                temperatureKelvin = mainPart.getString("temp");
                Double temp = new Double(temperatureKelvin);
                temp = temp - 273.15;
                String temperatureCelcius = String.format("%.2f", temp);


                Log.i("main", main);
                Log.i("description", description);
                weatherDisplay.setText("Main:" + main + "\nDescription: " + description + "\nTemperature: " + temperatureCelcius + "Celcius");

            } catch (Exception e) {
                e.printStackTrace();
            }



        }
        @Override
        protected String doInBackground(String... address) {
            //String... means multiple address can be sent, it acts as an array
            try {
                URL url = new URL(address[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int data = isr.read();
                String content = "";
                char ch;
                while (data != -1){
                    ch = (char) data;
                    content = content + ch;
                    data = isr.read();

                }
                return content;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Debug.startMethodTracing("onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Weather displayWeather = new Weather();
        displayWeather.searchWeather("London");

        shareButton = (Button) findViewById((R.id.share));
        ima = (ImageView) findViewById(R.id.imageView);

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
        shareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                    image();
            }
        });
//        Debug.stopMethodTracing();
    }

//    @Override
//    protected void onStop() {
//        Debug.stopMethodTracing();
//        super.onStop();
//    }
//




    /** Called when the user tap    1s the Send button */
    public void search(View view) {

        // Do something in response to button
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, SEARCH_ACTIVITY_REQUEST_CODE);
    }

    public void takePhoto(View v) {
        Debug.startMethodTracing("takePhoto");
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


//
//    protected void onDraw(Canvas canvas){
//        canvas.drawColor(Color.WHITE);
//        Paint p = new Paint();
//        float y = 10;
//        SparseArray<String> sparseArrayapp
//build.gradle
//gradle
//gradle.properties
//gradlew
//gradlew.bat
//PhotoGalleryApp.plantuml
//settings.gradle = new SparseArray<>();
//    }


    private ArrayList<String> findPhotos(Date startTimestamp, Date endTimestamp, String keywords,String latitude, String longitude) {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "Android/data/com.example.photogalleryapp/files/Pictures");
        ArrayList<String> photos = new ArrayList<String>();
        File[] fList = file.listFiles();
        if (fList != null) {
            for (File f : fList) {
                if (((startTimestamp == null && endTimestamp == null) || (f.lastModified() >= startTimestamp.getTime()
                        && f.lastModified() <= endTimestamp.getTime())
                ) && (keywords == "" || f.getPath().contains(keywords))
                        &&(latitude== "" || f.getPath().contains(latitude))
                        &&(longitude == "" || f.getPath().contains(longitude)))
                    photos.add(f.getPath());
            }
        }
        return photos;
    }

    public void scrollPhotos(View v) throws ParseException {
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
        String imageFileName ="_CAPTION_" + timeStamp + "_" + location +"_";
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
            mCurrentPhotoPath = to.getAbsolutePath();
            Collections.replaceAll(photos,from.getAbsolutePath(),mCurrentPhotoPath);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
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
            Debug.stopMethodTracing();
        }
    }
    private void image(){

        String PACKAGE_NAME = "com.google.android.gm";
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"sidhubalkaran9@gmail.com"});
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, ((EditText) findViewById(R.id.Caption)).getText().toString());
        shareIntent.putExtra(Intent.EXTRA_TEXT, "hello. this is a message sent from Photogallery app :-)");
        Uri fileUri = FileProvider.getUriForFile(this, "com.example.photogalleryapp.fileprovider", new File(photos.get(index)));
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        shareIntent.setPackage(PACKAGE_NAME);
        startActivity(Intent.createChooser(shareIntent,"share Image"));

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
        TextView loc = (TextView) findViewById(R.id.Location);
        double lat = location.getLatitude(),lon = location.getLongitude();
        String strLat = String. format("%.2f", lat),strLon = String. format("%.2f", lon);
        loc.setText("Lat:"+ strLat + " Long:" + strLon);
        updatePhoto(photos.get(index), ((EditText) findViewById(R.id.Caption)).getText().toString(),((TextView) findViewById(R.id.Location)).getText().toString());
        return;
    }

}





