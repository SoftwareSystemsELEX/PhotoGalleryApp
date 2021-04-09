package com.example.photogalleryapp;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class VideoCapture extends AppCompatActivity {
    private static int REQUEST_VIDEO_CAPTURE = 3;
    String  mCurrentVideoPath;
    private ArrayList<String> videos= null;

    private int index = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videos = findVideos();
        if (videos.size() == 0) {
            try {
                displayVideo(null);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                displayVideo(videos.get(index));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void scrollVideos(View v) throws ParseException {
        switch (v.getId()) {
            case R.id.videoLeft:
                if (index > 0) {
                    index--;
                }
                break;
            case R.id.videoRight:
                if (index < (videos.size() - 1)) {
                    index++;
                }
                break;
            default:
                break;
        }
        displayVideo(videos.get(index));
    }
    private void displayVideo(String path) throws ParseException {

        VideoView videoView = (VideoView) findViewById(R.id.videoView);

        if (path == null || path == "" ) {
//            vv.setImageResource(R.mipmap.ic_launcher);
//            vv.setVideoPath();

        } else {
            Uri fileUri = FileProvider.getUriForFile(this, "com.example.photogalleryapp.fileprovider", new File(videos.get(index)));
            videoView.setVideoURI(fileUri);
            videoView.seekTo(1);
            videoView.start();
        }
    }


    public void takeVideo(View v) {
//        Debug.startMethodTracing("takePhoto");
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        //if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
        File videoFile = null;
        try {
            videoFile = createVideoFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        // Continue only if the File was successfully created
        if (videoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this, "com.example.photogalleryapp.fileprovider", videoFile);
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
        //}
    }

    private File createVideoFile() throws IOException {
        //Create Movie File Path

        String videoFileName ="CAPTION_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        File video = File.createTempFile(videoFileName, ".mp4",storageDir);
        mCurrentVideoPath = video.getAbsolutePath();
        return video;
    }

    private ArrayList<String> findVideos() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "Android/data/com.example.photogalleryapp/files/Movies");
        ArrayList<String> videos= new ArrayList<String>();
        File[] fList = file.listFiles();
        if (fList != null) {
            for (File f : fList) {
                    videos.add(f.getPath());
            }
        }
        return videos;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {

            Uri videoUri = data.getData();
            ArrayList<Uri> videoUriList = new ArrayList<Uri>();
            videoUriList.add(videoUri);

            videos = findVideos();
            try {
                displayVideo(videos.get(index));
            } catch (ParseException e) {
                e.printStackTrace();
            }





        }
    }


}
