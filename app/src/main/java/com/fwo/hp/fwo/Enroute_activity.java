package com.fwo.hp.fwo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Enroute_activity extends AppCompatActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();

    private TextView txtName, txtEmail;
    private Button btnChangePass, btnLogout;
    private SessionManager session;
    private DatabaseHandler db;
    public static String sr;

    private ProgressDialog pDialog;

    private HashMap<String,String> user = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroute_activity);
        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        btnChangePass = (Button) findViewById(R.id.change_password);
        btnLogout = (Button) findViewById(R.id.logout);


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
        String name = user.get("name");
        String email = user.get("email");

        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);

        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();
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
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Enroute_activity.this);
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
        Intent intent = new Intent(Enroute_activity.this, LoginActivity.class);
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
                java.util.Map<String, String> params = new HashMap<>();
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
    public void srch(View v){
        Intent i=new Intent(this,MainActivity.class);
        sr ="&keyword="+((EditText)findViewById(R.id.editText)).getText().toString();
        sr=sr.replaceAll(" ","%20");
//    String src=space(sr);
        i.putExtra("EXTRA",sr);
        Log.e("SR", sr);
        startActivity(i);
    }

    public void res(View v){
        Intent i=new Intent(this,MainActivity.class);
        i.putExtra("EXTRA", "&type=atm");
        startActivity(i);
    }
    public void ban(View v){
        Intent i=new Intent(this,MainActivity.class);
        i.putExtra("EXTRA", "&name=rest area");
        startActivity(i);
    }
    public void hot(View v){
        Intent i=new Intent(this,MainActivity.class);
        i.putExtra("EXTRA","&name=petrol pump cng");
        startActivity(i);
    }
    public void ret(View v){
        Intent i=new Intent(this,MainActivity.class);
        i.putExtra("EXTRA","&name=retail%20store");
        startActivity(i);
    }
    public String space(ArrayList a){

        for(int i=0;i<a.size();i++){
            if(a.get(i)==" "){
                a.remove(i);
                a.add(i,"%");
                a.add(i+1,"2");
                a.add(i+2,"0");
            }
        }
        return a.toString();
    }

}
