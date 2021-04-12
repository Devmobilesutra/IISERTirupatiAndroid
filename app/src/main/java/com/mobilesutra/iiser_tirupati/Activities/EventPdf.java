package com.mobilesutra.iiser_tirupati.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Fragment.Fragment_Notice;
import com.mobilesutra.iiser_tirupati.R;

public class EventPdf extends AppCompatActivity {
    String url;
    WebView webView;

    boolean loadingFinished = true;
    boolean redirect = false;
    ProgressDialog pDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);



        webView = (WebView) findViewById(R.id.nyc_poi_webview);

        Intent i= getIntent();
        Bundle b = i.getExtras();

        if(b!=null)
        {
            url = b.getString("pdfurl");

            Log.d("", "onCreate11111: "+url);

        }else{

            Toast.makeText(this, "No pdf ", Toast.LENGTH_SHORT).show();
        }


        showPdf1(url);


    }



    public void showPdf1(String path){
            //new WebView(getApplicationContext());
        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        String googleLink = "http://docs.google.com/gview?embedded=true&url=";
        webView.loadUrl(googleLink + path);

        ttps://docs.google.com/viewerng/viewer?url=http://yourfile.pdf
        webView.setWebViewClient(new WebViewClient() {


/*
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                if (!loadingFinished) {
                    redirect = true;
                }

                loadingFinished = false;
                view.loadUrl(urlNewString);
                return true;
            }*/

          /*  @Override
            public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                loadingFinished = false;
                //SHOW LOADING IF IT ISNT ALREADY VISIBLE
                if (pDialog != null)
                    if (pDialog.isShowing())
                        pDialog.dismiss();

                pDialog = ProgressDialog.show(getApplicationContext(), getResources().getString(R.string.app_name),
                        "Loading...", false, false);
                pDialog.setCancelable(false);
            }*/

            @Override
            public void onPageFinished(WebView view, String url) {
                if(!redirect){
                    loadingFinished = true;
                }

               /* if(loadingFinished && !redirect){
                    //HIDE LOADING IT HAS FINISHED
                //    pDialog.dismiss();

                } else{
                    redirect = false;
                }*/
            }
        });
    }



}
