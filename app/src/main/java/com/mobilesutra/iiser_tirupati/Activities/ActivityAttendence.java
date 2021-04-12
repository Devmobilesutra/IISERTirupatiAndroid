package com.mobilesutra.iiser_tirupati.Activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Database.TABLE_ATTENDENCE_MASTER;
import com.mobilesutra.iiser_tirupati.Database.TABLE_COURSE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_STUDENT_ATTENDENCE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_TIMETABLE;
import com.mobilesutra.iiser_tirupati.Error_Activity.ExceptionHandler;
import com.mobilesutra.iiser_tirupati.Fragment.Fragment_Assign;
import com.mobilesutra.iiser_tirupati.Fragment.Fragment_Attendence;
import com.mobilesutra.iiser_tirupati.Model.DTO_Courses_offered;
import com.mobilesutra.iiser_tirupati.Model.DTO_Stud_Attendence;
import com.mobilesutra.iiser_tirupati.R;
import com.mobilesutra.iiser_tirupati.Tabs.SlidingTabLayout;
import com.mobilesutra.iiser_tirupati.background.IISERIntentService;
import com.mobilesutra.iiser_tirupati.banner.Animations.DescriptionAnimation;
import com.mobilesutra.iiser_tirupati.banner.SliderLayout;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.BaseSliderView;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.TextSliderView;
import com.mobilesutra.iiser_tirupati.banner.Tricks.ViewPagerEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class ActivityAttendence extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener{

    Context context = null;
    RecyclerView recycler_view = null;
    public List<DTO_Courses_offered> course_List = new ArrayList<>();
    ActivityAssignment.EventAdapter recyclerAdapter = null;
    LinearLayoutManager linearLayoutManager = null;
    Fragment_Attendence first = null;
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
    private ArrayList<DTO_Stud_Attendence> presentStudArraylist;
    Button add_courses = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_attendence);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.fragment_three_new);

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
     //   get_studenent_attendence();
      //  bindComponentData();
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

    private void get_studenent_attendence()
    {
        try {
            String json_response = "{\n" +
                    "\t\"response_status\": 1,\n" +
                    "\t\"roll_number\": \"201801183\",\n" +
                    "\t\"course_info\": [{\n" +
                    "\t\t\t\"course_code\": \"BIO121\",\n" +
                    "\t\t\t\"tot_no_classes\": \"1\",\n" +
                    "\t\t\t\"average_attendance\": \"100%\",\n" +
                    "\t\t\t\"present\": [{\n" +
                    "\t\t\t\t\t\"date\": \"2020-01-02\",\n" +
                    "\t\t\t\t\t\"course_code\": \"BIO121\",\n" +
                    "\t\t\t\t\t\"student_present\": \"1\",\n" +
                    "\t\t\t\t\t\"lecture_present\": 1\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"date\": \"2020-01-05\",\n" +
                    "\t\t\t\t\t\"course_code\": \"BIO121\",\n" +
                    "\t\t\t\t\t\"student_present\": \"0\",\n" +
                    "\t\t\t\t\t\"lecture_present\": 1\n" +
                    "\t\t\t\t},\n" +
                    "\t\t\t\t{\n" +
                    "\t\t\t\t\t\"date\": \"2020-01-07\",\n" +
                    "\t\t\t\t\t\"course_code\": \"BIO121\",\n" +
                    "\t\t\t\t\t\"student_present\": \"1\",\n" +
                    "\t\t\t\t\t\"lecture_present\": 1\n" +
                    "\t\t\t\t}, {\n" +
                    "\t\t\t\t\t\"date\": \"2020-01-08\",\n" +
                    "\t\t\t\t\t\"course_code\": \"BIO121\",\n" +
                    "\t\t\t\t\t\"student_present\": \"1\",\n" +
                    "\t\t\t\t\t\"lecture_present\": 1\n" +
                    "\t\t\t\t}, {\n" +
                    "\t\t\t\t\t\"date\": \"2020-01-11\",\n" +
                    "\t\t\t\t\t\"course_code\": \"BIO121\",\n" +
                    "\t\t\t\t\t\"student_present\": \"0\",\n" +
                    "\t\t\t\t\t\"lecture_present\": 1\n" +
                    "\t\t\t\t}, {\n" +
                    "\t\t\t\t\t\"date\": \"2020-01-14\",\n" +
                    "\t\t\t\t\t\"course_code\": \"BIO121\",\n" +
                    "\t\t\t\t\t\"student_present\": \"1\",\n" +
                    "\t\t\t\t\t\"lecture_present\": 1\n" +
                    "\t\t\t\t}, {\n" +
                    "\t\t\t\t\t\"date\": \"2020-01-15\",\n" +
                    "\t\t\t\t\t\"course_code\": \"BIO121\",\n" +
                    "\t\t\t\t\t\"student_present\": \"0\",\n" +
                    "\t\t\t\t\t\"lecture_present\": 0\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t]\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"course_code\": \"BIO443\",\n" +
                    "\t\t\t\"tot_no_classes\": false,\n" +
                    "\t\t\t\"average_attendance\": \"%\",\n" +
                    "\t\t\t\"present\": []\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"course_code\": \"BIO461\",\n" +
                    "\t\t\t\"tot_no_classes\": false,\n" +
                    "\t\t\t\"average_attendance\": \"%\",\n" +
                    "\t\t\t\"present\": []\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"course_code\": \"CHM323\",\n" +
                    "\t\t\t\"tot_no_classes\": false,\n" +
                    "\t\t\t\"average_attendance\": \"%\",\n" +
                    "\t\t\t\"present\": []\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"course_code\": \"CHM443\",\n" +
                    "\t\t\t\"tot_no_classes\": false,\n" +
                    "\t\t\t\"average_attendance\": \"%\",\n" +
                    "\t\t\t\"present\": []\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"course_code\": \"MTH122\",\n" +
                    "\t\t\t\"tot_no_classes\": \"5\",\n" +
                    "\t\t\t\"average_attendance\": \"70%\",\n" +
                    "\t\t\t\"present\": [{\n" +
                    "\t\t\t\t\"date\": \"2019-10-14\",\n" +
                    "\t\t\t\t\"course_code\": \"MTH122\",\n" +
                    "\t\t\t\t\"student_present\": \"1\",\n" +
                    "\t\t\t\t\"lecture_present\": 1\n" +
                    "\t\t\t}]\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"course_code\": \"ECS321\",\n" +
                    "\t\t\t\"tot_no_classes\": false,\n" +
                    "\t\t\t\"average_attendance\": \"%\",\n" +
                    "\t\t\t\"present\": []\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"course_code\": \"MTH322\",\n" +
                    "\t\t\t\"tot_no_classes\": \"1\",\n" +
                    "\t\t\t\"average_attendance\": \"100%\",\n" +
                    "\t\t\t\"present\": [{\n" +
                    "\t\t\t\t\"date\": \"2019-10-14\",\n" +
                    "\t\t\t\t\"course_code\": \"MTH322\",\n" +
                    "\t\t\t\t\"student_present\": \"1\",\n" +
                    "\t\t\t\t\"lecture_present\": 1\n" +
                    "\t\t\t}]\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"course_code\": \"MTH342\",\n" +
                    "\t\t\t\"tot_no_classes\": false,\n" +
                    "\t\t\t\"average_attendance\": \"%\",\n" +
                    "\t\t\t\"present\": []\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"course_code\": \"MTH422\",\n" +
                    "\t\t\t\"tot_no_classes\": false,\n" +
                    "\t\t\t\"average_attendance\": \"%\",\n" +
                    "\t\t\t\"present\": []\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"course_code\": \"PHY321\",\n" +
                    "\t\t\t\"tot_no_classes\": false,\n" +
                    "\t\t\t\"average_attendance\": \"%\",\n" +
                    "\t\t\t\"present\": []\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"course_code\": \"PHY341\",\n" +
                    "\t\t\t\"tot_no_classes\": false,\n" +
                    "\t\t\t\"average_attendance\": \"%\",\n" +
                    "\t\t\t\"present\": []\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "\t\t\t\"course_code\": \"PHY426\",\n" +
                    "\t\t\t\"tot_no_classes\": false,\n" +
                    "\t\t\t\"average_attendance\": \"%\",\n" +
                    "\t\t\t\"present\": []\n" +
                    "\t\t}\n" +
                    "\t]\n" +
                    "}";

       //     IISERApp.log(LOG_TAG, "Response is " + dtoService.getStr_response_body());

            JSONObject json = new JSONObject(json_response);
            if (json.getString("response_status").equalsIgnoreCase("1")) {

                String json_roll_number = json.getString("roll_number");
              //  IISERApp.set_session(IISERApp.SESSION_USER_TYPE, json_user_type);
                TABLE_ATTENDENCE_MASTER.deleteAllRecord();
                TABLE_STUDENT_ATTENDENCE.deleteAllRecord();
                JSONArray courseArray = json.getJSONArray("course_info");
           for (int i = 0; i < courseArray.length();i++)
           {
               JSONObject jsonObj = courseArray.getJSONObject(i);
               String coursecode = jsonObj.getString("course_code");
               String tot_no_classes = jsonObj.getString("tot_no_classes");
               String average_attendance = jsonObj.getString("average_attendance");
               TABLE_ATTENDENCE_MASTER.insertAttendenceMaster(coursecode,tot_no_classes,tot_no_classes,average_attendance);
               JSONArray presentJsonArray = jsonObj.getJSONArray("present");
               for (int j =0; j < presentJsonArray.length();j++)
               {
                   JSONObject jsonObj_present = presentJsonArray.getJSONObject(j);
                   String date = jsonObj_present.getString("date");
                   String course_code = jsonObj_present.getString("course_code");
                   String student_present = jsonObj_present.getString("student_present");
                   String lecture_present = jsonObj_present.getString("lecture_present");
                   TABLE_STUDENT_ATTENDENCE.insertStudentAttendence(date,course_code,student_present,lecture_present);
               }
           }







            } else {
               /* bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "0");*/
                //broadcast(context, "ActivityLogin", bundle);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getIntentData() {
    }

    private void initComponentData() {
        fab_refresh = (FloatingActionButton) findViewById(R.id.fab);
        fab_refresh.setVisibility(View.VISIBLE);
        fab_add_assingment = (FloatingActionButton) findViewById(R.id.fab_add_assingment);
      //  fab_add_assingment.setVisibility(View.GONE);


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

        add_courses = (Button) findViewById(R.id.add_course);

        add_process = (TextView) findViewById(R.id.add_process);
        //add_process.setVisibility(View.GONE);
        add_process.setText("ATTENDANCE");
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
                Toast.makeText(ActivityAttendence.this, "Please wait...Refreshing Data.", Toast.LENGTH_LONG).show();

                refresh_data();

            }
        });
/*        fab_add_assingment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (((ActivityHome)getParent()).getPermissionCount() >0)
                        ((ActivityHome)getParent()).check_app_permission();
                    else {
                        Intent in=new Intent(ActivityAttendence.this,ActivityAddAssignment.class);
                        startActivity(in);
                        //add_new_assignment();
                    }
                } else {
                    Intent in=new Intent(ActivityAttendence.this,ActivityAddAssignment.class);
                    startActivity(in);
                    //add_new_assignment();
                }

            }
        });*/

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
    public void onSliderClick(BaseSliderView slider) {

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
        public EventAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
            return new EventAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(EventAdapter.MyViewHolder holder, final int position)
        {
            DTO_Courses_offered assign = course_List.get(position);
            Log.d("Fragment", "course name:" + assign.getStr_course_name());
            holder.course_name.setText(assign.getStr_course_name());


         /*   holder.lnrLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("Fragment", "position of view:" + position);
                    Intent intent = new Intent(context, ActivityCourseDetails.class);
                    intent.putExtra("position", String.valueOf(position));
                    startActivity(intent);
                }
            });*/
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
       /* Toast.makeText(ActivityAttendence.this, "Please wait..refreshing data.", Toast.LENGTH_SHORT).show();
        refresh_data();
        Toast.makeText(ActivityAttendence.this, "Data refreshed successfully.", Toast.LENGTH_SHORT).show();
*/
        IISERApp.log(LOG_TAG, "Awak onresume-->");
        context.registerReceiver(mMessageReceiver, new IntentFilter(LOG_TAG));


        if (IISERApp.get_session(IISERApp.SESSION_ASSIGNMENT_DOCUMENT_FLAG).equalsIgnoreCase("Y"))
        {
           // set_document_name();
        }
        int count = TABLE_COURSE.get_selected_course_list();

        if (count == 0) {
            ///  listTabs.clear();
            listTabs.clear();
            objSlidingTabLayout = null;
            populateViewPager();
            if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("student")) {

                if (count == 0)

                {
                    add_courses.setVisibility(View.VISIBLE);

                    TABLE_TIMETABLE.delete_tbl_timetable_sync();




                    add_courses.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            add_courses.setVisibility(View.GONE);
                            Intent i = new Intent(ActivityAttendence.this, ActivityProfile.class);
                            startActivity(i);

                        }
                    });
                    Toast.makeText(ActivityAttendence.this, "Please select courses...", Toast.LENGTH_SHORT).show();
                }


             //   Toast.makeText(ActivityAttendence.this, "Please select courses...", Toast.LENGTH_SHORT).show();
            }
            // Toast.makeText(ActivityAssignment.this, "Courses are selected...", Toast.LENGTH_SHORT).show();
        } else {

          /*  listTabs.clear();
            objSlidingTabLayout=null;
            populateViewPager();*/
            add_courses.setVisibility(View.GONE);
            IISERApp.log("XYZ", " onResumeFragments():");
            IISERApp.set_session("onRES", "one");
       /* first=new Fragment_Assign();
        first.refreshData();*/
            int size = listTabs.size();
            IISERApp.log(LOG_TAG, "size_of_tabs is:" + size);
            for (int i = 0; i < size; i++) {
                first = (Fragment_Attendence) objViewPagerAdapter.instantiateItem(mViewPager, i);
                first.refreshData();
            }
        }
        // this.onCreate(null);

    }



    void populateViewPager()
    {
    //  if (objSlidingTabLayout == null) {
            try {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
             //   mViewPager.setOffscreenPageLimit(0);
//mViewPager.setOnPageChangeListener(this);

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
                Log.d("inonPageScrolled","");

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("onPageSelected","");
               CharSequence title = listTabs.get(position).getTitle();
                Log.d("onPageSelected ",String.valueOf(title));
                objSlidingTabLayout.set_selected_text_color(position);


              /* int item = mViewPager.getCurrentItem();
                first = (Fragment_Attendence) objViewPagerAdapter.instantiateItem(mViewPager, item);
                presentStudArraylist = TABLE_STUDENT_ATTENDENCE.getPresentDatesForStud(title.toString());
                first.cv.updateCalendar(presentStudArraylist);*/
             //   listTabs.get(item).createFragment();
               /* IISERApp.set_session(IISERApp.SESSION_TAB_TITLE, String.valueOf(title));
                String tabTitel = IISERApp.get_session((IISERApp.SESSION_TAB_TITLE));
                Log.d("tabtitleinfrag ",tabTitel);*/
              //  objViewPagerAdapter.notifyDataSetChanged();


            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("onPageScrollStateChan","");
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

}

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
            return Fragment_Attendence.newInstance(mTitle.toString());


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

        private Map<Integer, String> mFragmentTags;
        private FragmentManager mFragmentManager;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
            mFragmentTags = new HashMap<Integer, String>();
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
        public Object instantiateItem(ViewGroup container, int position) {
            Object object = super.instantiateItem(container, position);
            if (object instanceof Fragment) {
                Fragment fragment = (Fragment) object;
                String tag = fragment.getTag();
                mFragmentTags.put(position, tag);
            }
            return object;
        }
        
        @Override
        public CharSequence getPageTitle(int position) {
            return listTabs.get(position).getTitle();
        }

        public Fragment getFragment(int position) {
            Fragment fragment = null;
            String tag = mFragmentTags.get(position);
            if (tag != null) {
                fragment = mFragmentManager.findFragmentByTag(tag);
            }
            return fragment;
        }

    }


    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            IISERApp.log(LOG_TAG, "messege recieved");
            Bundle bundle = intent.getExtras();
            add_courses.setVisibility(View.GONE);
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
                first = (Fragment_Attendence) objViewPagerAdapter.instantiateItem(mViewPager, i);
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
            first = (Fragment_Attendence) objViewPagerAdapter.instantiateItem(mViewPager, i);
            first.refreshData();
        }

        listTabs.clear();
        populateViewPager();
    }

    public void refresh_data() {

        if (((IISERApp) getApplicationContext()).isInternetAvailable()) {
            IISERApp.log(LOG_TAG, "button is pressed for refreshing in notice ");
            //   prgress_dialog = ProgressDialog.show(context, null, null);
            Intent intent1 = new Intent(context, IISERIntentService.class);
            IISERApp.log(LOG_TAG, "App.INTENT_FLAG:" + IISERApp.INTENT_FLAG + " App.INTENT_FLAG_GET_EXAM:" + IISERApp.INTENT_FLAG_ASSIGNMENT);
            intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_STUD_ATTENDENCE);
            intent1.putExtra("Activity_name", "ActivityAttendence");
            context.startService(intent1);
        } else {
            ((IISERApp) getApplicationContext()).isInternetAvailable();
        }
    }


}
