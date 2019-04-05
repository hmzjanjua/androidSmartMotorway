package com.fwo.hp.fwo.app.entity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fwo.hp.fwo.R;

/**
 * Created by ajnen on 14/10/2017.
 */

public class CoreEntity {
    private Context context;
    private SharedPreferences sharedPref;

    CoreEntity(Context context){
        this.context = context;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getTemperatureUnit(){
        return sharedPref.getString(context.getString(R.string.pref_temperature_key), "1");
    }

    public String getTimeUnit(){
        return sharedPref.getString(context.getString(R.string.pref_time_key), "1");
    }
}
