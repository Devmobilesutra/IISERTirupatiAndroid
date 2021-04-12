package com.mobilesutra.iiser_tirupati.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Database.Database;
import com.mobilesutra.iiser_tirupati.Error_Activity.ExceptionHandler;
import com.mobilesutra.iiser_tirupati.Fragment.Fragment_Assign;
import com.mobilesutra.iiser_tirupati.R;

import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

/**
 * Created by kalyani on 25/04/2016.
 */
public class ActivityTabHost extends TabActivity implements TabHost.OnTabChangeListener {

    TabHost host = null;
    int tab_changed_img[] = {R.drawable.img_overview, R.drawable.img_faculty_profile, R.drawable.img_courses_offered,
            R.drawable.img_admission_process, R.drawable.img_events, R.drawable.img_login};
    private static Database db;
    private String LOG_TAG = "ActivityTabHost", activity_name = "";
    Context context = null;
    String deviceId = null;
    private int permission_count=3;
    String newVersion = "";
    boolean active = true;
    final int splashTime = 6000; // time to display the splash screen in m


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_tabhost);
        context = this;

        IISERApp.log(LOG_TAG, "INTENT_FLAG_INSERT_COURSE_DATA:" + IISERApp.INTENT_FLAG_INSERT_COURSE_DATA);
       /* if(IISERApp.get_session(IISERApp.INTENT_FLAG_INSERT_COURSE_DATA).equalsIgnoreCase("")) {
            Intent intent1 = new Intent(context, IISERIntentService.class);
            intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_GET_COURSE_DATA);
            context.startService(intent1);
        }*/
        IISERApp.log(LOG_TAG, "INTENT_FLAG_INSERT_COURSE_DATA:" + IISERApp.INTENT_FLAG_INSERT_COURSE_DATA);

        IISERApp.log(LOG_TAG, "GCMKey->" + ((IISERApp) getApplication()).getRegistrationId(context));


        if (((IISERApp) getApplication()).isInternetAvailable())
            if (((IISERApp) getApplication()).getRegistrationId(context).length() == 0)
                ((IISERApp) getApplication()).getRegistrationGCMID();
//get device ID

        deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        IISERApp.log(LOG_TAG, "DeviceId:" + deviceId);
        IISERApp.set_session(IISERApp.SESSION_DEVICE_ID, deviceId);


        db = new Database(this);
        export_database();
       /* set_tab_host();*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getPermissionCount() > 0)
                check_app_permission();
            //else
            // startActivity(callIntent);

        } else {
            /*set_tab_host();*/
            // startActivity(callIntent);
        }
        set_tab_host();
    }

    private void check_app_permission() {
        permission_count = 3;
        int permission_granted = PackageManager.PERMISSION_GRANTED;
        IISERApp.log(LOG_TAG, "PermissionGrantedCode->" + permission_granted);

        int calendar_permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR);
        IISERApp.log(LOG_TAG, "calendar_permission->" + calendar_permission);
        if (calendar_permission == permission_granted)
            permission_count -= 1;


        int storage_permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        IISERApp.log(LOG_TAG, "StoragePermission->" + storage_permission);
        if(storage_permission == permission_granted)
            permission_count -= 1;

     /*   int audio_record_permission = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO);
        IISERApp.log(LOG_TAG, "audio_record_permission->" + audio_record_permission);
        if(audio_record_permission == permission_granted)
            permission_count -= 1;

        int camera_permission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        IISERApp.log(LOG_TAG, "camera_permission->" + camera_permission);
        if(camera_permission == permission_granted)
            permission_count -= 1;
*/

        IISERApp.log(LOG_TAG, "check_app_permission PermissionCount->" + permission_count);

        if (permission_count > 0) {
            String permissionArray[] = new String[permission_count];

            for (int i = 0; i < permission_count; i++) {
                IISERApp.log(LOG_TAG, "i->" + i);

                if (calendar_permission != permission_granted) {
                    if (!Arrays.asList(permissionArray).contains(Manifest.permission.WRITE_CALENDAR))
                    {
                        permissionArray[i] = Manifest.permission.WRITE_CALENDAR;
                        IISERApp.log(LOG_TAG, "i->WRITE_CALENDAR");
                        // break;
                    }
                }

                if (storage_permission != permission_granted)
                {
                    if(!Arrays.asList(permissionArray).contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        permissionArray[i] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                        IISERApp.log(LOG_TAG,"i->WRITE_EXTERNAL_STORAGE");
                        // break;
                    }

                }

               /* if (audio_record_permission != permission_granted) {
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
                }*/
            }
         //   IISERApp.log(LOG_TAG, "PermissionArray->" + Arrays.deepToString(permissionArray));
          //  ActivityCompat.requestPermissions(ActivityTabHost.this, permissionArray, permission_count);
            //requestPermissions(permissionArray, permission_count);
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

            if (permissions[i].equalsIgnoreCase(Manifest.permission.WRITE_CALENDAR)) {
                int calendar_permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR);
                IISERApp.log(LOG_TAG, "Calendar->" + calendar_permission);
                if (calendar_permission == permission_granted) {
                    permission_count -= 1;
                } else {
                    str += "Calendar";
                }
            }

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

        }
    }

    public int getPermissionCount() {
        int count = 4;
        int permission_granted = PackageManager.PERMISSION_GRANTED;
        int calendar_permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR);
        if (calendar_permission == permission_granted)
            count -= 1;

        int storage_permission = ContextCompat.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (storage_permission == permission_granted)
            count -= 1;

        int audio_record_permission = ContextCompat.checkSelfPermission(context,Manifest.permission.RECORD_AUDIO);
        if (audio_record_permission == permission_granted)
            count -= 1;

        int camera_permission = ContextCompat.checkSelfPermission(context,Manifest.permission.CAMERA);
        if (camera_permission == permission_granted)
            count -= 1;

        return count;
    }
    private void set_tab_host() {
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
        tab1.setContent(new Intent(this, ActivityAboutUs.class));
        tab1.setIndicator(null, getResources().getDrawable(R.drawable.img_overview));

        // tab2.setIndicator("Tab2");
        tab2.setContent(new Intent(this, ActivityFacultyProfile.class));
        tab2.setIndicator(null, getResources().getDrawable(R.drawable.img_faculty_profile));

        // tab3.setIndicator("Tab3");
        tab3.setContent(new Intent(this, ActivityCourses.class));
        tab3.setIndicator(null, getResources().getDrawable(R.drawable.img_courses_offered));

        // tab4.setIndicator("Tab4");
        tab4.setContent(new Intent(this, ActivityAdmissionProcess.class));
        tab4.setIndicator(null, getResources().getDrawable(R.drawable.img_admission_process));

        // tab5.setIndicator("Tab5");
        tab5.setContent(new Intent(this, ActivityEvents.class));
        tab5.setIndicator(null, getResources().getDrawable(R.drawable.img_events));



       /* if (IISERApp.get_session(IISERApp.SESSION_USERNAME)!= null && IISERApp.get_session(IISERApp.SESSION_USERNAME)!= null) {
            Intent intent =new Intent(context,ActivityTabHost_AfterLogin.class);
            startActivity(intent);
        }*/
        tab6.setContent(new Intent(this, ActivityLogin.class));
        tab6.setIndicator(null, getResources().getDrawable(R.drawable.img_login));




        host.addTab(tab1);
        host.addTab(tab2);
        host.addTab(tab3);
        host.addTab(tab4);
        host.addTab(tab5);
        host.addTab(tab6);



        for (int i = 0; i < host.getTabWidget().getChildCount(); i++) {
            if (activity_name != null) {
                if (activity_name.equalsIgnoreCase("Event")) {
                    host.setCurrentTab(4);
                    host.getTabWidget().getChildTabViewAt(4).setSelected(true);
                }
            }
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        IISERApp.log(LOG_TAG, "new intent");
        Bundle extras = intent.getExtras();
        IISERApp.log_bundle(extras);
        if (extras != null) {
            activity_name = extras.getString(IISERApp.NOTIFICATION_FLAG);
            IISERApp.log(LOG_TAG, "activity_name:" + activity_name);

            if (activity_name!=null && activity_name.length() != 0) {
                if (activity_name.equalsIgnoreCase("Event")) {
                    host.setCurrentTab(4);
                }
            }
        }

        //  Toast.makeText(this, "onNewIntent", Toast.LENGTH_SHORT).show();
    }

    public void export_database() {
        File dir = new File(Environment.getExternalStorageDirectory().getPath()
                + "/ExportDatabase");
        File dir_app = new File(Environment.getExternalStorageDirectory()
                .getPath()
                + "/ExportDatabase/"
                + this.getString(R.string.app_name));
        try {
            if (!dir.exists()) {
                if (dir.mkdir()) {
                    System.out.println("Directory created");
                } else {
                    System.out.println("Directory is not created");
                }
            }

            if (!dir_app.exists()) {
                if (dir_app.mkdir()) {
                    System.out.println("Directory created");
                } else {
                    System.out.println("Directory is not created");
                }
            }
        } catch (Exception e) {
            System.out.println("DirectoryErrors" + e.getLocalizedMessage());
        }
        String[] tableNames = db.getTableNames();
        int tableLength = tableNames.length;
        for (int i = 0; i < tableLength; i++) {
            String file = Environment.getExternalStorageDirectory().getPath()
                    + "/ExportDatabase/" + this.getString(R.string.app_name)
                    + "/" + tableNames[i] + ".csv";
            try {
                File myFile = new File(file);
                FileOutputStream fOut = null;
                OutputStreamWriter myOutWriter = null;
                String[] columnNames = db.getTableColumnNames(tableNames[i]);
                if (myFile.exists() == true)
                    myFile.delete();

                myFile.createNewFile();
                fOut = new FileOutputStream(myFile, true);
                myOutWriter = new OutputStreamWriter(fOut);
                int columnCount = columnNames.length;
                String headerNames = "";
                for (int j = 0; j < columnCount; j++)
                    headerNames += columnNames[j] + ",";

                myOutWriter.write(headerNames + "\n");

                String data = db.getDataFromTable(tableNames[i]);
                myOutWriter.write(data + "\n");

                myOutWriter.flush();
                myOutWriter.close();
                fOut.close();
                myFile = null;

            } catch (IOException e) {
                System.out.println("FileErrors" + e.getLocalizedMessage());
            }
            MediaScannerConnection.scanFile(ActivityTabHost.this,
                    new String[]{file}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {

                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            // TODO Auto-generated method stub

                        }

                    });
        }// for

    }

   /* @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ActivityTabHost.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
*/






   /* void check_for_upgrade() {
        if (((IISERApp) getApplication()).isInternetAvailable()) {
            new FetchAppVersionFromGooglePlayStore().execute();
        } else {

            proceed();
        }
    }

    public void proceed() {
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (active && (waited < splashTime)) {
                        sleep(10);
                        if (active) {
                            waited += 10;
                        }
                    }
                } catch (InterruptedException e) {

                } finally {

                       *//* if ((((ExpoApp) getApplication()).db).user_is_login(ExpoApp.ExpoID)) {
                            Intent intent = new Intent(ActivitySplash.this, ActivityLogin.class);
                            startActivity(intent);
                            finish();
                        } else {*//*
                 *//*   Intent intent = new Intent(ActivitySplash.this, ActivityDashboard.class);
                    startActivity(intent);
                    finish();*//*
                    //}

                }
            }
        };
        splashTread.start();
    }




    class FetchAppVersionFromGooglePlayStore extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            try {

                return
                        Jsoup.connect("https://play.google.com/store/apps/details?id=" + ActivityTabHost.this.getPackageName() + "&hl=it")
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
                    proceed();
                }



                if (server_version > app_version) {
                    show_upgrade_dialog();
                } else {

                    proceed();
                }
                //show_upgrade_dialog();


            }else{
                proceed();
            }
        }
    }



    void show_upgrade_dialog() {
        final Dialog dialog1 = new Dialog(ActivityTabHost.this);
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
*/








}