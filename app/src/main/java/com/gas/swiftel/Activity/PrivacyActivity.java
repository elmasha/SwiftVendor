package com.gas.swiftel.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.gas.swiftel.R;

public class PrivacyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        WebView web= findViewById(R.id.web1);
        web.loadUrl("file:///android_asset/privacy.html");


    }
}
