package com.mobilesutra.iiser_tirupati.Activities;

import android.app.Activity;
import android.os.Bundle;

import com.mobilesutra.iiser_tirupati.Error_Activity.ExceptionHandler;
import com.mobilesutra.iiser_tirupati.R;

/**
 * Created by kalyani on 14/04/2016.
 */
public class ActivityAssignmntDetails extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_assignment_details);

        initComponents();
        initComponentsListeners();
        bindComponentData();
    }
    private void initComponents() {
    }

    private void initComponentsListeners() {
    }

    private void bindComponentData() {
    }

}
