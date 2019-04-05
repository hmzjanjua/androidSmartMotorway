package com.fwo.hp.fwo.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.fwo.hp.fwo.LoginActivity;
import com.fwo.hp.fwo.R;

public class Splash extends Activity implements LocationListener {

    LocationManager locationManager ;
    String provider;
    private static int SPLASH_TIME_OUT = 5000;
    public static String latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);
        TextView txtView=(TextView) findViewById(R.id.textView23);
        txtView.setSelected(true);

        new Handler().postDelayed(new Runnable() {

			/*
             * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(Splash.this, LoginActivity.class);
                i.putExtra("lat",latitude);
                i.putExtra("long",longitude);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);






        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        // Creating an empty criteria object
        Criteria criteria = new Criteria();

        // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, false);

        if(provider!=null && !provider.equals("")){

            // Get the location from the given provider
            Location location = locationManager.getLastKnownLocation(provider);

            locationManager.requestLocationUpdates(provider, 20000, 1, this);

            if(location!=null)
                onLocationChanged(location);
            else
                Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
        }





    }


    @Override
    public void onLocationChanged(Location location) {

//Toast.makeText(getApplicationContext(),"Longitude:" + location.getLongitude()+"Latitude:" + location.getLatitude(),Toast.LENGTH_LONG).show();
        latitude= String.valueOf(location.getLatitude());
        longitude= String.valueOf(location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

}
