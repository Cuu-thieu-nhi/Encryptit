package com.example.encryptit.view.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.example.encryptit.R;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        WebView webView = (WebView) findViewById(R.id.webview);

        webView.loadUrl("file:///android_asset/guide.html");
    }
}