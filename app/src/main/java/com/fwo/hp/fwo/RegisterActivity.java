package com.fwo.hp.fwo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fwo.hp.fwo.helper.Functions;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by Akshay Raj on 6/16/2016.
 * akshay@snowcorp.org
 * www.snowcorp.org
 */

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    private Button btnRegister, btnLinkToLogin;
    private EditText inputName, inputEmail, inputPassword,inputPhone;
    private ProgressDialog pDialog;
    RadioGroup rg;
    EditText edt_vehicle;
RadioButton radioButton_travel,radioButton2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputName = (EditText) findViewById(R.id.rTextName);
        inputEmail = (EditText) findViewById(R.id.rTextEmail);
        inputPassword = (EditText) findViewById(R.id.rTextPassword);
        rg = (RadioGroup) findViewById(R.id.rTextPhone);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        edt_vehicle=(EditText)findViewById(R.id.edt_cell);
        radioButton_travel=(RadioButton)findViewById(R.id.radioButton_travel);
        radioButton2=(RadioButton)findViewById(R.id.radioButton2);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();
    }

    private void init() {
        radioButton_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_vehicle.setVisibility(View.VISIBLE);
            }
        });
        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_vehicle.setVisibility(View.GONE);
            }
        });
        // Login button Click Event
        btnRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                // Hide Keyboard
                Functions.hideSoftKeyboard(RegisterActivity.this);

                String name = inputName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String phone = ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();
                String vehicle=edt_vehicle.getText().toString().trim();

                // Check for empty data in the form
                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !phone.isEmpty() ) {
                    if (Functions.isValidEmailAddress(email)) {
                        registerUser(name, email, password, phone,vehicle);
                        Toast.makeText(RegisterActivity.this,"resgistered successfully",Toast.LENGTH_LONG).show();
                       /* Intent intent=new Intent(getApplicationContext(),EmailVerify.class);
                        startActivity(intent);*/
                    } else {
                        Toast.makeText(getApplicationContext(), "Email is not valid!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);

            }
        });
    }

    private void registerUser(final String name, final String email, final String password,final  String phone,final String vehicle) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.REGISTER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Functions logout = new Functions();
                        logout.logoutUser(getApplicationContext());

                        Bundle b = new Bundle();
                        b.putString("email", email);
                        Intent i = new Intent(RegisterActivity.this, EmailVerify.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtras(b);
                        startActivity(i);
                        pDialog.dismiss();
                        finish();

                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("phone", phone);
                params.put("cell", vehicle);

                return params;
            }

        };

        // Adding request to request queue
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
