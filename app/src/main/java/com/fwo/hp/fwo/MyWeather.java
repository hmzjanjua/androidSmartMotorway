package com.fwo.hp.fwo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by Xa Tao Ra on 12/21/2015.
 */


public class MyWeather implements Serializable {

    MyWeatherCallBack mCallBack = null;
    private DayWeather dayWeather;
    private String apiKey = "888f6b25afb559fc027310f7bc8e9a3a";
    private String queryLink = "";
    //Week
    int numberOfDay;
    DayWeather[] weekArray;

    //Hinh anh
    private String statusImagePath;

    //Don vi
    String tempUnit = "C";

    public MyWeather(MyWeatherCallBack callBack, String link)
    {
        this.mCallBack = callBack;

        statusImagePath = "@drawable/not_available";

        queryLink = link + "&units=metric&APPID=" + getApiKey();
    }


    public String getOverview()
    {
        return dayWeather.getWeatherMain();
    }

    public String getTempurature()
    {
        return dayWeather.getMainTemp();
    }

    public String getHumidity()
    {
        return dayWeather.getMainHumidity();
    }

    public String getMinTempurature()
    {
        return dayWeather.getMainTemp_min();
    }

    public String getMaxTempurature()
    {
        return dayWeather.getMainTemp_max();
    }

    public String getCityName()
    {
        return dayWeather.getName();
    }

    public String getWindDetail()
    {
        return (dayWeather.getWindSpeed() + "km/h. Direction: "  + dayWeather.getWindDeg() + "°");
    }

    public String getDescription()
    {
        return dayWeather.getWeatherDescription();
    }

    public String getCountryName()
    {
        return dayWeather.getSysCountry();
    }

   public String getSunriseTime()
   {
       return dayWeather.getSysSunrise();
   }

    public String getSunsetTime()
    {
        return dayWeather.getSysSunset();
    }

    public String getCityID()
    {
        return dayWeather.getId();
    }

    public String getCloudiness()
    {
        return dayWeather.getCloudsAll();
    }




    /*
    public void sortImage()
    {

        statusImagePath = "@drawable/" + dayWeather.getWeatherIcon();
        for (int i = 0; i < numberOfDay; i++)
        {
            weekArray[i].statusImagePath = "@drawable/i" + weekArray[i].conditionCode;
        }
    }

    */

    public void getData()
    {
        ContentDownloader downloader = new ContentDownloader();
        ImageDownloader imgDownloader = new ImageDownloader();
        try {
            String data = downloader.execute(queryLink).get();
            dayWeather = new DayWeather(data);
            //this.statusImage = imgDownloader.execute("http://openweathermap.org/img/w/" + dayWeather.getWeatherIcon() + ".png").get();
            statusImagePath = "@drawable/i" + dayWeather.getWeatherIcon();
            mCallBack.UpdateView();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public String getStatusImagePath() {
        return statusImagePath;
    }

    public String getApiKey() {
        return apiKey;
    }
}

class ContentDownloader extends AsyncTask<String,Integer,String>
{
    @Override
    protected String doInBackground(String... urls) {
        String data = "";
        int time = 3;

        do {
            time--;

            try {
                    URL url = new URL(urls[0]);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    InputStream inputStream = urlConnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(inputStream);

                    BufferedReader bufferedReader = new BufferedReader(reader);
                    StringBuilder builder = new StringBuilder();


                    while ((data = bufferedReader.readLine()) != null) {
                        builder.append(data);
                    }

                    data = builder.toString();

                     Log.i("MyTag",data);




            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("MyTag","MalformedURL");
            } catch (IOException e) {
                Log.e("MyTag", "IOException");
                e.printStackTrace();
            }
        }
        while ((data == null || data.isEmpty() || data == " ") && time > 0);
        return data;
    }

    @Override
    protected void onPostExecute(String data) {
        super.onPostExecute(data);
    }
}

class ImageDownloader extends AsyncTask<String,Void,Bitmap> {
    @Override
    protected Bitmap doInBackground(String... urls) {
        URL url = null;
        Bitmap bitmap = null;
        try {
            url = new URL(urls[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();

            bitmap = BitmapFactory.decodeStream(inputStream);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
