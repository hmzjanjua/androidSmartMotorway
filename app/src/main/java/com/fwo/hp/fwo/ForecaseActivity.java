package com.fwo.hp.fwo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

public class ForecaseActivity extends AppCompatActivity {

    //Khai bao bien
    TextView txtweekDays[];
    TextView txtWeekDaysTemper[];
    TextView txtLocation;

    String weatherIcon[];
    //MyWeather weatherResult;
    String apiQuery = "";
    String jsonContent = "";
    String statusImagePath = "@drawable/i";
    int numberOfDay = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecase);
        setTitle("DỰ BÁO");
        findAllViewById();

        apiQuery = getIntent().getStringExtra("weatherExtra");

        Log.i("MyTag",apiQuery);

        ContentDownloader contentDownloader = new ContentDownloader();
        try {
            jsonContent = contentDownloader.execute(apiQuery).get();
            parseData(jsonContent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        for(int i = 0; i < numberOfDay; i++)
        {
            String imageViewName = "weekDays_" + i + "_imageView";
            int imageViewID = getResources().getIdentifier(imageViewName, "id", getPackageName());
            ImageView imgView = ((ImageView) findViewById(imageViewID));
            int resIDArray = getResources().getIdentifier(statusImagePath + weatherIcon[i], "drawable", getPackageName());
            imgView.setImageDrawable(getResources().getDrawable(resIDArray));
        }


    }

    private void findAllViewById()
    {
        //Lay view
        txtWeekDaysTemper = new TextView[numberOfDay];
        txtweekDays = new TextView[numberOfDay];
        weatherIcon = new String[numberOfDay];

        txtLocation = (TextView)findViewById(R.id.forecast_location_textview);

        //Lay view theo ten dang String
        for(int i = 0; i < numberOfDay; i++)
        {
            String textViewName = "weekDays_" + i + "_textView";
            int txtViewID = getResources().getIdentifier(textViewName,"id",getPackageName());
            txtweekDays[i] = (TextView)findViewById(txtViewID);
        }


        for(int i = 0; i < numberOfDay; i++)
        {
            String temperTextViewName = "weekDays_" + i + "_temper_textView";
            int txtViewID = getResources().getIdentifier(temperTextViewName,"id",getPackageName());
            txtWeekDaysTemper[i] = (TextView)findViewById(txtViewID);
        }
    }

    private void parseData(String data)
    {
        try {
            Log.i("MyTag",data);
            JSONObject root = new JSONObject(data);

            txtLocation.setText(root.getJSONObject("city").getString("name"));

            JSONArray list = root.getJSONArray("list");
            for (int i = 0; i < list.length(); i++)
            {
                JSONObject jsonObject = list.getJSONObject(i);
                txtweekDays[i].setText(convertUnixTimeStamp(jsonObject.getString("dt")));
                weatherIcon[i] = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");

                JSONObject temp = jsonObject.getJSONObject("temp");
                txtWeekDaysTemper[i].setText("High: " + temp.getString("max") + "°C\tLow: " + temp.getString("min") + "°C\nWind speed: " + jsonObject.getString("speed") + "\tHumidity: " + jsonObject.getString("humidity") + "%");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    String convertUnixTimeStamp(String timestamp)
    {
        long dv = Long.valueOf(timestamp)*1000;// its need to be in milisecond
        Date df = new java.util.Date(dv);
        SimpleDateFormat sdf =  new SimpleDateFormat("EEE, MM/dd/yyyy");
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(df);
    }
}
