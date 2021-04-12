

package com.mobilesutra.iiser_tirupati.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Error_Activity.ExceptionHandler;
import com.mobilesutra.iiser_tirupati.R;

import java.util.Arrays;

/**
 * Created by kalyani on 26/04/2016.
 */

public class ActivityHome extends TabActivity implements TabHost.OnTabChangeListener {

    TabHost host = null;
   /* int tab_changed_img[] = {R.drawable.img_notice, R.drawable.img_exam_schedule, R.drawable.img_assign,
            R.drawable.img_timetable, R.drawable.img_profile};
*/

    int tab_changed_img[] = {R.drawable.img_notice,  R.drawable.img_timetable, R.drawable.img_assign,
            R.drawable.img_exam_schedule, R.drawable.attendance,R.drawable.img_profile};

    String LOG_TAG = "ActivityHome", activity_name = null;
    int PERMISSION_REQUEST_VIDEO = 1, PERMISSION_REQUEST_DOCUMENT = 2;
    private int permission_count = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_tabhost_footer);

        getIntentData();
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
        /*TabHost.TabSpec tab6 = host.newTabSpec("Sixth Tab");*/

        // tab1.setIndicator("Tab1");
        tab1.setContent(new Intent(this, ActivityNotice.class));
        tab1.setIndicator(null, getResources().getDrawable(R.drawable.img_notice));


        // tab2.setIndicator("Tab2");
        tab4.setContent(new Intent(this, ActivityTimeTable.class ));
        tab4.setIndicator(null, getResources().getDrawable(R.drawable.img_timetable));

        // tab3.setIndicator("Tab3");
        tab3.setContent(new Intent(this, ActivityAssignment.class));
        tab3.setIndicator(null, getResources().getDrawable(R.drawable.img_assign));

        // tab4.setIndicator("Tab4");
        tab2.setContent(new Intent(this,ActivityExamSchedule.class));
        tab2.setIndicator(null, getResources().getDrawable(R.drawable.img_exam_schedule));

        if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("student"))
        {
            // tab5.setIndicator("Tab5");
            tab5.setContent(new Intent(this, ActivityAttendence.class));
            tab5.setIndicator(null, getResources().getDrawable(R.drawable.attendance));
        }  else if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("faculty")) {
            // tab5.setIndicator("Tab5");
            tab5.setContent(new Intent(this, ActivityAttendenceFaculty.class));
            tab5.setIndicator(null, getResources().getDrawable(R.drawable.attendance));

        }


        // tab6.setIndicator("Tab6");
        tab6.setContent(new Intent(this, ActivityProfile.class));
        tab6.setIndicator(null, getResources().getDrawable(R.drawable.img_profile));

        // tab6.setIndicator("Tab6");
        /*tab6.setContent(new Intent(this, ActivityLogin.class));
        tab6.setIndicator(null, getResources().getDrawable(R.drawable.img_login));*/

        if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("student"))
        {
            IISERApp.log(LOG_TAG, " in student type");
            host.addTab(tab1);
            host.addTab(tab4);
            host.addTab(tab3);
            host.addTab(tab2);
            host.addTab(tab5);
            host.addTab(tab6);



           /* host.addTab(tab1);
            host.addTab(tab2);
            host.addTab(tab3);
            host.addTab(tab4);
            host.addTab(tab6);*/
        }

        else if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("faculty"))
        {
            IISERApp.log(LOG_TAG, " in faculty type");
           /* host.addTab(tab1);
            host.addTab(tab4);
            host.addTab(tab2);
            host.addTab(tab5);*/

            host.addTab(tab1);
            host.addTab(tab4);
            host.addTab(tab3);
            host.addTab(tab2);
            host.addTab(tab5);
            host.addTab(tab6);
           // host.addTab(tab5);


        }

        else if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("supervisor"))
        {
            IISERApp.log(LOG_TAG, " in supervisor type");
            host.addTab(tab1);
            host.addTab(tab4);
            host.addTab(tab2);
            host.addTab(tab6);
          //  host.addTab(tab5);


        }


        /*else
            IISERApp.log(LOG_TAG, " in super");
            host.addTab(tab1);
            host.addTab(tab2);
            host.addTab(tab5);*/
        // host.addTab(tab6);
        if (IISERApp.get_session(IISERApp.SESSION_LOGIN_FLAG).equalsIgnoreCase("N")) {
            IISERApp.log(LOG_TAG, "xxxe");
            if (activity_name != null) {
                IISERApp.log(LOG_TAG, "IN if actvity_");
                if (activity_name.equalsIgnoreCase("Assignment")) {
                    ;
                    host.setCurrentTab(2);
                    host.getTabWidget().getChildTabViewAt(2).setSelected(true);
                } else if (activity_name.equalsIgnoreCase("Notice")) {
                    host.setCurrentTab(0);
                    host.getTabWidget().getChildTabViewAt(0).setSelected(true);
                } else if (activity_name.equalsIgnoreCase("Examschedule")) {
                    host.setCurrentTab(1);
                    host.getTabWidget().getChildTabViewAt(1).setSelected(true);
                } else if (activity_name.equalsIgnoreCase("Timetable")) {
                    host.setCurrentTab(3);
                    host.getTabWidget().getChildTabViewAt(3).setSelected(true);
                }
            }
            for (int i = 0; i < host.getTabWidget().getChildCount(); i++) {
                IISERApp.log(LOG_TAG, "in for int i = 0; i < host.getTabWidget().getChildCount(); i++" + host.getTabWidget().getChildCount());
                if (host.getTabWidget().getChildAt(i).isSelected()) { // selected
                    // tab
               /* host.getTabWidget().getChildAt(i)
                        .setBackgroundResource(R.drawable.tab_background);*/
                    ImageView yourImage = (ImageView) host.getTabWidget().getChildTabViewAt(i).findViewById(android.R.id.icon);
                    IISERApp.log(LOG_TAG, "login type is: " + IISERApp.get_session(IISERApp.SESSION_USER_TYPE));
                    if (i == 2 && IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("supervisor")) {
                        IISERApp.log(LOG_TAG, "IN IF i == 2");
                        yourImage.setImageResource(tab_changed_img[4]);

                    } else
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
        } else if (IISERApp.get_session(IISERApp.SESSION_LOGIN_FLAG).equalsIgnoreCase("Y")) {

            if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("student")) {
                IISERApp.log(LOG_TAG, " in student type set current tab" + host.getCurrentTab());
                host.setCurrentTab(5);
                IISERApp.log(LOG_TAG, "tab 4 is set for student");
                host.getTabWidget().getChildTabViewAt(4).setSelected(true);

            } else if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("faculty")) {
                IISERApp.log(LOG_TAG, " in faculty type  set current tab: " + host.getCurrentTab());
                host.setCurrentTab(5);
                IISERApp.log(LOG_TAG, "tab 2 is set for faculty");
                host.getTabWidget().getChildTabViewAt(4).setSelected(true);

            } else if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("supervisor")) {
                IISERApp.log(LOG_TAG, " in supervisor type  set current tab: " + host.getCurrentTab());
                host.setCurrentTab(2);
                IISERApp.log(LOG_TAG, "tab 2 is set for faculty");
                host.getTabWidget().getChildTabViewAt(2).setSelected(true);

            }
        /*host.getTabWidget().getChildAt(host.getCurrentTab())
                .setBackgroundResource(R.color.tab_back_color);*/

            IISERApp.log("IISER", "host.getTabWidget().getChildCount():" + host.getTabWidget().getChildCount());
            for (int i = host.getTabWidget().getChildCount() - 1; i >= 0; i--) {
                IISERApp.log("IISER", "In for loop");
                if (host.getTabWidget().getChildAt(i).isSelected()) { // selected
                    // tab
               /* host.getTabWidget().getChildAt(i)
                        .setBackgroundResource(R.drawable.tab_background);*/
                    IISERApp.log("IISER", "In if of for loop");
                    if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("supervisor")) {
                        IISERApp.log("IISER", "In if of for loop supervisor");
                        ImageView yourImage = (ImageView) host.getTabWidget().getChildTabViewAt(i).findViewById(android.R.id.icon);
                        yourImage.setImageResource(tab_changed_img[4]);
                    } else {
                        IISERApp.log("IISER", "In if of for loop supervisor else");
                        ImageView yourImage = (ImageView) host.getTabWidget().getChildTabViewAt(i).findViewById(android.R.id.icon);
                        yourImage.setImageResource(tab_changed_img[i]);
                    }
                    host.getTabWidget().getChildAt(i)
                            .setBackgroundResource(R.color.tab_onclick_color);

                } else {
                    IISERApp.log("IISER", "In else of for loop");
                    // unselected tabs //
                    // tabHost.setBackgroundColor(Color.parseColor("#336986"));
               /* host.getTabWidget().getChildAt(i)
                        .setBackgroundResource(R.drawable.tab_background);*/


                    host.getTabWidget().getChildAt(host.getCurrentTab())
                            .setBackgroundResource(R.color.tab_back_color);
                }
            }
        }

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener()

                                     {

                                         @Override
                                         public void onTabChanged(String arg0) {
                                             // TODO Auto-generated method stub
                                             for (int i = 0; i < host.getTabWidget().getChildCount(); i++) {
                                                 IISERApp.log("IISER", "In else of for loop  "+host.getTabWidget().getChildCount());
                   /* host.getTabWidget().getChildAt(i)
                            .set(R.drawable.img1_overview);*/

                                                 ImageView yourImage = (ImageView) host.getTabWidget().getChildTabViewAt(i).findViewById(android.R.id.icon);
                                                 yourImage.setImageResource(tab_changed_img[i]);

                                                 if (i == 2 && IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("supervisor")) {
                                                     yourImage.setImageResource(tab_changed_img[4]);


                                                 } else
                                                     yourImage.setImageResource(tab_changed_img[i]);
                                                 host.getTabWidget().getChildAt(i)
                                                         .setBackgroundResource(R.color.tab_onclick_color);
                                             }

               /* ImageView yourImage = (ImageView) host.getTabWidget().getChildTabViewAt(host.getCurrentTab()).findViewById(android.R.id.icon);
                yourImage.setImageResource(R.drawable.img_overview144);*/
                                             host.getTabWidget().getChildAt(host.getCurrentTab())
                                                     .setBackgroundResource(R.color.tab_back_color);
                                         }
                                     }

        );
    }

    @Override
    public void onTabChanged(String tabId) {

    }

    private void getIntentData() {
        Intent intent = getIntent();
        activity_name = intent.getStringExtra(IISERApp.NOTIFICATION_FLAG);
        IISERApp.log(LOG_TAG, "activity_name:" + activity_name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IISERApp.log(LOG_TAG, "ondestroy");
        this.registerReceiver(mMessageReceiver, new IntentFilter(LOG_TAG));
        this.unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IISERApp.log(LOG_TAG, "onActivityResult");
        IISERApp.log(LOG_TAG, "RequestCode->" + requestCode);
        IISERApp.log(LOG_TAG, "ResultCode->" + resultCode);
        if (requestCode == PERMISSION_REQUEST_DOCUMENT) {
            if (requestCode != -1) {
                if (requestCode == 2) {
                    if (data != null) {
                        IISERApp.log(LOG_TAG, "URL->" + data.getData());
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        IISERApp.log(LOG_TAG, "Uri = " + uri.toString());
                        IISERApp.set_session(IISERApp.SESSION_ASSIGNMENT_DOCUMENT_FLAG, "Y");
                        IISERApp.set_session(IISERApp.SESSION_ASSIGNMENT_DOCUMENT_URI,uri.toString());
                    }
                }else if(requestCode==1)
                {

                }
            }
        }
    }

    public void check_app_permission() {
        permission_count = 2;
        int permission_granted = PackageManager.PERMISSION_GRANTED;
        IISERApp.log(LOG_TAG, "PermissionGrantedCode->" + permission_granted);

        int storage_permission = ContextCompat.checkSelfPermission(ActivityHome.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        IISERApp.log(LOG_TAG, "StoragePermission->" + storage_permission);
        if(storage_permission == permission_granted)
            permission_count -= 1;

        int audio_record_permission = ContextCompat.checkSelfPermission(ActivityHome.this, Manifest.permission.RECORD_AUDIO);
        IISERApp.log(LOG_TAG, "audio_record_permission->" + audio_record_permission);
        if(audio_record_permission == permission_granted)
            permission_count -= 1;

        int camera_permission = ContextCompat.checkSelfPermission(ActivityHome.this, Manifest.permission.CAMERA);
        IISERApp.log(LOG_TAG, "camera_permission->" + camera_permission);
        if(camera_permission == permission_granted)
            permission_count -= 1;


        IISERApp.log(LOG_TAG, "check_app_permission PermissionCount->" + permission_count);

        if (permission_count > 0) {
            String permissionArray[] = new String[permission_count];

            for (int i = 0; i < permission_count; i++) {
                IISERApp.log(LOG_TAG, "i->" + i);


                if (storage_permission != permission_granted)
                {
                    if(!Arrays.asList(permissionArray).contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        permissionArray[i] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                        IISERApp.log(LOG_TAG,"i->WRITE_EXTERNAL_STORAGE");
                        // break;
                    }

                }
                if (audio_record_permission != permission_granted) {
                    if (!Arrays.asList(permissionArray).contains(Manifest.permission.RECORD_AUDIO))
                    {
                        permissionArray[i] = Manifest.permission.RECORD_AUDIO;
                        IISERApp.log(LOG_TAG, "i->RECORD_AUDIO");
                        // break;
                    }
                }

                if (camera_permission != permission_granted) {
                    if (!Arrays.asList(permissionArray).contains(Manifest.permission.CAMERA))
                    {
                        permissionArray[i] = Manifest.permission.CAMERA;
                        IISERApp.log(LOG_TAG, "i->CAMERA");
                        // break;
                    }
                }
            }
            IISERApp.log(LOG_TAG, "PermissionArray->" + Arrays.deepToString(permissionArray));

            ActivityCompat.requestPermissions(getParent(), permissionArray, permission_count);//requestPermissions(permissionArray, permission_count);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permission_count = permissions.length;
        IISERApp.log(LOG_TAG, "In onRequestPermissionsResult");
        IISERApp.log(LOG_TAG, "requestCode->" + requestCode);
        IISERApp.log(LOG_TAG, "permissions->" + Arrays.deepToString(permissions));
        int len = grantResults.length;
        IISERApp.log(LOG_TAG, "permissionsLength->" + len);

        int permission_granted = PackageManager.PERMISSION_GRANTED;
        IISERApp.log(LOG_TAG, "PermissionGrantedCode->" + permission_granted);
        String str = "";
        for (int i = 0; i < len; i++) {
            IISERApp.log(LOG_TAG,"In for permission");

            if(permissions[i].equalsIgnoreCase(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                int storage_permission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                IISERApp.log(LOG_TAG, "StoragePermission->" + storage_permission);
                if (storage_permission == permission_granted) {
                    permission_count -= 1;
                }
                else {
                    str += "Storage, ";
                }
            }

            if(permissions[i].equalsIgnoreCase(Manifest.permission.RECORD_AUDIO)) {
                int audio_record_permission = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
                IISERApp.log(LOG_TAG, "audio_record_permission->" + audio_record_permission);
                if (audio_record_permission == permission_granted) {
                    permission_count -= 1;
                }
                else {
                    str += "Audio Record, ";
                }
            }

            if(permissions[i].equalsIgnoreCase(Manifest.permission.CAMERA)) {
                int camera_permission = checkSelfPermission(Manifest.permission.CAMERA);
                IISERApp.log(LOG_TAG, "camera_permission->" + camera_permission);
                if (camera_permission == permission_granted) {
                    permission_count -= 1;
                }
                else {
                    str += "Camera ";
                }
            }


            IISERApp.log(LOG_TAG, "onRequestPermissionsResult PermissionCount->" + permission_count);
        }

        if (permission_count > 0) {

            // Toast.makeText(ActivityTabHost.this, "IISER needs permissions :" + str, Toast.LENGTH_SHORT).show();
            Snackbar.make(host, "App needs permissions : " + str,
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    String SCHEME = "package";
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts(SCHEME, getApplication().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            }).show();
        } else {
            Intent in=new Intent(ActivityHome.this,ActivityAddAssignment.class);
            startActivity(in);
        }
    }


    public int getPermissionCount() {
        int count = 3;
        int permission_granted = PackageManager.PERMISSION_GRANTED;

        int storage_permission = ContextCompat.checkSelfPermission(ActivityHome.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (storage_permission == permission_granted)
            count -= 1;

        int audio_record_permission = ContextCompat.checkSelfPermission(ActivityHome.this,Manifest.permission.RECORD_AUDIO);
        if (audio_record_permission == permission_granted)
            count -= 1;

        int camera_permission = ContextCompat.checkSelfPermission(ActivityHome.this,Manifest.permission.CAMERA);
        if (camera_permission == permission_granted)
            count -= 1;

        return count;
    }


    @Override
    public void onResume() {

        IISERApp.log(LOG_TAG, "in resume");
        super.onResume();
        if (IISERApp.get_session(IISERApp.SESSION_NOTIFICATION_FLAG).equalsIgnoreCase("Assignment")) {
            host.setCurrentTab(2);
            IISERApp.set_session(IISERApp.SESSION_NOTIFICATION_FLAG, "");
        } else if (IISERApp.get_session(IISERApp.SESSION_NOTIFICATION_FLAG).equalsIgnoreCase("Notice")) {
            host.setCurrentTab(0);
            IISERApp.set_session(IISERApp.SESSION_NOTIFICATION_FLAG, "");
        } else if (IISERApp.get_session(IISERApp.SESSION_NOTIFICATION_FLAG).equalsIgnoreCase("Examschedule")) {
            host.setCurrentTab(1);
            IISERApp.set_session(IISERApp.SESSION_NOTIFICATION_FLAG, "");
        } else if (IISERApp.get_session(IISERApp.SESSION_NOTIFICATION_FLAG).equalsIgnoreCase("Timetable")) {
            host.setCurrentTab(3);
            IISERApp.set_session(IISERApp.SESSION_NOTIFICATION_FLAG, "");
        }
       /* else if (IISERApp.get_session(IISERApp.SESSION_NOTIFICATION_FLAG).equalsIgnoreCase("Profile")) {
            host.setCurrentTab(4);
            IISERApp.set_session(IISERApp.SESSION_NOTIFICATION_FLAG, "");
        }*/
        //IISERApp.log(LOG_TAG,"IN THIS.REGISTERRECEIVER");
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
