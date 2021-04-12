package com.mobilesutra.iiser_tirupati.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Error_Activity.ExceptionHandler;
import com.mobilesutra.iiser_tirupati.R;

/**
 * Created by kalyani on 15/04/2016.
 */
public class ActivityCourseDetails extends Activity {

    String[] asset_array={"file:///android_asset/BSMSProgramme.html","file:///android_asset/Integrated_PhD_Programme.html"
            ,"file:///android_asset/Ph.html"};
    WebView wbvw_course=null;
    int position=0;
    TextView user_name=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_course_details);

        getIntentData();
        initComponents();
        initComponentsListeners();
        bindComponentData();
        initComponentData();
    }

    private void initComponentData() {

        user_name=(TextView)findViewById(R.id.user_name);
        //  IISERApp.log(LOG_TAG,"session value is"+IISERApp.get_session(IISERApp.SESSION_LOGIN_FLAG));
        if((IISERApp.get_session(IISERApp.SESSION_LOGIN_FLAG).equalsIgnoreCase("Y"))) {

            String user = IISERApp.get_session((IISERApp.SESSION_STUDENT_NAME));
            String sem_name = IISERApp.get_session((IISERApp.SESSION_SEMESTER_NAME));

            // String sem = IISERApp.get_session((IISERApp.SESSION_SEMESTER_NAME ));

            //user_name.setText(user);
            if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("student"))
            {
                user_name.setText(user + " (sem- " + sem_name + ")");
            }
            else  if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("faculty"))
            {
                user_name.setText(user + " (" + "Faculty" +")");
            }
            else  if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("supervisor"))
            {
                user_name.setText(user + " (" + "Invigilator" +")");
            }
        }
        else
        {
            user_name.setVisibility(View.GONE);
        }
    }




    private void getIntentData() {
        Intent intent=getIntent();
        position = Integer.parseInt(intent.getStringExtra("position"));
        Log.d("Fragment", "position="
                + position);

    }

    private void initComponents() {
        wbvw_course= (WebView) findViewById(R.id.webview_course);
    }

    private void initComponentsListeners() {
    }

    private void bindComponentData() {
        wbvw_course.getSettings().setJavaScriptEnabled(true);
        wbvw_course.getSettings().setPluginState(WebSettings.PluginState.ON);

        wbvw_course.loadUrl(asset_array[position].replaceAll(" ", "%20"));
    }

}