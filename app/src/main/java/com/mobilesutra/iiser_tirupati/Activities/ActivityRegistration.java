package com.mobilesutra.iiser_tirupati.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.mobilesutra.iiser_tirupati.Error_Activity.ExceptionHandler;
import com.mobilesutra.iiser_tirupati.R;

/**
 * Created by kalyani on 27/04/2016.
 */
public class ActivityRegistration extends Activity {

    Context context=null;
    Intent intent=null;
    ImageButton back=null;
    FloatingActionButton fab = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_registration);

        context = this;
        getIntentData();
        initComponents();
        initComponentsListeners();
        bindComponentData();
    }

    private void getIntentData() {
    }

    private void initComponents() {
        back= (ImageButton) findViewById(R.id.img_back);
       /* fab = (FloatingActionButton)findViewById(R.id.fab);*/
    }

    private void initComponentsListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    Log.i("MainActivity", "popping backstack");
                    fm.popBackStack();
                } else {
                    Log.i("MainActivity", "nothing on backstack, calling super");

                }
                finish();
            }
        });

       /* fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent=new Intent(ActivityRegistration.this,ActivityCourseSelection.class);
                startActivity(intent);

            }
        });*/

    }

    private void bindComponentData() {
    }
}
