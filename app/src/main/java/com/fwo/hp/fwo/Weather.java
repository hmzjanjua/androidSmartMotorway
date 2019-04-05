package com.fwo.hp.fwo;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Weather extends AppCompatActivity implements MyWeatherCallBack {
    //Khai bao bien
    TextView txtTempurature;
    TextView txtWindSpeed;
    TextView txtSunrise;
    TextView txtSunset;
    TextView txtLocation;
    TextView txtHighTemper;
    TextView txtLowTemper;
    TextView txtHumidity;
    TextView txtCloudiness;
    TextView txtCountry;
    TextView txtDate;
    TextView txtConditionText;
    Button txtHelper;
    ImageView imgWeatherStatus;

    //Class main
    MyWeather weatherResult;

    //Default location
    String rootLink;
    String queryLink;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Intent getLocation = new Intent(getBaseContext(), GetLocation.class);
        data=getIntent().getStringExtra("city_weather");
        getLocation.putExtra("city",data);
        //request code 1 cho get location
        startActivityForResult(getLocation, 1);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        rootLink = "http://api.openweathermap.org/data/2.5/weather?";
        queryLink = rootLink + "q=London,uk";



        //Lay thong tin view
        findAllView();

        Button btnGetLocation = (Button) findViewById(R.id.get_location_button);


        //Su kien click lay thong tin dia diem
        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getLocation = new Intent(getBaseContext(), GetLocation.class);

                //request code 1 cho get location
                startActivityForResult(getLocation, 1);


                //setTitle("Test");
                /*Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });*/


            }
        });

        weatherResult = new MyWeather(this, queryLink);
        weatherResult.getData();
    }

    @Override
    public void UpdateView() {
        Resources res = getResources();
        String mDrawableName = weatherResult.getStatusImagePath();
        int resID = res.getIdentifier(mDrawableName, "drawable", getPackageName());
        Drawable drawable = res.getDrawable(resID);
        imgWeatherStatus.setImageDrawable(drawable);


        //imgWeatherStatus.setImageBitmap(weatherResult.getStatusImage());

        txtLocation.setText(weatherResult.getCityName());
        txtCountry.setText(weatherResult.getCountryName());
        txtTempurature.setText(weatherResult.getTempurature() + "°" + weatherResult.tempUnit);
        txtHighTemper.setText(weatherResult.getMaxTempurature() + "°" + weatherResult.tempUnit);
        txtLowTemper.setText(weatherResult.getMinTempurature() + "°" + weatherResult.tempUnit);
        txtWindSpeed.setText(weatherResult.getWindDetail());
        txtHumidity.setText(weatherResult.getHumidity() + "%");
        txtCloudiness.setText(weatherResult.getCloudiness() + "%");
        txtSunrise.setText(weatherResult.getSunriseTime());
        txtSunset.setText(weatherResult.getSunsetTime());
        txtConditionText.setText(weatherResult.getOverview());
        txtDate.setText(weatherResult.getDescription());


        txtHelper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forecastIntent = new Intent(getBaseContext(),ForecaseActivity.class);
                forecastIntent.putExtra("weatherExtra", "http://api.openweathermap.org/data/2.5/forecast/daily?id=" + weatherResult.getCityID() + "&units=metric&cnt=10&appid=" + weatherResult.getApiKey());
                startActivity(forecastIntent);
            }
        });

    }
    private void findAllView() {
        //
        imgWeatherStatus = (ImageView) findViewById(R.id.weather_imageView);
        txtTempurature = (TextView) findViewById(R.id.temperature_textView);
        txtHighTemper = (TextView) findViewById(R.id.high_temper_textView);
        txtLowTemper = (TextView) findViewById(R.id.low_temper_textView);
        txtWindSpeed = (TextView) findViewById(R.id.wind_speed_textView);
        txtSunrise = (TextView) findViewById(R.id.sunrise_textView);
        txtSunset = (TextView) findViewById(R.id.sunset_textView);
        txtLocation = (TextView) findViewById(R.id.location_textView);
        txtHumidity = (TextView) findViewById(R.id.humidity_textView);
        txtCloudiness = (TextView) findViewById(R.id.cloud_textview);
        txtConditionText = (TextView) findViewById(R.id.conditionText_textView);
        txtCountry = (TextView) findViewById(R.id.country_textView);
        txtDate = (TextView) findViewById(R.id.status_textview);
        txtHelper = (Button) findViewById(R.id.helper_textview);
    }



    //Cac su kien listener
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode != 0 && data != null) {
            //Lay du lieu tra ve tu clas child
            String woeid = data.getStringExtra("woeid");

            if (resultCode == 1)
                queryLink = rootLink + "id=" + woeid;
            else {

                try {
                    queryLink = rootLink + "q=" + URLEncoder.encode(woeid, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            //Lay thong tin tu API
            weatherResult = new MyWeather(this, queryLink);
            weatherResult.getData();

        }
    }
}

