package com.mobilesutra.iiser_tirupati.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.mobilesutra.iiser_tirupati.Fragment.Fragment_Notice;
import com.mobilesutra.iiser_tirupati.R;

public class Notice_pdf extends AppCompatActivity {


    String url;
    WebView webView;
    boolean loadingFinished = true;
    boolean redirect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);


        webView = (WebView) findViewById(R.id.nyc_poi_webview);

        receiveData();


    /*    android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction t= manager.beginTransaction();
        Fragment_Notice m= new Fragment_Notice();
        t.add(R.id.linear,m);
        t.commit();*/
    }

/*
    public void fi(String pdfUrl) {


        String str = pdfUrl;

        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();


    }
*/



    private void receiveData()
    {
        //RECEIVE DATA VIA INTENT
        Intent i = getIntent();
        String name = i.getStringExtra("NAME_KEY");

        Toast.makeText(this,name , Toast.LENGTH_SHORT).show();


        showPdf1(name);
    }


    public void showPdf1(String path){
        //new WebView(getApplicationContext());
       // webView.setVisibility(View.VISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        String googleLink = "http://docs.google.com/gview?embedded=true&url=";
        webView.loadUrl(googleLink + path);

      //  ttps://docs.google.com/viewerng/viewer?url=http://yourfile.pdf
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                if(!redirect){
                    loadingFinished = true;
                }

            }
        });
    }

}

