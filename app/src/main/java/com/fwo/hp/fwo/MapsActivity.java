package com.fwo.hp.fwo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.impl.io.ContentLengthOutputStream;

import static com.fwo.hp.fwo.R.id.googleMap;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();


    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(MapsActivity.this);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        Double l,t;
        mMap.addMarker(new MarkerOptions().position
                (new LatLng(getIntent().getDoubleExtra("LATITUDE", 0), getIntent().getDoubleExtra("LONGITUDE", 0)))
                .title(getIntent().getStringExtra("NAME"))).showInfoWindow();
       CameraUpdate c= CameraUpdateFactory.newLatLngZoom(new LatLng(getIntent().getDoubleExtra("LATITUDE", 0), getIntent().getDoubleExtra("LONGITUDE", 0)),15);
         mMap.moveCamera(c);
        l= Double.valueOf(MainActivity.latitude);
        t= Double.valueOf(MainActivity.longitude);
          mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
           mMap.setMyLocationEnabled(true);
        mMap.addPolyline(new PolylineOptions().geodesic(true)
                .add(new LatLng(getIntent().getDoubleExtra("LATITUDE", 0), getIntent().getDoubleExtra("LONGITUDE", 0))) // Sydney
                .add(new LatLng(l,t)) .color(Color.BLUE) // Fiji

        );

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
mMap=googleMap;

        setUpMap();
    }
}
