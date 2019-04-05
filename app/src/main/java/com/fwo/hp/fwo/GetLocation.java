package com.fwo.hp.fwo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class GetLocation extends AppCompatActivity {

    RadioGroup group;
    Spinner spinner;
    EditText cityInput;
    String woeid;
    String cityName;
    int resultCode = 1;
    View v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);
        cityInput = (EditText)findViewById(R.id.editText_city);
        cityInput.setText(getIntent().getStringExtra("city"));
        resultCode = 2;
        setTitle("LẤY ĐỊA ĐIỂM");
        woeid = "1581130"; //mac dinh la ha noi
        //Lay cac view
Intent intent=new Intent();
        woeid = cityInput.getText().toString();
        //Tro ve main activity

        intent.putExtra("woeid",woeid);
       // getLocation(v);
        setResult(resultCode, intent);
        finish(); //ket thuc




    }








    //Su kien click cua button
    public void getLocation(View v)
    {
        v=v;
        if (resultCode == 2)
            resultCode = 2;
            woeid = cityInput.getText().toString();

        //Tro ve main activity
        Intent output = new Intent();
        output.putExtra("woeid",woeid);

        setResult(resultCode, output);
        finish(); //ket thuc
    }
}
