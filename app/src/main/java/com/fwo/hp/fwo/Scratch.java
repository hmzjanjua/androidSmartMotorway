package com.fwo.hp.fwo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Scratch extends AppCompatActivity {
Button btn_scracth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch);
        btn_scracth=(Button)findViewById(R.id.button8);
        btn_scracth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Navigation.class);
                SmsManager smsManager =     SmsManager.getDefault();
                smsManager.sendTextMessage("03002426173", null, "500 rupees added to your M.Tag account.Thanks for using our services", null, null);
                Toast.makeText(Scratch.this,"Verification message sent to your mobile",Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });
    }
}
