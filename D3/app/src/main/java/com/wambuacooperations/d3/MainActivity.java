package com.wambuacooperations.d3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView=findViewById(R.id.webview);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient()); //To open in the WebView instead of the default browser

        webView.loadUrl("https://www.goal.com/en");
//        webView.loadUrl("file:///asset/index.html");
//        webView.loadData("<html><body><h2><strong>Wassuup!</strong></h2></body></html>","text/html","UTF-8");

    }
}
