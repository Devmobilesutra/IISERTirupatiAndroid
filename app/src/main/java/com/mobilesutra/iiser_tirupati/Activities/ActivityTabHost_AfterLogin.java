package com.mobilesutra.iiser_tirupati.Activities;

import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TabHost;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.R;

/**
 * Created by kalyani on 26/04/2016.
 */
public class ActivityTabHost_AfterLogin extends TabActivity implements TabHost.OnTabChangeListener {

    TabHost host = null;
    int tab_changed_img[] = {R.drawable.img_home, R.drawable.img_overview, R.drawable.img_faculty_profile, R.drawable.img_courses_offered,
            R.drawable.img_admission_process, R.drawable.img_events};

    String LOG_TAG = "ActivityTabHost_AfterLogin", activity_name = "";
    RecyclerView recycler_view = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
     // Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_tabhost);

        IISERApp.log(LOG_TAG, " onCreate()");

        getIntentData();
        loadTabhost();
       // Toast.makeText(ActivityTabHost_AfterLogin.this, "User Logged In Successfully ...", Toast.LENGTH_SHORT).show();
        Snackbar.make(host,"User Logged In Successfully...", Snackbar.LENGTH_LONG).show();
    }

    private void loadTabhost() {
        host = (TabHost) findViewById(android.R.id.tabhost);
        host.setup();
       /* Resources ressources = getResources();
        TabHost host = getTabHost();*/

        TabHost.TabSpec tab1 = host.newTabSpec("First Tab");
        TabHost.TabSpec tab2 = host.newTabSpec("Second Tab");
        TabHost.TabSpec tab3 = host.newTabSpec("Third Tab");
        TabHost.TabSpec tab4 = host.newTabSpec("Forth Tab");
        TabHost.TabSpec tab5 = host.newTabSpec("Fifth Tab");
        TabHost.TabSpec tab6 = host.newTabSpec("Sixth Tab");

        // tab1.setIndicator("Tab1");
        Intent intent = new Intent(this, ActivityHome.class);

        if(!TextUtils.isEmpty(activity_name)) intent.putExtra(IISERApp.NOTIFICATION_FLAG, activity_name);
        tab1.setContent(intent);
        tab1.setIndicator(null, getResources().getDrawable(R.drawable.img_overview));
        // tab1.setIndicator("Tab1");
        tab2.setContent(new Intent(this, ActivityAboutUs.class));
        tab2.setIndicator(null, getResources().getDrawable(R.drawable.img_overview));

        // tab2.setIndicator("Tab2");
        tab3.setContent(new Intent(this, ActivityFacultyProfile.class));
        tab3.setIndicator(null, getResources().getDrawable(R.drawable.img_faculty_profile));

        // tab3.setIndicator("Tab3");
        tab4.setContent(new Intent(this, ActivityCourses.class));
        tab4.setIndicator(null, getResources().getDrawable(R.drawable.img_courses_offered));

        // tab4.setIndicator("Tab4");
        tab5.setContent(new Intent(this, ActivityAdmissionProcess.class));
        tab5.setIndicator(null, getResources().getDrawable(R.drawable.img_admission_process));

        // tab5.setIndicator("Tab5");
        tab6.setContent(new Intent(this, ActivityEvents.class));
        tab6.setIndicator(null, getResources().getDrawable(R.drawable.img_events));

        // tab6.setIndicator("Tab6");
       /* tab6.setContent(new Intent(this, ActivityLogin.class));
        tab6.setIndicator(null, getResources().getDrawable(R.drawable.img_login));*/

        host.addTab(tab1);
        host.addTab(tab2);
        host.addTab(tab3);
        host.addTab(tab4);
        host.addTab(tab5);
        host.addTab(tab6);

        for (int i = 0; i < host.getTabWidget().getChildCount(); i++) {
            if (host.getTabWidget().getChildAt(i).isSelected()) { // selected
                // tab
               /* host.getTabWidget().getChildAt(i)
                        .setBackgroundResource(R.drawable.tab_background);*/
                ImageView yourImage = (ImageView) host.getTabWidget().getChildTabViewAt(i).findViewById(android.R.id.icon);
                yourImage.setImageResource(tab_changed_img[i]);
                host.getTabWidget().getChildAt(i)
                        .setBackgroundResource(R.color.tab_onclick_color);
            } else {
                // unselected tabs //
                // tabHost.setBackgroundColor(Color.parseColor("#336986"));
               /* host.getTabWidget().getChildAt(i)
                        .setBackgroundResource(R.drawable.tab_background);*/

               /* ImageView yourImage = (ImageView) host.getTabWidget().getChildTabViewAt(host.getCurrentTab()).findViewById(android.R.id.icon);
                yourImage.setImageResource(R.drawable.img_overview144);*/
                host.getTabWidget().getChildAt(host.getCurrentTab())
                        .setBackgroundResource(R.color.tab_back_color);
            }
        }

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String arg0) {
                // TODO Auto-generated method stub
                for (int i = 0; i < host.getTabWidget().getChildCount(); i++) {
                   /* host.getTabWidget().getChildAt(i)
                            .set(R.drawable.img1_overview);*/

                    ImageView yourImage = (ImageView) host.getTabWidget().getChildTabViewAt(i).findViewById(android.R.id.icon);
                    yourImage.setImageResource(tab_changed_img[i]);
                    //
                    host.getTabWidget().getChildAt(i)
                            .setBackgroundResource(R.color.tab_onclick_color);
                }

               /* ImageView yourImage = (ImageView) host.getTabWidget().getChildTabViewAt(host.getCurrentTab()).findViewById(android.R.id.icon);
                yourImage.setImageResource(R.drawable.img_overview144);*/
                host.getTabWidget().getChildAt(host.getCurrentTab())
                        .setBackgroundResource(R.color.tab_back_color);
            }
        });
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if(intent.hasExtra(IISERApp.NOTIFICATION_FLAG))
        activity_name = intent.getStringExtra(IISERApp.NOTIFICATION_FLAG);
        IISERApp.log(LOG_TAG, "activity_name:" + activity_name);
    }

    @Override
    public void onTabChanged(String tabId) {


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        IISERApp.log(LOG_TAG, "new intent");
        Bundle extras = intent.getExtras();
        IISERApp.log_bundle(extras);
        if(extras != null) {
            activity_name = extras.getString(IISERApp.NOTIFICATION_FLAG);
            IISERApp.log(LOG_TAG, "activity_name:" + activity_name);
            if (activity_name.equalsIgnoreCase("Event")) {
                host.setCurrentTab(5);
            } else {
                host.setCurrentTab(0);
                IISERApp.set_session(IISERApp.SESSION_NOTIFICATION_FLAG, activity_name);
            }
        }
        // getIntentData();
        // loadTabhost();
       // Toast.makeText(this, "onNewIntent", Toast.LENGTH_SHORT).show();
        //Toast.makeText(ActivityTabHost_AfterLogin.this, "User Logged In Successfully ...", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IISERApp.log(LOG_TAG, "ondestroy");
        this.registerReceiver(mMessageReceiver, new IntentFilter(LOG_TAG));
        this.unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();

        IISERApp.log(LOG_TAG, "Awak onresume-->");
        this.registerReceiver(mMessageReceiver, new IntentFilter(LOG_TAG));
    }

    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            IISERApp.log(LOG_TAG, "messege recieved");
            Bundle bundle = intent.getExtras();
        }
    };

}
