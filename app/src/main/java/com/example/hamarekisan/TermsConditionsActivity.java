package com.example.hamarekisan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class TermsConditionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        WebView View = findViewById(R.id.webView);
        View.setWebViewClient(new MyBrowser());
        String url = "https://aashikamaddineni.blogspot.com/2022/04/hamarekisan-app_2.html".toString();
        View.getSettings().setLoadsImagesAutomatically(true);
        View.getSettings().setJavaScriptEnabled(true);
        View.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        View.loadUrl(url);
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}