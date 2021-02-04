package com.example.photogalleryapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent; import android.os.Bundle;
import android.view.View; import android.widget.EditText;
import java.text.DateFormat; import java.text.SimpleDateFormat;
import java.util.Calendar; import java.util.Date;
import java.util.Locale;
public class SearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        try {
            Calendar calendar = Calendar.getInstance();
            DateFormat format = new SimpleDateFormat("yyyy‐MM‐dd");
            Date now = calendar.getTime();
            String todayStr = new SimpleDateFormat("yyyy‐MM‐dd", Locale.getDefault()).format(now);
            Date today = format.parse((String) todayStr);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            String tomorrowStr = new SimpleDateFormat("yyyy‐MM‐dd", Locale.getDefault()).format(calendar.getTime());
            Date tomorrow = format.parse((String) tomorrowStr);
            ((EditText) findViewById(R.id.etFromDateTime)).setText("");
            ((EditText) findViewById(R.id.etToDateTime)).setText("");
//            ((EditText) findViewById(R.id.etFromDateTime)).setText(new SimpleDateFormat(
//                    "yyyy‐MM‐dd HH:mm:ss", Locale.getDefault()).format(today));
//            ((EditText) findViewById(R.id.etToDateTime)).setText(new SimpleDateFormat(
//                    "yyyy‐MM‐dd HH:mm:ss", Locale.getDefault()).format(tomorrow));
        } catch (Exception ex) {
        }
    }

    public void cancel(final View v) {
        finish();
    }

    public void go(final View v) {
        Intent i = new Intent();
        EditText from = (EditText) findViewById(R.id.etFromDateTime);
        EditText to = (EditText) findViewById(R.id.etToDateTime);
        EditText keywords = (EditText) findViewById(R.id.etKeywords);
        EditText latitude = (EditText) findViewById(R.id.etLati);
        EditText longitude = (EditText) findViewById(R.id.etLongi);
        i.putExtra("STARTTIMESTAMP", from.getText() != null ? from.getText().toString() : "");
        i.putExtra("ENDTIMESTAMP", to.getText() != null ? to.getText().toString() : "");
        i.putExtra("KEYWORDS", keywords.getText() != null ? keywords.getText().toString() : "");
        i.putExtra("LATITUDE", latitude.getText() != null ? latitude.getText().toString() : "");
        i.putExtra("LONGITUDE", longitude.getText() != null ? longitude.getText().toString() : "");
        setResult(RESULT_OK, i);
       // Intent intent = new Intent(SearchActivity.this,MainActivity.class);
       // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //startActivityForResult(intent,SEARCH_ACTIVITY_REQUEST_CODE);
        finish();
    }
}