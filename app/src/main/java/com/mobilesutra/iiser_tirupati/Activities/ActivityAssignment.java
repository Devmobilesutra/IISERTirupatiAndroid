package com.mobilesutra.iiser_tirupati.Activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


/*import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;*/
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mobilesutra.iiser_tirupati.Config.AppConstant;
import com.mobilesutra.iiser_tirupati.Config.DocumentType;
import com.mobilesutra.iiser_tirupati.Config.FileUtils;
import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Database.TABLE_COURSE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_EXAM_SCHEDULE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_TIMETABLE;
import com.mobilesutra.iiser_tirupati.Error_Activity.ExceptionHandler;
import com.mobilesutra.iiser_tirupati.Fragment.Fragment_Assign;
import com.mobilesutra.iiser_tirupati.Model.DTO_Courses_offered;
import com.mobilesutra.iiser_tirupati.R;
import com.mobilesutra.iiser_tirupati.Tabs.SlidingTabLayout;
import com.mobilesutra.iiser_tirupati.background.IISERIntentService;
import com.mobilesutra.iiser_tirupati.banner.Animations.DescriptionAnimation;
import com.mobilesutra.iiser_tirupati.banner.SliderLayout;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.BaseSliderView;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.TextSliderView;
import com.mobilesutra.iiser_tirupati.banner.Tricks.ViewPagerEx;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by kalyani on 26/04/2016.
 */
public class ActivityAssignment extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    Context context = null;
    RecyclerView recycler_view = null;
    public List<DTO_Courses_offered> course_List = new ArrayList<>();
    EventAdapter recyclerAdapter = null;
    LinearLayoutManager linearLayoutManager = null;
    Fragment_Assign first = null;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    FloatingActionButton fab_refresh = null, fab_add_assingment = null;

    private List<PagerItem> listTabs = new ArrayList<PagerItem>();
    private SlidingTabLayout objSlidingTabLayout = null;
    ViewPagerAdapter objViewPagerAdapter = null;
    ViewPager mViewPager = null;
    TextView add_process = null;
    TextView user_name = null;

    List<LinkedHashMap<String, String>> assign_list = new ArrayList<>();
    SliderLayout mDemoSlider = null;

    String LOG_TAG = "ActivityAssignment", str_file_name = "", str_file_path = "", filename_image = "", str_image_path = "";
    Dialog dialog1;
    int PERMISSION_REQUEST_VIDEO = 1, PERMISSION_REQUEST_DOCUMENT = 2, PERMISSIONS_MULTIPLE_REQUEST = 123;
    ;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    TextView txt_file_name = null;

    Handler handler;
    TextView tv;
    MediaRecorder mRecorder;
    String fileName;
    Boolean isRecording;
    int recordTime, playTime;
    SeekBar seekBar;
    MediaPlayer mPlayer;
    Button tbrecord, tbstop, tbplay, image_capture;
    private int permission_count = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.fragment_three);






        context = this;
        // objSlidingTabLayout = null;

       /* if (IISERApp.is_marshmellow()) {
            int count = getPermissionCount();
            IISERApp.log(LOG_TAG, "MPermissionCount->" + count);
            if (count > 0) {
                check_app_permission();
            }
        }*/
        getIntentData();
        initComponentData();
        initComponentListeners();
        displayBanner();
        bindComponentData();
        // populateViewPager();

        /*if (((IISERApp) getApplication()).isInternetAvailable()) {
            Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
            intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_ASSIGNMENT);

            context.startService(intent1);
        } else {
            Snackbar.make(recycler_view, "No Internet available...", Snackbar.LENGTH_LONG).show();
        }*/
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();




    }

    private void getIntentData() {
    }

    private void initComponentData() {
        fab_refresh = (FloatingActionButton) findViewById(R.id.fab);
        fab_refresh.setVisibility(View.GONE);
        fab_add_assingment = (FloatingActionButton) findViewById(R.id.fab_add_assingment);


        if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("faculty")) {
            fab_add_assingment.setVisibility(View.GONE);
        }
        if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("student")) {
            fab_add_assingment.setVisibility(View.GONE);
        }

        if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("supervisor"))
        {
            fab_add_assingment.setVisibility(View.GONE);
        }

        add_process = (TextView) findViewById(R.id.add_process);
        //add_process.setVisibility(View.GONE);
        add_process.setText("ASSIGNMENT");
        user_name = (TextView) findViewById(R.id.user_name);
        IISERApp.log(LOG_TAG, "session value is" + IISERApp.get_session(IISERApp.SESSION_LOGIN_FLAG));
        String user = IISERApp.get_session((IISERApp.SESSION_STUDENT_NAME));

        String sem_name = IISERApp.get_session((IISERApp.SESSION_SEMESTER_NAME));
        if ((IISERApp.get_session(IISERApp.SESSION_LOGIN_FLAG).equalsIgnoreCase("Y"))) {


            if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("student")) {
                user_name.setText(user + " (sem- " + sem_name + ")");
            } else if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("faculty")) {
                user_name.setText(user + " (" + "Faculty" + ")");
            } else if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("supervisor")) {
                user_name.setText(user + " (" + "Supervisor" + ")");
            }
        } else if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("student")) {
            user_name.setText(user + " (sem- " + sem_name + ")");
        } else if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("faculty")) {
            user_name.setText(user + " (" + "Faculty" + ")");
        } else if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("supervisor")) {
            user_name.setText(user + " (" + "Supervisor" + ")");
        }

    }

    private void initComponentListeners() {
        fab_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityAssignment.this, "Please wait...Refreshing Data.", Toast.LENGTH_LONG).show();

                refresh_data();

            }
        });
        fab_add_assingment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (((ActivityHome)getParent()).getPermissionCount() >0)
                        ((ActivityHome)getParent()).check_app_permission();
                    else {
                        Intent in=new Intent(ActivityAssignment.this,ActivityAddAssignment.class);
                        startActivity(in);
                        //add_new_assignment();
                    }
                } else {
                    Intent in=new Intent(ActivityAssignment.this,ActivityAddAssignment.class);
                    startActivity(in);
                    //add_new_assignment();
                }

            }
        });

    }




    private void add_new_assignment() {

        str_file_name = "";
        dialog1 = new Dialog(this.getParent().getWindow().getContext());
        // dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.activity_add_assignment);
        dialog1.show();
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(false);

        txt_file_name = (TextView) dialog1.findViewById(R.id.txt_file_name);
        txt_file_name.setText(str_file_name);
        final TextView txt_document = (TextView) dialog1.findViewById(R.id.txt_document);


       /* tv = (TextView) dialog1.findViewById(R.id.txttime);
        seekBar = (SeekBar) dialog1.findViewById(R.id.seek1);
        handler = new Handler();
        fileName = Environment.getExternalSto
        rageDirectory() + "/audio" + System.currentTimeMillis() + ".3gp";
        isRecording = false;
        tbstop = (Button) dialog1.findViewById(R.id.tbstop);
        tbrecord = (Button) dialog1.findViewById(R.id.tbrecord);
        tbplay = (Button) dialog1.findViewById(R.id.tbplay);*/

        image_capture = (Button) dialog1.findViewById(R.id.image_capture);

        image_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IISERApp.log(LOG_TAG, "Image Clicked");

                String current_date = ((IISERApp) getApplication()).get_current_date();
                IISERApp.log(LOG_TAG, "current date is: " + current_date);
                try {
                    IISERApp.log(LOG_TAG, "In try");
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    File imageStorageDir = new File(Environment.getExternalStorageDirectory(), AppConstant.PHOTO_ALBUM);
                    if (!imageStorageDir.exists()) {
                        imageStorageDir.mkdirs();
                    }
                    filename_image = "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                    str_image_path = imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                    File img_file = new File(str_image_path);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(img_file));

                   /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (getPermissionCount() > 0) {
                            check_app_permission();
                        } else {*/
                    startActivityForResult(intent, 1);
                     /*   }

                    } else {
                        startActivityForResult(intent, 1);
                    }*/
                } catch (Exception e) {
                    IISERApp.log(LOG_TAG, "In catch");
                    //Toast.makeText(context, "Unable to get Camera, Please try again later!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Unable to get Camera, Please try again later!", Toast.LENGTH_SHORT);
                }

            }
        });

       /* tbrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecording) {
                    //Create MediaRecorder and initialize audio source, output format, and audio encoder
                    mRecorder = new MediaRecorder();
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mRecorder.setOutputFile(fileName);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    // Starting record time
                    recordTime = 0;
                    // Show TextView that displays record time
                    tv.setVisibility(TextView.VISIBLE);
                    try {
                        mRecorder.prepare();
                    } catch (IOException e) {
                        Log.e("LOG_TAG", "prepare failed");
                    }
                    // Start record job
                    mRecorder.start();
                    // Change isRecroding flag to true
                    isRecording = true;
                    // Post the record progress
                    handler.post(UpdateRecordTime);
                }
            }
        });
        tbstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    // Stop recording and release resource
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;
                    // Change isRecording flag to false
                    isRecording = false;
                    // Hide TextView that shows record time
                    tv.setVisibility(TextView.GONE);
                   *//* playIt(); // Play the audio*//*
                }
            }
        });
        tbplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playIt(); // Play the audio
            }
        });*/
        txt_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(txt_document, "Documents Allowed: csv,pdf,xlsx,xls,txt,rtf,docx,doc,pptx,ppt",
                        Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestForDocument();
                        // txt_file_name.setText(str_file_name);
                    }
                }).show();

            }
        });


    }

    public void playIt() {
        // Create MediaPlayer object
        mPlayer = new MediaPlayer();
        // set start time
        playTime = 0;
        // Reset max and progress of the SeekBar
        seekBar.setMax(recordTime);
        seekBar.setProgress(0);
        try {
            // Initialize the player and start playing the audio
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            mPlayer.start();
            // Post the play progress
            handler.post(UpdatePlayTime);
        } catch (IOException e) {
            Log.e("LOG_TAG", "prepare failed");
        }
    }

    Runnable UpdateRecordTime = new Runnable() {
        public void run() {
            if (isRecording) {
                tv.setText(String.valueOf(recordTime));
                recordTime += 1;
                // Delay 1s before next call
                handler.postDelayed(this, 1000);
            }
        }
    };
    Runnable UpdatePlayTime = new Runnable() {
        public void run() {
            if (mPlayer.isPlaying()) {
                tv.setText(String.valueOf(playTime));
                // Update play time and SeekBar
                playTime += 1;
                seekBar.setProgress(playTime);
                // Delay 1s before next call
                handler.postDelayed(this, 1000);
            }
        }
    };


    private void requestForDocument() {

        IISERApp.log(LOG_TAG, "requestForDocument");

        List<Intent> targetShareIntents = new ArrayList<Intent>();
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        // intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        List<ResolveInfo> resInfos = getPackageManager().queryIntentActivities(intent, 0);
        if (!resInfos.isEmpty()) {
            System.out.println("Have package");
            for (ResolveInfo resInfo : resInfos) {
                String packageName = resInfo.activityInfo.packageName;
                IISERApp.log(LOG_TAG, "Package Name->" + packageName);
                Intent intent1 = new Intent();
                intent1.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                targetShareIntents.add(intent1);
            }
        }
        if (!targetShareIntents.isEmpty()) {
            System.out.println("Have Intent");
            Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0), "Choose app to select document");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
            getParent().startActivityForResult(chooserIntent, PERMISSION_REQUEST_DOCUMENT);
        } else {
            System.out.println("Do not Have Intent");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IISERApp.log(LOG_TAG, "onActivityResult");
        isRemovableSDCardAvailable();
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
                     /*
     Get the file's content URI from the incoming Intent, then
      get the file's MIME type
     */

                        try {
                            String mimeType = getContentResolver().getType(uri);
                            IISERApp.log(LOG_TAG, "MimeType->" + mimeType);
                            // Get the file path from the URI
                            final String path = FileUtils.getPath(context, uri);
                            IISERApp.log(LOG_TAG, "path->" + path);
                            if (!TextUtils.isEmpty(path)) {
                                String str[] = path.split("\\.");

                                IISERApp.log(LOG_TAG, "strArr->" + str.length);
                                if (str.length > 0) {
                                    mimeType = str[(str.length - 1)];
                                    IISERApp.log(LOG_TAG,"extesion is"+mimeType);
                                }
                                if (DocumentType.check_valid_docuement(mimeType)) {

                                    String str_file[] = path.split("\\/");
                                    IISERApp.log(LOG_TAG, "str_file->" + str_file.length);
                                    if (str_file.length > 0) {
                                        IISERApp.log(LOG_TAG, "str_fileName->" + str_file[(str_file.length - 1)]);
                                        str_file_name = str_file[(str_file.length - 1)];

                                    } else {
                                        str_file_name = path;
                                    }
                                    IISERApp.log(LOG_TAG, "DocType->" + mimeType);
                                    IISERApp.log(LOG_TAG, "path->" + path);
                                    IISERApp.log(LOG_TAG, "IsValidDoc->" + DocumentType.check_valid_docuement(mimeType));
                                    IISERApp.log(LOG_TAG, "MimeType->" + DocumentType.get_document_mimeType(mimeType));

                                    File file = new File(path);
                                    str_file_path = path;

                                    IISERApp.log(LOG_TAG, "FileExists->" + file.exists());
                                    Toast.makeText(context, "File Selected: " + path, Toast.LENGTH_LONG).show();
                                    txt_file_name.setText(str_file_path);
                                  /*recyclerAdapter.rowItems.get(current_question_position).setStr_local_document(str_file_path);
                                    recyclerAdapter.rowItems.get(current_question_position).setStr_document_name(str_file_name);
                                    recyclerAdapter.notifyItemChanged(current_question_position);*/
                                } else {

                                    Snackbar.make(fab_add_assingment, "Inavlid document type", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            IISERApp.log(LOG_TAG, "File select error" + e);
                        }
                    }
                }
            }
        } /*else if (requestCode == PERMISSION_REQUEST_VIDEO) {
            if (resultCode != -1) {
                if (data != null) {

                    Bundle extras = data.getExtras();
                    RVFApp.log_bundle(extras);
                    str_video_path = extras.getString("file_path");
                    String str_file[] = str_video_path.split("\\/");
                    RVFApp.log(LOG_TAG, "str_video->" + str_file.length);
                    if (str_file.length > 0) {
                        RVFApp.log(LOG_TAG, "str_videoName->" + str_file[(str_file.length - 1)]);
                        str_video_name = str_file[(str_file.length - 1)];
                    } else {
                        str_video_name = str_video_path;
                    }
                    current_question_position = Integer.parseInt(extras.getString("position"));
                    recyclerAdapter.rowItems.get(current_question_position).setStr_local_video(str_video_path);
                    recyclerAdapter.rowItems.get(current_question_position).setStr_video_name(str_video_name);
                    recyclerAdapter.notifyItemChanged(current_question_position);
                    if (data != null) {
                        RVFApp.log(LOG_TAG, "URL->" + data.getData());
                    }
                }
            }

    }/**//**/


    }

    public String isRemovableSDCardAvailable() {
        final String FLAG = "mnt";
        final String SECONDARY_STORAGE = System.getenv("SECONDARY_STORAGE");
        //  final String EXTERNAL_STORAGE_DOCOMO = System.getenv("EXTERNAL_STORAGE_DOCOMO");
        /*final String EXTERNAL_SDCARD_STORAGE = System.getenv("EXTERNAL_SDCARD_STORAGE");
        final String EXTERNAL_SD_STORAGE = System.getenv("EXTERNAL_SD_STORAGE");
        final String EXTERNAL_STORAGE = System.getenv("EXTERNAL_STORAGE");
*/
        Map<Integer, String> listEnvironmentVariableStoreSDCardRootDirectory = new HashMap<Integer, String>();
        listEnvironmentVariableStoreSDCardRootDirectory.put(0, SECONDARY_STORAGE);
        // listEnvironmentVariableStoreSDCardRootDirectory.put(1, EXTERNAL_STORAGE_DOCOMO);
       /* listEnvironmentVariableStoreSDCardRootDirectory.put(1, EXTERNAL_SDCARD_STORAGE);
        listEnvironmentVariableStoreSDCardRootDirectory.put(2, EXTERNAL_SD_STORAGE);
        listEnvironmentVariableStoreSDCardRootDirectory.put(3, EXTERNAL_STORAGE);*/

        File externalStorageList[] = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            externalStorageList = getExternalFilesDirs(null);
        }
        String directory = null;
        int size = listEnvironmentVariableStoreSDCardRootDirectory.size();
        IISERApp.log(LOG_TAG, getClass().getSimpleName() + "Map->" + listEnvironmentVariableStoreSDCardRootDirectory);
        for (int i = 0; i < size; i++) {

            if (externalStorageList != null && externalStorageList.length > 1 && externalStorageList[1] != null) {
                directory = externalStorageList[1].getAbsolutePath();
                IISERApp.log(LOG_TAG, "directoryE->" + i + "->" + directory);
            } else {
                directory = listEnvironmentVariableStoreSDCardRootDirectory.get(i);
                IISERApp.log(LOG_TAG, "directoryL->" + i + "->" + directory);
            }
            IISERApp.log(LOG_TAG, "directory->" + directory);
            // directory = canCreateFile(directory);
            if (directory != null && directory.length() != 0) {
                if (i == size - 1) {
                    if (directory.contains(FLAG)) {
                        IISERApp.log(LOG_TAG, getClass().getSimpleName() + "SD Card's directory: " + directory);
                        return directory;
                    } else {
                        return null;
                    }
                }
                IISERApp.log(LOG_TAG, getClass().getSimpleName() + "SD Card's directory: " + directory);
                return directory;
            }
        }
        return null;
    }

    private void bindComponentData() {
        int assign_list_count = 0;
        assign_list = TABLE_EXAM_SCHEDULE.get_exam_schedule_list();
        //assign_list= TABLE_ASSIGNMENT.get_assignment_details();
        IISERApp.log(LOG_TAG, "exam_schedule_list: " + assign_list);//list is getting blank
        // assign_list_count = assign_list.size();


//        if(assign_list.size()==0)
//        {
//            Toast.makeText(ActivityAssignment.this, "Please select coursesbbbb", Toast.LENGTH_SHORT).show();
//        }


        /*for (int i = 0; i < course_array.length; i++) {
            DTO_Courses_offered assignment = new DTO_Courses_offered(course_array[i]);
            course_List.add(assignment);
        }
        recyclerAdapter = new EventAdapter(course_List);
        recycler_view.setAdapter(recyclerAdapter);

        recyclerAdapter.notifyDataSetChanged();*/


    }


    void populateViewPager()
    {

            //  if (objSlidingTabLayout == null) {
            try {
                mViewPager = (ViewPager) findViewById(R.id.viewpager);

                // mViewPager.setOffscreenPageLimit(1);
                LinkedHashMap<String, String> lhm=null;
                if(IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("student")) {
                    IISERApp.log(LOG_TAG,"in  if of session ");
                  lhm = TABLE_COURSE.get_selected_course();
                }
                else if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("faculty"))
                {
                    IISERApp.log(LOG_TAG,"in else if of session ");
                    lhm = TABLE_TIMETABLE.get_faculty_course();
                }

                int lhmLength = lhm.size();

                IISERApp.log(LOG_TAG, "Course_lhm:" + lhm);


                if (lhmLength > 0) {

                    Set<String> keys = lhm.keySet();
                    for (String key : keys) {
                        IISERApp.log(LOG_TAG, "key:" + key);
                        listTabs.add(new PagerItem(
                                key, // Title
                                0, // Indicator color
                                0 // Divider color
                        ));
                    }
                }
                // else
                //{
                //    Toast.makeText(ActivityAssignment.this, "Please select courses", Toast.LENGTH_SHORT).show();
                //  }
                /*listTabs.add(new PagerItem("BIO 302",0,0));
                listTabs.add(new PagerItem("BIO 323",0,0));
                listTabs.add(new PagerItem("CHM 310",0,0));
                listTabs.add(new PagerItem("CHM 322",0,0));
                listTabs.add(new PagerItem("MTH 320",0,0));
                listTabs.add(new PagerItem("PHY 320",0,0));
                listTabs.add(new PagerItem("PHY 321",0,0));
                listTabs.add(new PagerItem("ECS 311",0,0));*/

                objViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                mViewPager.setAdapter(objViewPagerAdapter);

                objSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabs);
                objSlidingTabLayout.setViewPager(mViewPager);
                objSlidingTabLayout.set_selected_text_color(0);

                objSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                        objSlidingTabLayout.set_selected_text_color(position);
                       /* Log.d("Fragment", "onPageSelected->" + position);
                        Fragment fragmentDemo =
                                getSupportFragmentManager().findFragmentById(R.id.viewpager);
                        Log.d("Fragment", "fragmentDemo->" + fragmentDemo);
                        RVFApp.set_session(RVFApp.SESSION_CURRENT_TAB, position + "");
                        if(position == 0) {
                            FragmentOne first = (FragmentOne) objViewPagerAdapter.instantiateItem(mViewPager, position);
                            first.refreshData();
                        }
                        else if(position == 1) {
                            FragmentTwo first = (FragmentTwo) objViewPagerAdapter.instantiateItem(mViewPager, position);
                            first.refreshData();
                        }
                        else if(position == 2) {
                            FragmentThree first = (FragmentThree) objViewPagerAdapter.instantiateItem(mViewPager, position);
                            first.refreshData();
                        }*/

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                // objSlidingTabLayout.set_current_tab(RVFApp.get_Intsession(RVFApp.SESSION_CURRENT_TAB));
                objSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

                    @Override
                    public int getIndicatorColor(int position) {
                        int color = listTabs.get(position).getIndicatorColor();
                        return color;
                    }

                    @Override
                    public int getDividerColor(int position) {
                        int color = listTabs.get(position).getDividerColor();
                        return color;
                    }

                });
            } catch (final Exception ex) {
                Log.d("Fragment", "Exception in thread->" + ex.getMessage());
            }
            // }



       /*else if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("faculty")) {
            //  if (objSlidingTabLayout == null) {
            try {
                mViewPager = (ViewPager) findViewById(R.id.viewpager);

                // mViewPager.setOffscreenPageLimit(1);


                LinkedHashMap<String, String> lhm = TABLE_TIMETABLE.get_faculty_course();

                int lhmLength = lhm.size();

                IISERApp.log(LOG_TAG, "Course_lhm:" + lhm);


                if (lhmLength > 0) {

                    Set<String> keys = lhm.keySet();
                    for (String key : keys) {
                        IISERApp.log(LOG_TAG, "key:" + key);
                        listTabs.add(new PagerItem(
                                key, // Title
                                0, // Indicator color
                                0 // Divider color
                        ));
                    }
                }
                // else
                //{
                //    Toast.makeText(ActivityAssignment.this, "Please select courses", Toast.LENGTH_SHORT).show();
                //  }
                *//*listTabs.add(new PagerItem("BIO 302",0,0));
                listTabs.add(new PagerItem("BIO 323",0,0));
                listTabs.add(new PagerItem("CHM 310",0,0));
                listTabs.add(new PagerItem("CHM 322",0,0));
                listTabs.add(new PagerItem("MTH 320",0,0));
                listTabs.add(new PagerItem("PHY 320",0,0));
                listTabs.add(new PagerItem("PHY 321",0,0));
                listTabs.add(new PagerItem("ECS 311",0,0));*//*

                objViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                mViewPager.setAdapter(objViewPagerAdapter);

                objSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabs);
                objSlidingTabLayout.setViewPager(mViewPager);
                objSlidingTabLayout.set_selected_text_color(0);

                objSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                        objSlidingTabLayout.set_selected_text_color(position);
                       *//* Log.d("Fragment", "onPageSelected->" + position);
                        Fragment fragmentDemo =
                                getSupportFragmentManager().findFragmentById(R.id.viewpager);
                        Log.d("Fragment", "fragmentDemo->" + fragmentDemo);
                        RVFApp.set_session(RVFApp.SESSION_CURRENT_TAB, position + "");
                        if(position == 0) {
                            FragmentOne first = (FragmentOne) objViewPagerAdapter.instantiateItem(mViewPager, position);
                            first.refreshData();
                        }
                        else if(position == 1) {
                            FragmentTwo first = (FragmentTwo) objViewPagerAdapter.instantiateItem(mViewPager, position);
                            first.refreshData();
                        }
                        else if(position == 2) {
                            FragmentThree first = (FragmentThree) objViewPagerAdapter.instantiateItem(mViewPager, position);
                            first.refreshData();
                        }*//*

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                // objSlidingTabLayout.set_current_tab(RVFApp.get_Intsession(RVFApp.SESSION_CURRENT_TAB));
                objSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

                    @Override
                    public int getIndicatorColor(int position) {
                        int color = listTabs.get(position).getIndicatorColor();
                        return color;
                    }

                    @Override
                    public int getDividerColor(int position) {
                        int color = listTabs.get(position).getDividerColor();
                        return color;
                    }

                });
            } catch (final Exception ex) {
                Log.d("Fragment", "Exception in thread->" + ex.getMessage());
            }
            // }
        }*/
    }

   /* @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ActivityAssignment Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.mobilesutra.iiser_tirupati.Activities/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ActivityAssignment Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.mobilesutra.iiser_tirupati.Activities/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }*/

    static class PagerItem {
        private final CharSequence mTitle;
        private final int mIndicatorColor;
        private final int mDividerColor;

        PagerItem(CharSequence title, int indicatorColor, int dividerColor) {
            mTitle = title;
            mIndicatorColor = indicatorColor;
            mDividerColor = dividerColor;
        }

        /**
         * @return A new {@link Fragment} to be displayed by a {@link ViewPager}
         */

        Fragment createFragment() {
            Log.d("Fragment", "In createFragment");
            return Fragment_Assign.newInstance(mTitle.toString());


        }


        CharSequence getTitle() {
            return mTitle;
        }

        int getIndicatorColor() {
            return mIndicatorColor;
        }

        int getDividerColor() {
            return mDividerColor;
        }


    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return listTabs.get(i).createFragment();
        }

        @Override
        public int getCount() {
            return listTabs.size();
            //IISERApp.log(LOG_TAG"Count of tabs is "+listTabs.getSize());
            //if(listTabs.size()==0)
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return listTabs.get(position).getTitle();
        }

    }

    public void displayBanner() {
        // final ViewFlipper MyViewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);


        mDemoSlider = (SliderLayout) findViewById(R.id.sample_output);
        mDemoSlider.setVisibility(View.GONE);
        mDemoSlider.setFocusable(true);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(3000);
        mDemoSlider.addOnPageChangeListener(this);
       /* LinkedHashMap<String, String> lhm = ShopApp.db.getFrontBanner();
        int length = lhm.size();
        if (length == 0) {*/
        int bannerArray[] = {R.drawable.banner, R.drawable.banner_1,  R.drawable.banner_3};

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

    public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

        private List<DTO_Courses_offered> course_List;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView course_name;
            public LinearLayout lnrLayout;


            public MyViewHolder(View view) {
                super(view);
                course_name = (TextView) view.findViewById(R.id.txt_course_name);
                lnrLayout = (LinearLayout) view.findViewById(R.id.lnrlayout_event_cardview);
            }
        }

        public EventAdapter(List<DTO_Courses_offered> course_List) {
            this.course_List = course_List;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_course_list_row, parent, false);

           /* Log.d("Fragment","position of view:"+viewType);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("Fragment","position of view:"+view.getTag());
                    Intent intent = new Intent(getContext(), ActivityCourseDetails.class);
                    intent.putExtra("position",String.valueOf(view.getTag()));
                    startActivity(intent);


                }
            });*/
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position)
        {
            DTO_Courses_offered assign = course_List.get(position);
            Log.d("Fragment", "course name:" + assign.getStr_course_name());
            holder.course_name.setText(assign.getStr_course_name());


            holder.lnrLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("Fragment", "position of view:" + position);
                    Intent intent = new Intent(context, ActivityCourseDetails.class);
                    intent.putExtra("position", String.valueOf(position));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount()

        {
            return course_List.size();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IISERApp.log(LOG_TAG, "ondestroy");
        context.registerReceiver(mMessageReceiver, new IntentFilter(LOG_TAG));
        context.unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Toast.makeText(ActivityAssignment.this, "Please wait..refreshing data.", Toast.LENGTH_SHORT).show();
        refresh_data();
        Toast.makeText(ActivityAssignment.this, "Data refreshed successfully.", Toast.LENGTH_SHORT).show();

        IISERApp.log(LOG_TAG, "Awak onresume-->");
        context.registerReceiver(mMessageReceiver, new IntentFilter(LOG_TAG));


        if (IISERApp.get_session(IISERApp.SESSION_ASSIGNMENT_DOCUMENT_FLAG).equalsIgnoreCase("Y"))
        {
            set_document_name();
        }
        int count = TABLE_COURSE.get_selected_course_list();

        if (count == 0) {
            ///  listTabs.clear();
            listTabs.clear();
            objSlidingTabLayout = null;
            populateViewPager();
            if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("student")) {
                Toast.makeText(ActivityAssignment.this, "Please select courses...", Toast.LENGTH_SHORT).show();
            }
            // Toast.makeText(ActivityAssignment.this, "Courses are selected...", Toast.LENGTH_SHORT).show();
        } else {

          /*  listTabs.clear();
            objSlidingTabLayout=null;
            populateViewPager();*/

            IISERApp.log("XYZ", " onResumeFragments():");
            IISERApp.set_session("onRES", "one");
       /* first=new Fragment_Assign();
        first.refreshData();*/
            int size = listTabs.size();
            IISERApp.log(LOG_TAG, "size_of_tabs is:" + size);
            for (int i = 0; i < size; i++) {
                first = (Fragment_Assign) objViewPagerAdapter.instantiateItem(mViewPager, i);
                first.refreshData();
            }
        }
        // this.onCreate(null);

    }

    private void set_document_name() {
        final Uri uri = Uri.parse(IISERApp.get_session(IISERApp.SESSION_ASSIGNMENT_DOCUMENT_URI));
        IISERApp.log(LOG_TAG, "Uri = " + uri.toString());
          /*
     Get the file's content URI from the incoming Intent, then
      get the file's MIME type
     */

        try {
            String mimeType = getContentResolver().getType(uri);
            IISERApp.log(LOG_TAG, "MimeType->" + mimeType);
            // Get the file path from the URI
            final String path = FileUtils.getPath(context, uri);
            IISERApp.log(LOG_TAG, "path->" + path);
            if (!TextUtils.isEmpty(path)) {
                String str[] = path.split("\\.");
                IISERApp.log(LOG_TAG, "strArr->" + str.length);
                if (str.length > 0) {
                    mimeType = str[(str.length - 1)];
                }
                if (DocumentType.check_valid_docuement(mimeType)) {

                    String str_file[] = path.split("\\/");
                    IISERApp.log(LOG_TAG, "str_file->" + str_file.length);
                    if (str_file.length > 0) {
                        IISERApp.log(LOG_TAG, "str_fileName->" + str_file[(str_file.length - 1)]);
                        str_file_name = str_file[(str_file.length - 1)];
                    } else {
                        str_file_name = path;
                    }
                    IISERApp.log(LOG_TAG, "DocType->" + mimeType);
                    IISERApp.log(LOG_TAG, "path->" + path);
                    IISERApp.log(LOG_TAG, "IsValidDoc->" + DocumentType.check_valid_docuement(mimeType));
                    IISERApp.log(LOG_TAG, "MimeType->" + DocumentType.get_document_mimeType(mimeType));

                 /*   File file = new File(path);
                    str_file_path = path;

                    IISERApp.log(LOG_TAG, "FileExists->" + file.exists());*/
                    Toast.makeText(context, "File Selected: " + path, Toast.LENGTH_LONG).show();
                    txt_file_name.setText(str_file_name);
                    IISERApp.set_session(IISERApp.SESSION_ASSIGNMENT_DOCUMENT_FLAG, "N");
                                  /*recyclerAdapter.rowItems.get(current_question_position).setStr_local_document(str_file_path);
                                    recyclerAdapter.rowItems.get(current_question_position).setStr_document_name(str_file_name);
                                    recyclerAdapter.notifyItemChanged(current_question_position);*/
                } else {

                    Snackbar.make(fab_add_assingment, "Inavlid document type", Snackbar.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            IISERApp.log(LOG_TAG, "File select error" + e);
        }
    }


//    int get_course_count = TABLE_COURSE.get_selected_course_count();
//
//    if(get_course_count == 0)
//
//    {
//        Toast.makeText(ActivityAssignment.this, "courses are selectted", Toast.LENGTH_SHORT).show();
//    }
//    else
//    {
//        Toast.makeText(ActivityAssignment.this, "select courses", Toast.LENGTH_SHORT).show();
//    }

    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            IISERApp.log(LOG_TAG, "messege recieved");
            Bundle bundle = intent.getExtras();
            /*IISERApp.log_bundle(bundle);
            Intent intent1 = new Intent(context,ActivityAssignment.class);
            startActivity(intent1);
            finish();*/
           /* if (bundle.getString(IISERApp.BUNDLE_RESPONSE_CODE).equalsIgnoreCase("200")) {
                *//*Intent intent1 = new Intent(context,ActivityAssignment.class);
                startActivity(intent1);
                finish();*//*
                bindComponentData();
                populateViewPager();
                // Snackbar.make(recycler_view, bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE), Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(recycler_view, bundle.getString(IISERApp.BUNDLE_EXCEPTION), Snackbar.LENGTH_LONG).show();
            }*/

            int size = listTabs.size();
            for (int i = 0; i < size; i++) {
                first = (Fragment_Assign) objViewPagerAdapter.instantiateItem(mViewPager, i);
                first.refreshData();
                // onResumeFragments();
            }


        }
    };

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        IISERApp.log("XYZ", " onResumeFragments():");
        IISERApp.set_session("onRES", "one");
        /*first=new Fragment_TimeTable();
        first.refreshData();*/
        int size = listTabs.size();
        IISERApp.log(LOG_TAG, "size_of_tabs is:" + size);
        for (int i = 0; i < size; i++) {
            first = (Fragment_Assign) objViewPagerAdapter.instantiateItem(mViewPager, i);
            first.refreshData();
        }

        listTabs.clear();
        populateViewPager();
    }

    /* @Override
     protected void onResume() {

         super.onResume();
         this.onCreate(null);
     }*/
    public void refresh_data() {

        if (((IISERApp) getApplicationContext()).isInternetAvailable()) {
            IISERApp.log(LOG_TAG, "button is pressed for refreshing in notice ");
            //   prgress_dialog = ProgressDialog.show(context, null, null);
            Intent intent1 = new Intent(context, IISERIntentService.class);
            IISERApp.log(LOG_TAG, "App.INTENT_FLAG:" + IISERApp.INTENT_FLAG + " App.INTENT_FLAG_GET_EXAM:" + IISERApp.INTENT_FLAG_ASSIGNMENT);
            intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_ASSIGNMENT);
            intent1.putExtra("Activity_name", "ActivityAssignment");
            context.startService(intent1);
        } else {
            ((IISERApp) getApplicationContext()).isInternetAvailable();
        }
    }

}

