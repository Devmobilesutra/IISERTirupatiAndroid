package com.mobilesutra.iiser_tirupati.Activities;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TabHost;

import com.mobilesutra.iiser_tirupati.Error_Activity.ExceptionHandler;
import com.mobilesutra.iiser_tirupati.R;

/**
 * Created by satish on 20/05/2016.
 */
public class ActivityTabHst_AftrLogin1  extends TabActivity implements TabHost.OnTabChangeListener {

    TabHost host = null;
    int tab_changed_img[] = {R.drawable.img_home, R.drawable.img_overview, R.drawable.img_faculty_profile, R.drawable.img_courses_offered,
            R.drawable.img_admission_process, R.drawable.img_events};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_tabhost);

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
        tab1.setContent(new Intent(this, ActivityHomeProfile.class));
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

    @Override
    public void onTabChanged(String tabId) {


    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

}
