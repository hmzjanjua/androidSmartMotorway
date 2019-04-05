package com.fwo.hp.fwo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Recharge extends AppCompatActivity {
Button btn_scratch,btn_recharge;
    TextView txt1,txt2;
    boolean check = false;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        btn_scratch=(Button)findViewById(R.id.button6);
        btn_recharge=(Button)findViewById(R.id.button7);
        txt1=(TextView) findViewById(R.id.textView7);
        txt2=(TextView)findViewById(R.id.textView8);
       /* relativeLayout=(RelativeLayout)findViewById(R.id.activity_recharge);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check==false) {
                    check = true;
                    txt1.setText("اہم نوٹ");
                    txt2.setText("آپ کی فراہم کردہ تمام معلومات درست ہو جائیں گے یا پھر ہوشیار موٹر ویز آپ کے ایم ٹی اکاؤنٹ کو بلاک کریں.");
                }
                else {
                    txt1.setText("Important Note");
                    txt2.setText("All the information you provided will be correct otherwise smart motorways block your M.Tag account.");
                    check=false;
                }
            }
        });*/

        btn_scratch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Recharge.this,"All above services will be shortly available.",Toast.LENGTH_LONG).show();
               /* Intent intent=new Intent(getApplicationContext(),Scratch.class);
                startActivity(intent);*/
            }
        });
        btn_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Recharge.this,"All above services will be shortly available.",Toast.LENGTH_LONG).show();
               /* Intent intent=new Intent(getApplicationContext(),Webview.class);
                startActivity(intent);*/
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_navigation_drawer, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_dua:
                Intent aboutIntent = new Intent(Recharge.this, safar.class);
                startActivity(aboutIntent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
