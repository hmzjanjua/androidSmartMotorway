package com.fwo.hp.fwo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class About extends AppCompatActivity {
TextView txt1,txt2,txt3;
    boolean check = false;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        txt1=(TextView)findViewById(R.id.txt);
        txt2=(TextView)findViewById(R.id.txt1);
        txt3=(TextView)findViewById(R.id.txt2);

        relativeLayout=(RelativeLayout)findViewById(R.id.activity_about);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check==false) {
                    check = true;
                    txt1.setText("ہمارے بارے میں");
                    txt2.setText("اسمارٹ مووم ویز ملٹی لیشنز مفت فلو (ایم ایل ایف) ٹریفک اور انٹیلجنٹ ٹرانسمیشن سسٹم کا حتمی مستقبل ہیں. مکمل، محفوظ، محفوظ اور آرام دہ اور پرسکون سفر کے تجربے کے ساتھ ہمارے قیمتی ہموار سفر ٹول پلازاس کے ذریعے عمدہ سفر کی عمدہ اور اہم دیکھ بھال کی تلاش میں.");
                    txt3.setText("نجی گاڑیاں: تمام قسم کی گاڑیاں مفت کی جا رہی ہیں. 30 جنوری 2018 تک 30 جنوری کو رجسٹریشن کریں گے، ابتدائی چارج / 300 روپے کے توازن کے ساتھ اور ہر 5000 کلو میٹر سفر کے بعد 500 روپے کے ریچارج کا حوصلہ افزائی کریں.");
                }
                else {
                    txt1.setText("About Us :");
                    txt2.setText("Smart Motorways are the ultimate future of Multi Lanes Free Flow(MLFF) traffic and Intelligent Transport System.In the quest of Excellence and prime care of our valuable seamless travel through toll plazas with complete,Safe,Secure and Comfortable travelling experience.");
                    txt3.setText("Private Vehicles : All such type of vehicles are being offered free.M.Tag registration till 30th of January 2018 , with initial charge/balance of Rs.300 and incentive of recharge Rs.500 after every 5000 km travelling.");
                check=false;
                }
            }
        });
      /*  if (check == true) {

txt1.setText("About Us :");
            txt2.setText("Smart Motorways are the ultimate future of Multi Lanes Free Flow(MLFF) traffic and Intelligent Transport System.In the quest of Excellence and prime care of our valuable seamless travel through toll plazas with complete,Safe,Secure and Comfortable travelling experience.");
            txt3.setText("Private Vehicles : All such type of vehicles are being offered free.M.Tag registration till 30th of January 2018 , with initial charge/balance of Rs.300 and incentive of recharge Rs.500 after every 5000 km travelling.");

        }
        else {

        }*/
    }
}
