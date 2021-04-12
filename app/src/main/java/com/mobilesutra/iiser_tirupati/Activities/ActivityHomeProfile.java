package com.mobilesutra.iiser_tirupati.Activities;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TabHost;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Error_Activity.ExceptionHandler;
import com.mobilesutra.iiser_tirupati.R;

/**
 * Created by satish on 20/05/2016.
 */
public class ActivityHomeProfile extends TabActivity implements TabHost.OnTabChangeListener {

    TabHost host = null;
    int tab_changed_img[] = {R.drawable.img_notice, R.drawable.img_exam_schedule, R.drawable.img_assign,
            R.drawable.img_timetable, R.drawable.img_profile};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_tabhost_footer);

        host = (TabHost) findViewById(android.R.id.tabhost);
        host.setup();
       /* Resources ressources = getResources();
        TabHost host = getTabHost();*/

        TabHost.TabSpec tab1 = host.newTabSpec("First Tab");
        TabHost.TabSpec tab2 = host.newTabSpec("Second Tab");
        TabHost.TabSpec tab3 = host.newTabSpec("Third Tab");
        TabHost.TabSpec tab4 = host.newTabSpec("Forth Tab");
        TabHost.TabSpec tab5 = host.newTabSpec("Fifth Tab");
        /*TabHost.TabSpec tab6 = host.newTabSpec("Sixth Tab");*/

        // tab1.setIndicator("Tab1");

        tab1.setContent(new Intent(this, ActivityNotice.class));
        tab1.setIndicator(null, getResources().getDrawable(R.drawable.img_notice));

        // tab2.setIndicator("Tab2");
        tab2.setContent(new Intent(this, ActivityExamSchedule.class));
        tab2.setIndicator(null, getResources().getDrawable(R.drawable.img_exam_schedule));

        // tab3.setIndicator("Tab3");
        tab3.setContent(new Intent(this, ActivityAssignment.class));
        tab3.setIndicator(null, getResources().getDrawable(R.drawable.img_assign));

        // tab4.setIndicator("Tab4");
        tab4.setContent(new Intent(this, ActivityTimeTable.class));
        tab4.setIndicator(null, getResources().getDrawable(R.drawable.img_timetable));

        // tab5.setIndicator("Tab5");
        tab5.setContent(new Intent(this, ActivityProfile.class));
        tab5.setIndicator(null, getResources().getDrawable(R.drawable.img_profile));


        host.addTab(tab1);
        host.addTab(tab2);
        host.addTab(tab3);
        host.addTab(tab4);
        host.addTab(tab5);

        host.setCurrentTab(4);
        host.getTabWidget().getChildTabViewAt(4).setSelected(true);
        /*host.getTabWidget().getChildAt(host.getCurrentTab())
                .setBackgroundResource(R.color.tab_back_color);*/

        IISERApp.log("IISER", "host.getTabWidget().getChildCount():"+host.getTabWidget().getChildCount());
        for (int i = host.getTabWidget().getChildCount()-1; i >=0; i--) {
            IISERApp.log("IISER","In for loop");
            if (host.getTabWidget().getChildAt(i).isSelected()) { // selected
                // tab
               /* host.getTabWidget().getChildAt(i)
                        .setBackgroundResource(R.drawable.tab_background);*/
                IISERApp.log("IISER","In if of for loop");
                ImageView yourImage = (ImageView) host.getTabWidget().getChildTabViewAt(i).findViewById(android.R.id.icon);
                yourImage.setImageResource(tab_changed_img[i]);
                host.getTabWidget().getChildAt(i)
                        .setBackgroundResource(R.color.tab_onclick_color);

            } else {
                IISERApp.log("IISER","In else of for loop");
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


