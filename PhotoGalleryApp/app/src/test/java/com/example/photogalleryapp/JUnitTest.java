package com.example.photogalleryapp;

import android.os.Environment;

import org.junit.Test;

import java.io.File;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.*;
import static org.junit.Assert.assertEquals;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class JUnitTest {
    @Test
    public void findPhotos_iscorrect() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date1 = format.parse("2021-01-01 00:00:00");
        String from = new SimpleDateFormat(
                "yyyy‐MM‐dd HH:mm:ss", Locale.getDefault()).format(date1);
        Date date2 = format.parse("2021-04-26 00:00:00");
        String to = new SimpleDateFormat(
                "yyyy‐MM‐dd HH:mm:ss", Locale.getDefault()).format(date2);
        ArrayList<String> DummyPhotoPaths = new ArrayList(Arrays.asList(
        "Android/data/com.example.photogalleryapp/files/Pictures/_door_20210203_195335_Lat:22.30 Long:11.85_2509275942840573539.jpg",
        "Android/data/com.example.photogalleryapp/files/Pictures/_cat_20210206_165335_Lat:13.30 Long:12.95_2509275942840573531.jpg",
        "Android/data/com.example.photogalleryapp/files/Pictures/_dog_20210321_145335_Lat:12.30 Long:12.15_2509275942840573532.jpg",
        "Android/data/com.example.photogalleryapp/files/Pictures/_black_20210204_125335_Lat:31.30 Long:11.85_2509275942840573537.jpg",
        "Android/data/com.example.photogalleryapp/files/Pictures/_taylor_20210502_115325_Lat:22.30 Long:13.85_2509275942840573536.jpg",
        "Android/data/com.example.photogalleryapp/files/Pictures/_swift_20210102_105135_Lat:31.30 Long:14.85_25092759424350573523.jpg",
        "Android/data/com.example.photogalleryapp/files/Pictures/_peas_20200703_125015_Lat:30.30 Long:15.85_250927534534534573533.jpg"
        ));
        ArrayList<String> filterPhotos = findPhotos(DummyPhotoPaths, date1,date2,"","","");//"dog","12.30","12.15");
        System.out.print(filterPhotos);
        ArrayList ExpectedList =  new ArrayList(Arrays.asList(
                "Android/data/com.example.photogalleryapp/files/Pictures/_door_20210203_195335_Lat:22.30 Long:11.85_2509275942840573539.jpg",
                "Android/data/com.example.photogalleryapp/files/Pictures/_cat_20210206_165335_Lat:13.30 Long:12.95_2509275942840573531.jpg",
                "Android/data/com.example.photogalleryapp/files/Pictures/_dog_20210321_145335_Lat:12.30 Long:12.15_2509275942840573532.jpg",
                "Android/data/com.example.photogalleryapp/files/Pictures/_black_20210204_125335_Lat:31.30 Long:11.85_2509275942840573537.jpg",
                "Android/data/com.example.photogalleryapp/files/Pictures/_swift_20210102_105135_Lat:31.30 Long:14.85_25092759424350573523.jpg"
                ));
        assertEquals(ExpectedList, filterPhotos);
    }
    public ArrayList<String> findPhotos(ArrayList<String> photopaths ,Date startTimestamp, Date endTimestamp, String keywords, String latitude, String longitude) throws ParseException {
        ArrayList<String> photos = new ArrayList<String>();
        if (photopaths != null) {
            for (String f : photopaths) {
                String[] attr = f.split("_");
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                Date dat = format.parse(attr[2].substring(0, 4) + "" + attr[2].substring(4, 6) + "" + attr[2].substring(6, 8) + "" + attr[3].substring(0, 2) + "" + attr[3].substring(2, 4) + "" + attr[3].substring(4, 6));

                if (((startTimestamp == null && endTimestamp == null) || (dat.getTime() >= startTimestamp.getTime()
                        && dat.getTime() <= endTimestamp.getTime())
                ) && (keywords == "" || f.contains(keywords))
                        &&(latitude== "" || f.contains(latitude))
                        &&(longitude == "" || f.contains(longitude)))
                    photos.add(f);
            }
        }
        return photos;
    }
}