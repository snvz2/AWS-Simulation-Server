package edu.sjsu.teamneon.hangrymobile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;
/**
 * Created by t_zarun on 5/9/2017.
 */

public class TruckUIWeb extends AppCompatActivity {
    public WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_ui2);

        demo2TryLocallyStoredHtmlPage();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void demo2TryLocallyStoredHtmlPage() {
        webview = (WebView) findViewById(R.id.webView1);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setBackgroundColor(0x00000000);
        webview.setHorizontalScrollBarEnabled(false);
        webview.setVerticalScrollBarEnabled(false);

        webview.getSettings().setBuiltInZoomControls(true);
        // a custom WebViewClient could check for url containing the
        // home-base address "file:///android_asset/" webview.setWebViewClient(new WebViewClient());
        // continue using  WebViews
        //webview.setWebViewClient(new MyWebViewClient2(this)); // not for local URls
        //======= Remote URLs work OK:  =======================
        // webview.setWebViewClient(new WebViewClient()); //try later
        //webview.loadUrl("http://www.amazon.com"); // OK
        //====================================================

        //webview.loadUrl("https://youtu.be/n38wfAp_mHw?t=104");//("https://youtu.be/n38wfAp_mHw");
        //webview.setWebViewClient(new MyWebViewClient(txtMsg, "ebay.com"));
        //webview.loadUrl("http://www.ebay.com");

        //========= Works OK for local URLs ======
        webview.loadUrl("file:///android_asset/webpage.html");
        //=======================================

    }

}

