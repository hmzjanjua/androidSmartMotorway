package com.fwo.hp.fwo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Xa Tao Ra on 12/21/2015.
 */

public class DayWeather implements Serializable
{
    private String weatherID = "721";
    private String weatherMain = "Haze";
    private String weatherDescription = "haze";
    private String weatherIcon = "50d";
    private String base = "cmc stations";
    private String mainTemp = "290.29";
    private String mainPressure = "1018";
    private String mainHumidity = "82";
    private String mainTemp_min = "288.15";
    private String mainTemp_max = "293.15";
    private String windSpeed = "5.1";
    private String windDeg = "210";
    private String cloudsAll = "90";
    private String dt = "1468047939";
    private String sysCountry = "GB";
    private String sysSunrise = "1468036501";
    private String sysSunset = "1468095366";
    private String id = "2643743";
    private String name = "London";

    public DayWeather(String jsonString)
    {
        try {
            JSONObject root = new JSONObject(jsonString);
            JSONObject weather = root.getJSONArray("weather").getJSONObject(0);
            JSONObject main = root.getJSONObject("main");
            JSONObject wind = root.getJSONObject("wind");
            JSONObject clouds = root.getJSONObject("clouds");
            JSONObject sys = root.getJSONObject("sys");

            weatherMain = weather.getString("main");
            weatherDescription = weather.getString("description");
            weatherDescription = weatherDescription.substring(0,1).toUpperCase() + weatherDescription.substring(1);
            weatherIcon = weather.getString("icon");
            weatherID = weather.getString("id");

            mainTemp = main.getString("temp");
            mainHumidity = main.getString("humidity");
            mainPressure = main.getString("pressure");
            mainTemp_max = main.getString("temp_max");
            mainTemp_min = main.getString("temp_min");

            windSpeed = wind.getString("speed");
            windDeg = wind.getString("deg");

            cloudsAll = clouds.getString("all");

            sysCountry = sys.getString("country");
            sysSunrise = convertUnixTimeStamp(sys.getString("sunrise"));
            sysSunset = convertUnixTimeStamp(sys.getString("sunset"));

            name = root.getString("name");
            id = root.getString("id");

        } catch (JSONException e) {
            Log.e("MyTag","DayWeather");
            Log.e("MyTag",e.toString());
            e.printStackTrace();
        }
    }

    String convertUnixTimeStamp(String timestamp)
    {
        long dv = Long.valueOf(timestamp)*1000;// its need to be in milisecond
        Date df = new java.util.Date(dv);
        SimpleDateFormat sdf =  new SimpleDateFormat("EEE, MM/dd/yyyy, HH:mm aaa");
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(df);
    }

    public String getWeatherID() {
        return weatherID;
    }

    public String getWeatherMain() {
        return weatherMain;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public String getBase() {
        return base;
    }

    public String getMainTemp() {
        return mainTemp;
    }

    public String getMainPressure() {
        return mainPressure;
    }

    public String getMainHumidity() {
        return mainHumidity;
    }

    public String getMainTemp_min() {
        return mainTemp_min;
    }

    public String getMainTemp_max() {
        return mainTemp_max;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getWindDeg() {
        return windDeg;
    }

    public String getCloudsAll() {
        return cloudsAll;
    }

    public String getDt() {
        return dt;
    }

    public String getSysSunrise() {
        return sysSunrise;
    }

    public String getSysSunset() {
        return sysSunset;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSysCountry() {
        return sysCountry;
    }

    public void setSysCountry(String sysCountry) {
        this.sysCountry = sysCountry;
    }
}