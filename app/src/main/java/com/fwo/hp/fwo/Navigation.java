package com.fwo.hp.fwo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fwo.hp.fwo.helper.DatabaseHandler;
import com.fwo.hp.fwo.helper.Functions;
import com.fwo.hp.fwo.helper.SessionManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.Map;

public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,LocationListener {
    private SessionManager session;
    private GoogleMap googleMap;
    private static final String TAG = Navigation.class.getSimpleName();
    private DatabaseHandler db;
    private HashMap<String,String> user = new HashMap<>();
    private ProgressDialog pDialog;
    TextView txtName,txtEmail,txt_person;
    private Button btnChangePass, btnLogout;
    final String APIKey="AIzaSyBs5e7Xg-dR-eKaAqn4bWIYX0RwrwBTyw4";
    String name,email;
    MediaPlayer mp;
    public static String l_address;
    ImageButton img_emer;
    boolean musicPlaying=false;
    String place_name,place_address,place_phone;
    String fullurl;
    String ServerURL = "http://www.fwosmartmotorways.com/server_side_php/emer.php" ;
    public static double latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        btnChangePass = (Button) findViewById(R.id.change_password);
        btnLogout = (Button) findViewById(R.id.logout);
        img_emer = (ImageButton) findViewById(R.id.btn_emer);
        img_emer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                Toast.makeText(Navigation.this,"your current location send successfully",Toast.LENGTH_LONG).show();
                callIntent.setData(Uri.parse("tel:9909"));
                startActivity(callIntent);

                //   String fullurl="plz help me this is my current location: " + "http://maps.google.com/maps?q=" + Splash.latitude + "," + Splash.longitude,null, null));
                SmsManager smsManager =     SmsManager.getDefault();
                smsManager.sendTextMessage("03002426173", null,"plz help me this is my current location: " + "http://maps.google.com/maps?q=" + latitude + "," + longitude, null, null);
                GetData();

                InsertData(place_name,place_phone,place_address);
                //l_address="http://maps.google.com/maps?q=" + Splash.latitude + "," + Splash.longitude;
            }
        });

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        db = new DatabaseHandler(getApplicationContext());
        user = db.getUserDetails();

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from database
        name = user.get("name");
        email= user.get("email");

        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);

        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        }else { // Google Play Services are available

            // Getting reference to the SupportMapFragment of activity_main.xml
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            // Getting GoogleMap object from the fragment
            googleMap = fm.getMap();

            // Enabling MyLocation Layer of Google Map
            googleMap.setMyLocationEnabled(true);


            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Getting Current Location
            Location location = locationManager.getLastKnownLocation(provider);

            if(location!=null){
                onLocationChanged(location);
                location.setLatitude(MainActivity.latitude);
                location.setLongitude(MainActivity.longitude);
            }
            locationManager.requestLocationUpdates(provider, 20000, 0, this);

        }
        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        txt_person=(TextView)header.findViewById(R.id.textView4);
        txt_person.setText(name);
    }

    public void GetData(){



        place_name = name;
        place_phone=email;
        place_address="http://maps.google.com/maps?q=" + latitude + "," + longitude;


    }
    public void InsertData(final String name, final String email,final String n){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String NameHolder = name ;
                String EmailHolder = email ;
                String NHolder=n;



                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("name", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("phone", EmailHolder));
                nameValuePairs.add(new BasicNameValuePair("location", NHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ServerURL);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "Location Sent Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

                Toast.makeText(Navigation.this, "Location Sent Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(name, email);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
            mp.stop();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Download and Install Smart Motorways Android App its too helpul while traveling on motorway.";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        } else if (id == R.id.nav_gallery) {
            Intent intent=new Intent(getApplicationContext(),About.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent=new Intent(getApplicationContext(),selection.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            Intent intent=new Intent(getApplicationContext(),RateActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {

            logoutUser();

        }

        else if (id == R.id.nav_Journey) {

            Intent intent=new Intent(getApplicationContext(),Journey.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_dua) {
            Intent intent=new Intent(getApplicationContext(),safar.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_fm) {
            Intent intent=new Intent(getApplicationContext(),Radio.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_rech) {
            Intent intent=new Intent(getApplicationContext(),Recharge.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_youtube) {
            Intent intent=new Intent(getApplicationContext(),StandaloneActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_help) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            Toast.makeText(Navigation.this,"your current location send successfully",Toast.LENGTH_LONG).show();
            callIntent.setData(Uri.parse("tel:9909"));
            startActivity(callIntent);
            fullurl="http://maps.google.com/maps?q=" + Splash.latitude + "," + Splash.longitude;
            SmsManager smsManager =     SmsManager.getDefault();
            smsManager.sendTextMessage("03002426173", null,"plz help me this is my current location: " + "http://maps.google.com/maps?q=" + longitude + "," + latitude, null, null);
            GetData();

            InsertData(place_name,place_phone,place_address);
            //l_address="http://maps.google.com/maps?q=" + Splash.latitude + "," + Splash.longitude;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {

        //TextView tvLocation = (TextView) findViewById(R.id.tv_location);

        // Getting latitude of the current location
        latitude = location.getLatitude();

        // Getting longitude of the current location
        longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.clear();
        // Showing the current location in Google Map
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//Toast.makeText(Navigation.this,"Latitude:" +  Splash.latitude  + ", Longitude:"+ Splash.longitude ,Toast.LENGTH_LONG).show();
        // Setting latitude and longitude in the TextView tv_location
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("Lets Go").snippet("FWO SMART MOTORWAYS")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));



       /* PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.add(latLng)
                .add(new LatLng(31.535034, 74.262033))
                .add(new LatLng(location.getLatitude(),location.getLongitude()));
        googleMap.addPolyline(polylineOptions);*/
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
    private void init() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Navigation.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.change_password, null);

                dialogBuilder.setView(dialogView);
                dialogBuilder.setTitle("Change Password");
                dialogBuilder.setCancelable(false);

                final TextInputLayout oldPassword = (TextInputLayout) dialogView.findViewById(R.id.old_password);
                final TextInputLayout newPassword = (TextInputLayout) dialogView.findViewById(R.id.new_password);

                dialogBuilder.setPositiveButton("Change",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // empty
                    }
                });

                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                final AlertDialog alertDialog = dialogBuilder.create();

                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(oldPassword.getEditText().getText().length() > 0 &&
                                newPassword.getEditText().getText().length() > 0){
                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        } else {
                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                };

                oldPassword.getEditText().addTextChangedListener(textWatcher);
                newPassword.getEditText().addTextChangedListener(textWatcher);

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        final Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setEnabled(false);

                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String email = user.get("email");
                                String old_pass = oldPassword.getEditText().getText().toString();
                                String new_pass = newPassword.getEditText().getText().toString();

                                if (!old_pass.isEmpty() && !new_pass.isEmpty()) {
                                    changePassword(email, old_pass, new_pass);
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Fill all values!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                });

                alertDialog.show();
            }
        });
    }

    private void logoutUser() {
        session.setLogin(false);
        // Launching the login activity
        Functions logout = new Functions();
        logout.logoutUser(getApplicationContext());
        Intent intent = new Intent(Navigation.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void changePassword(final String email, final String old_pass, final String new_pass) {
        // Tag used to cancel the request
        String tag_string_req = "req_reset_pass";

        pDialog.setMessage("Please wait...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.RESET_PASS_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Reset Password Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Reset Password Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected java.util.Map<String, String> getParams() {
                // Posting parameters to login url
                java.util.Map<String, String> params = new HashMap<>();

                params.put("tag", "change_pass");
                params.put("email", email);
                params.put("old_password", old_pass);
                params.put("password", new_pass);

                return params;
            }

            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

        };

        // Adding request to volley request queue
        strReq.setRetryPolicy(new DefaultRetryPolicy(5 * DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
