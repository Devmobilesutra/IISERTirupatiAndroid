package com.mobilesutra.iiser_tirupati.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Error_Activity.ExceptionHandler;
import com.mobilesutra.iiser_tirupati.R;
import com.mobilesutra.iiser_tirupati.banner.Animations.DescriptionAnimation;
import com.mobilesutra.iiser_tirupati.banner.SliderLayout;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.BaseSliderView;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.TextSliderView;
import com.mobilesutra.iiser_tirupati.banner.Tricks.ViewPagerEx;

import org.jsoup.Jsoup;

public class ActivityAboutUs extends Activity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    private static final String ARG_PARAM_TITLE = "title", LOG_TAG = "ActivityAboutUs";
    Context context = null;
    SliderLayout mDemoSlider = null;
    TextView add_process = null;
    TextView user_name = null;
    WebView webView;

    String newVersion = "";
    boolean active = true;
    final int splashTime = 6000; // time to display the splash screen in m

    // TextView user_type1=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.fragment_one);
        context = this;
        check_for_upgrade();
        getIntentData();
        initComponents();
        initComponentsListeners();
        displayBanner();
        bindComponentData();
        initComponentData();
        final ScrollView main = (ScrollView) findViewById(R.id.scrollView123);
        main.post(new Runnable() {
            public void run() {
                main.scrollTo(0, 0);
            }
        });
    }

    private void initComponentData() {
        add_process = (TextView) findViewById(R.id.add_process);
        add_process.setVisibility(View.GONE);
        webView = (WebView) findViewById(R.id.webView);

        String text;
        text = "<html><body><p align=\"justify\">";
        text += getResources().getString(R.string.txt_about_us_value1) + "</p><p align=\"justify\">" + getResources().getString(R.string.txt_about_us_value2);
        text += "</p></body></html>";
        webView.loadData(text, "text/html", "utf-8");

        // user_type=(TextView)findViewById(R.id.user_type);
      /*if((IISERApp.get_session(IISERApp.SESSION_LOGIN_FLAG).equalsIgnoreCase("Y")))
        {
            String user = IISERApp.get_session((IISERApp.SESSION_USERNAME ));
            String sem = IISERApp.get_session((IISERApp.SESSION_SEMESTER_NAME ));
            user_type.setText(user + sem);
        }else
      {
          user_type.setVisibility(View.GONE);
      }*/
        user_name = (TextView) findViewById(R.id.user_name);
        IISERApp.log(LOG_TAG, "session value is" + IISERApp.get_session(IISERApp.SESSION_LOGIN_FLAG));
        if ((IISERApp.get_session(IISERApp.SESSION_LOGIN_FLAG).equalsIgnoreCase("Y"))) {

            String user = IISERApp.get_session((IISERApp.SESSION_STUDENT_NAME));
            String sem_name = IISERApp.get_session((IISERApp.SESSION_SEMESTER_NAME));

            // String sem = IISERApp.get_session((IISERApp.SESSION_SEMESTER_NAME ));

            //user_name.setText(user);
            if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("student")) {
                user_name.setText(user + " (sem- " + sem_name + ")");
            } else if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("faculty")) {
                user_name.setText(user + " (" + "Faculty" + ")");
            } else if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("supervisor")) {
                user_name.setText(user + " (" + "Invigilator" + ")");
            }
        } else {
            user_name.setVisibility(View.GONE);
        }
    }

    private void getIntentData() {
    }

    private void initComponents() {
    }

    private void initComponentsListeners() {
    }

    private void bindComponentData() {
    }

    public void displayBanner() {
        //final ViewFlipper MyViewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);

        mDemoSlider = (SliderLayout) findViewById(R.id.sample_output);
        mDemoSlider.setFocusable(true);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(3000);
        mDemoSlider.addOnPageChangeListener(this);

       /* LinkedHashMap<String, String> lhm = ShopApp.db.getFrontBanner();
        int length = lhm.size();
        if (length == 0) {*/
        int bannerArray[] = {R.drawable.banner, R.drawable.banner_1, R.drawable.banner_3};

        for (int i = 0; i < 3; i++) {

            TextSliderView textSliderView = new TextSliderView(context);
            // initialize a SliderLayout
            textSliderView
                    .description("")
                    .image(bannerArray[i])
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", "");
            mDemoSlider.addSlider(textSliderView);
        }
        /* } else {*/

          /*  Set<String> keys = lhm.keySet();
            for (String key : keys) {
                TextSliderView textSliderView = new TextSliderView(getContext());
                // initialize a SliderLayout
                textSliderView
                        .description("")
                        .image(lhm.get(key))
                        .setScaleType(BaseSliderView.ScaleType.CenterInside)
                        .setOnSliderClickListener(this);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra", key);

                mDemoSlider.addSlider(textSliderView);
            }*/
          /*  for (int i = 0; i < length; i++) {

                TextSliderView textSliderView = new TextSliderView(this);
                // initialize a SliderLayout
                textSliderView
                        .description("")
                        .image(lhm.get("" + i))
                        .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                        .setOnSliderClickListener(this);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra", lhm.get("" + i));

                mDemoSlider.addSlider(textSliderView);
            }*/
        // }


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
    }


    void check_for_upgrade() {
        if (((IISERApp) getApplication()).isInternetAvailable()) {
            new FetchAppVersionFromGooglePlayStore().execute();
        }
    }






    class FetchAppVersionFromGooglePlayStore extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            try {

                return
                        Jsoup.connect("https://play.google.com/store/apps/details?id=" + ActivityAboutUs.this.getPackageName() + "&hl=it")
                                .timeout(30000)
                                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                                .referrer("http://www.google.com")
                                .get()
                                .select(".hAyfc .htlgb")
                                .get(7)
                                .ownText();

            } catch (Exception e) {
                return "";
            }
        }

        protected void onPostExecute(String string) {
            newVersion = string;

            Log.d("new Version", newVersion);

            if (newVersion != "") {
                float server_version = Float.parseFloat(newVersion);


                float app_version = 0.0f;
                PackageManager manager = context.getPackageManager();
                PackageInfo info = null;
                try {
                    info = manager.getPackageInfo(context.getPackageName(), 0);
                    app_version = Float.parseFloat(info.versionName);
                    //      if(value(info.versionName)<value(newVersion));
                } catch (PackageManager.NameNotFoundException e) {
                   // proceed();
                }



                if (server_version > app_version) {
                    show_upgrade_dialog();
                } else {

                  //  proceed();
                }
                //show_upgrade_dialog();


            }else{
               // proceed();
            }
        }
    }



    void show_upgrade_dialog() {
        final Dialog dialog1 = new Dialog(ActivityAboutUs.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.layout_upgrade);

        dialog1.show();
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(false);

        final TextView txt_upgrade = (TextView) dialog1.findViewById(R.id.txt_upgrade);
        final TextView txt_cancel = (TextView) dialog1.findViewById(R.id.txt_cancel);
        txt_cancel.setVisibility(View.GONE);
        txt_upgrade.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Intent intent = new Intent();
                Uri uri = Uri.parse("market://details?id=" + getApplication().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                //  To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getApplication().getPackageName())));
                }
                dialog1.dismiss();
            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                dialog1.dismiss();
                // proceed();
            }
        });
    }




}