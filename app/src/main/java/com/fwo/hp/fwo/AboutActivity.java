package com.fwo.hp.fwo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        setTitle("VỀ TÔI");
    }

    class MyTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            return null;
        }
    }
}
