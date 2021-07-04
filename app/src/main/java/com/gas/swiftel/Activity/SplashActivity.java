package com.gas.swiftel.Activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.gas.swiftel.R;

public class SplashActivity extends AppCompatActivity {


    private TextView textView;
    private ImageView splashImage,splashLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        textView = findViewById(R.id.SPlashTxt);
        splashLogo = findViewById(R.id.Splashlogo);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        splashLogo.startAnimation(myanim);
        final Intent i = new Intent(this,WalkthroughActivity.class);
        Thread timer =new Thread(){
            public void run (){
                try {
                    sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }


        };
        timer.start();


    }
}
