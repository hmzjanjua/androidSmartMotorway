package com.fwo.hp.fwo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fwo.hp.fwo.helper.DatabaseHandler;
import com.fwo.hp.fwo.helper.Functions;
import com.fwo.hp.fwo.helper.SessionManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Query_area extends AppCompatActivity {
TextView textView_name,txt_address;
    String ServerURL = "http://www.fwosmartmotorways.com/server_side_php/get_data.php" ;
    EditText query ;
    RadioGroup r_rate;
    Button button;
    String TempName, TempEmail ,t_n,t_p,place_name,place_address,location_adreess;
    String name1,email1,l_address;
    private SessionManager session;
    private DatabaseHandler db;
    private HashMap<String,String> user = new HashMap<>();
    private static final String TAG = Query_area.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_area);
        db = new DatabaseHandler(getApplicationContext());
        user = db.getUserDetails();

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }
        textView_name=(TextView)findViewById(R.id.txt_name);
        textView_name.setText(getIntent().getStringExtra("NAME"));
        txt_address=(TextView)findViewById(R.id.txt_address);
        txt_address.setText(getIntent().getStringExtra("ADDRESS"));

        r_rate = (RadioGroup) findViewById(R.id.editText2);
        query = (EditText)findViewById(R.id.editText3);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetData();

                InsertData(TempName, TempEmail,t_n,t_p,place_name,place_address,location_adreess);
                Intent intent=new Intent(getApplicationContext(),Navigation.class);
                startActivity(intent);

            }
        });
        // Fetching user details from database
        name1 = user.get("name");
        email1 = user.get("email");
//Toast.makeText(Query.this,name1,Toast.LENGTH_LONG).show();
        // Displaying the user details on the screen


        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    private void logoutUser() {
        session.setLogin(false);
        // Launching the login activity
        Functions logout = new Functions();
        logout.logoutUser(getApplicationContext());
        Intent intent = new Intent(Query_area.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void GetData(){

        TempName = ((RadioButton)findViewById(r_rate.getCheckedRadioButtonId())).getText().toString();

        TempEmail = query.getText().toString();
        t_n=name1;
        t_p=email1;

        place_name=getIntent().getStringExtra("NAME");
        place_address=getIntent().getStringExtra("ADDRESS");
        location_adreess=QMainActivity.l_address;
    }

    public void InsertData(final String name, final String email,final String n,final String p,final String p_name,final String p_add,final String p_nameloca){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String NameHolder = name ;
                String EmailHolder = email ;
                String NHolder=n;
                String PHolder=p;
                String PuHolder=p_name;
                String PaHolder=p_add;
                String PlHolder=p_nameloca;


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("rate", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("query_msg", EmailHolder));
                nameValuePairs.add(new BasicNameValuePair("name", NHolder));
                nameValuePairs.add(new BasicNameValuePair("phone", PHolder));
                nameValuePairs.add(new BasicNameValuePair("place_name", PuHolder));
                nameValuePairs.add(new BasicNameValuePair("place_address", PaHolder));
                nameValuePairs.add(new BasicNameValuePair("user_address", PlHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ServerURL);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "Query Sent Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

                Toast.makeText(Query_area.this, "Query Sent Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(name, email);
    }
}
