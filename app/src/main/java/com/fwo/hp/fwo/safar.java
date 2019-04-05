package com.fwo.hp.fwo;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class safar extends AppCompatActivity {
Button btn_dua;
    MediaPlayer mPlayer;
    int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safar);
        btn_dua=(Button)findViewById(R.id.button10);
        btn_dua.setBackgroundResource(R.color.bg_login);
        btn_dua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 0) {
                    Log.v("Inside if", "Success" + "");
                    mPlayer = MediaPlayer.create(getApplicationContext(),
                            R.raw.safar);
                    mPlayer.start();
                    btn_dua.setText("play");
                    btn_dua.setBackgroundResource(R.color.colorAccent);
                    flag++;
                } else {
                    Log.v("Inside else", "Success" + "");
                    mPlayer.stop();
                    mPlayer.release();
                    btn_dua.setText("pause");
                    btn_dua.setBackgroundResource(R.color.colorPrimary);
                    flag = 0;
                }
            }
        });
    }
  /*  @Override
    public void onBackPressed() {
        mPlayer.stop();

    }*/
}
