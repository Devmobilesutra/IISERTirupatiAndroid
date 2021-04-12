package com.mobilesutra.iiser_tirupati.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilesutra.iiser_tirupati.Activities.ActivityAttendence;
import com.mobilesutra.iiser_tirupati.Activities.ActivityAttendenceFaculty;
import com.mobilesutra.iiser_tirupati.Activities.ActivityHome;
import com.mobilesutra.iiser_tirupati.Activities.CalendarView;
import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Database.TABLE_ATTENDENCE_MASTER;
import com.mobilesutra.iiser_tirupati.Database.TABLE_FACULTY_ATTENDENCE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_STUDENT_ATTENDENCE;
import com.mobilesutra.iiser_tirupati.Model.DTOService;
import com.mobilesutra.iiser_tirupati.Model.DTO_Faculty_Attendence;
import com.mobilesutra.iiser_tirupati.Model.DTO_Stud_Attendence;
import com.mobilesutra.iiser_tirupati.R;
import com.mobilesutra.iiser_tirupati.Tabs.SlidingTabLayout;
import com.mobilesutra.iiser_tirupati.banner.SliderLayout;
import com.mobilesutra.iiser_tirupati.view.ProgressUtil;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;


public class FragmentFacultyAttendence extends Fragment {
    Context context = null;

    // EventAdapter recyclerAdapter = null;
    LinearLayoutManager linearLayoutManager = null;
    String LOG_TAG = "Fragment_Asssign", tab_title = "", filename = "";
    ProgressDialog dialog = null;

    SliderLayout mDemoSlider = null;
    View view = null;

    private static final String ARG_PARAM_TITLE = "title";
    private static final String ARG_PARAM_facultyOrMentor = "facultyOrMentor";
    private RecyclerView recyclerview;
    private ArrayList<DTO_Faculty_Attendence> attendence_RollNo_percentage;
    private EventAdapter adapter;
    DecimalFormat df = new DecimalFormat("#.##");
    private LinearLayout calenderLayout;
    public LinearLayout recyclerView_layout;
    private CalendarView cv;
    private ArrayList<DTO_Stud_Attendence> presentStudArraylist;
    private Button backButton;
    private ActivityAttendenceFaculty Acti_attendence;
    private ProgressDialog progressDialog;
    public FrameLayout framelayout;
    public ViewPager mViewPager;
    private List<PagerItem> listTabs = new ArrayList<PagerItem>();
    private ViewPagerAdapter objViewPagerAdapter;
    public SlidingTabLayout objSlidingTabLayout_mentor;
    private Button backButton_mentor;
    private FragmentFacultyAttendence myContext;
    private String tab_facultyOrMentor;
    private TextView tv_totalNoClasses;
    private TextView tv_total_present;
    private TextView tv_total_percentage;
    private LinearLayout mentor_VP_linearlayout;
    private TextView tv_rollNo;
    private TextView tv_rollnoNotassigned;
    private ProgressBar progressBar_cyclic;


    public FragmentFacultyAttendence() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentFacultyAttendence newInstance(String str_title,String mentorVal) {
        FragmentFacultyAttendence fragment = new FragmentFacultyAttendence();
        Log.d("Fragment", "In newInstance");
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM_TITLE, str_title);
        bundle.putString(ARG_PARAM_facultyOrMentor, mentorVal);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity().getApplicationContext();
        Acti_attendence = (ActivityAttendenceFaculty) getActivity();
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_five, container, false);
     //   if (view == null) {
            view = inflater.inflate(R.layout.fragment_faculty_attendence, container, false);

            Bundle args = getArguments();
            IISERApp.log(LOG_TAG, "tab title next:" + args.getCharSequence(ARG_PARAM_TITLE).toString());
            tab_title = args.getCharSequence(ARG_PARAM_TITLE).toString();
            tab_facultyOrMentor = args.getCharSequence(ARG_PARAM_facultyOrMentor).toString();
            LOG_TAG = LOG_TAG + tab_title;

            initComponentData(view);
            initComponentListeners();
            bindComponentData();

     //   }
        return view;
        // Inflate the layout for this fragment
    }

    private void initComponentData(View view) {

        tv_rollnoNotassigned = (TextView)view.findViewById(R.id.tv_rollnoNotassigned);
        recyclerview = (RecyclerView) view.findViewById(R.id.rv_rollnumber);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setHasFixedSize(true);
        calenderLayout = (LinearLayout) view.findViewById(R.id.calenderGrird);
        tv_totalNoClasses = (TextView) view.findViewById(R.id.tv_totalNo_classes);
        tv_total_present = (TextView) view.findViewById(R.id.tv_total_present);
        tv_total_percentage = (TextView) view.findViewById(R.id.tv_total_percentage);
        recyclerView_layout = (LinearLayout)view.findViewById(R.id.recyclerView_layout);
        cv = (CalendarView)view.findViewById(R.id.calendar_view);
        backButton = (Button) view.findViewById(R.id.btn_back);
        backButton_mentor = (Button) view.findViewById(R.id.btn_back_mentor);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        objSlidingTabLayout_mentor = (SlidingTabLayout) view.findViewById(R.id.tabs_mentor);
        mentor_VP_linearlayout =(LinearLayout)view.findViewById(R.id.mentor_vp_linearlayout);
        tv_rollNo = (TextView) view.findViewById(R.id.tv_rollNo);
      //  progressBar_cyclic = (ProgressBar)view.findViewById(R.id.progressBar_cyclic);

       // framelayout =(FrameLayout)view.findViewById(R.id.frame_container);

       /* if(tab_facultyOrMentor.equalsIgnoreCase("faculty"))
        {
            recyclerView_layout.setVisibility(View.VISIBLE);
            calenderLayout.setVisibility(View.GONE);
            backButton.setVisibility(View.GONE);
        }else if(tab_facultyOrMentor.equalsIgnoreCase("mentor"))
        {
            recyclerView_layout.setVisibility(View.GONE);
            calenderLayout.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.VISIBLE);
        }*/

    }



   /* @Override
    public void onResume() {
        super.onResume();
        recyclerView_layout.setVisibility(View.VISIBLE);
        calenderLayout.setVisibility(View.GONE);
        backButton.setVisibility(View.GONE);
        mViewPager.setVisibility(View.GONE);
        objSlidingTabLayout_mentor.setVisibility(View.GONE);
     //   framelayout.setVisibility(View.GONE);
    }*/

    @Override
    public void onPause() {
        super.onPause();
        Log.d("call","onStart");
        recyclerView_layout.setVisibility(View.VISIBLE);
        calenderLayout.setVisibility(View.GONE);
        backButton.setVisibility(View.GONE);
        mViewPager.setVisibility(View.GONE);
        objSlidingTabLayout_mentor.setVisibility(View.GONE);
    }

    private void bindComponentData()
    {
      //  recyclerView_layout = (LinearLayout)view.findViewById(R.id.recyclerView_layout);
        if (view != null) {
           /* IISERApp.set_session(IISERApp.SESSION_TAB_TITLE, tab_title);
            String tabTitel = IISERApp.get_session((IISERApp.SESSION_TAB_TITLE));
            Log.d("tabtitleinfrag ",tabTitel);*/
            attendence_RollNo_percentage = TABLE_FACULTY_ATTENDENCE.getrollNumberFaculty(tab_title);

            Log.d("AttendaceFaculty", "AttendaceFaculty: "+attendence_RollNo_percentage);

            if(attendence_RollNo_percentage.size() == 0)
            {
                tv_rollnoNotassigned.setVisibility(View.VISIBLE);
            }else {
                adapter = new EventAdapter(attendence_RollNo_percentage,tab_title);
                recyclerview.setAdapter(adapter);
            }

           // recyclerView_layout.setVisibility(View.VISIBLE);
        }


        /*for (int i = 0; i < 10; i++) {
            DTO_Assign assignment = new DTO_Assign("Assignment 1", "Description", "APR", "18", "Dr.Aloke Das");
            assign_List.add(assignment);
        }*/


    }

    private void initComponentListeners() {

       /* HashSet<Date> events = new HashSet<>();
        events.add(new Date());
        presentStudArraylist = TABLE_STUDENT_ATTENDENCE.getPresentDatesForStud(tab_title);
        cv.updateCalendar(presentStudArraylist);*/
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView_layout.setVisibility(View.VISIBLE);
                calenderLayout.setVisibility(View.GONE);
                backButton.setVisibility(View.GONE);
            }
        });

        backButton_mentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView_layout.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.GONE);
                objSlidingTabLayout_mentor.setVisibility(View.GONE);
                backButton_mentor.setVisibility(View.GONE);
                Acti_attendence.objSlidingTabLayout.setVisibility(View.VISIBLE);
                mentor_VP_linearlayout.setVisibility(View.GONE);

            }
        });

        cv.setEventHandler(new CalendarView.EventHandler() {
            @Override
            public void onDayPress(Date date) {

                // show returned day
                //  MyApplication.ChangeMilkSchedule(context, null, null, 1, date);

            }
        });

        cv.setEventHandlerMonthPrev(new CalendarView.EventHandlerMonthPrev() {
            @Override
            public void onDayPressNew(String date) {
                IISERApp.log(LOG_TAG, "DATE in dashboard : " + date);
                String firstDate = "01-" + date;
                String lastDate = IISERApp.getLastDateOfMonth("01-" + date);
                presentStudArraylist = TABLE_STUDENT_ATTENDENCE.getPresentDatesForStud(tab_title);
                cv.updateCalendar(presentStudArraylist,tab_title);
                //   setCowMilkandBuffaloMilk(firstDate, lastDate);

            }

        });
        cv.setEventHandlerMonthNext(new CalendarView.EventHandlerMonthNext() {
            @Override
            public void onDayPressNext(String date) {
                IISERApp.log(LOG_TAG, "DATE in dashboard Next Month: " + date);
                IISERApp.log(LOG_TAG, "DATE in dashboard : " + date);
                String firstDate = "01-" + date;
                String lastDate = IISERApp.getLastDateOfMonth("01-" + date);
                presentStudArraylist = TABLE_STUDENT_ATTENDENCE.getPresentDatesForStud(tab_title);
                cv.updateCalendar(presentStudArraylist,tab_title);
                // setCowMilkandBuffaloMilk(firstDate, lastDate);
            }

        });



    }

    public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

        private List<DTO_Faculty_Attendence> rollNumber_List;
        String tabTitle;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_rollnumber,tv_percentage;
            public ProgressBar progressAttendence;


            public MyViewHolder(View view) {
                super(view);
                tv_rollnumber = (TextView) view.findViewById(R.id.txt_rollNumber);
                tv_percentage = (TextView) view.findViewById(R.id.txt_percentage);
                 progressAttendence = (ProgressBar) view.findViewById(R.id.progressbar_attendence);


            }
        }


        public EventAdapter(List<DTO_Faculty_Attendence> assign_List,String tabtitle) {
            this.rollNumber_List = assign_List;
            this.tabTitle = tabtitle;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_layout_for_faculty_attendence, parent, false);

           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   TextView rollNumber = (TextView) view.findViewById(R.id.txt_rollNumber);
                   Toast.makeText(getContext(),"item is clicked :"+rollNumber.getText().toString() +" tab :"+tabTitle,Toast.LENGTH_SHORT).show();

                   if(tabTitle.equals("MENTOR PhD"))
                   {
                      /* ActivityAttendenceFaculty.objSlidingTabLayout.setVisibility(View.GONE);
                       recyclerView_layout.setVisibility(View.GONE);*/
                       /* final FragmentTransaction ft = getFragmentManager().beginTransaction();
                   ft.replace(R.id.frame_container, new MentorPhdFragment(), "MentorPhdFragment");
                   ft.commit();*/


                       // Within the activity
                  /*     FragmentTransaction ft = getFragmentManager().beginTransaction();
                       MentorPhdFragment fragmentDemo = MentorPhdFragment.newInstance(tabTitle);
                       ft.replace(R.id.frame_container, fragmentDemo);
                       ft.commit();*/
                       getStudenetAttendenceFromserverForFaculty(rollNumber.getText().toString());
                     //  recyclerView_layout.setVisibility(View.GONE);
                    //   framelayout.setVisibility(View.VISIBLE);
                    //   Acti_attendence.objSlidingTabLayout.setVisibility(View.GONE);
                       Acti_attendence.add_process.setText("MENTOR PhD");
                      /* Fragment childFragment = new MentorPhdFragment();
                       FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                       transaction.replace(R.id.frame_container, childFragment).commit();*/

                      //  ActivityAttendenceFaculty.add_process.setText("MENTOR PhD");
                     //  MentorPhdFragment.newInstance(tabTitle);
                   }else {
                       getStudenetAttendenceFromserverForFaculty(rollNumber.getText().toString());
//                       DownloadPdfPageTask task = new DownloadPdfPageTask(rollNumber.getText().toString());
//                       task.execute();
                   }







               }
           });
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            DTO_Faculty_Attendence assign = rollNumber_List.get(position);
            holder.tv_rollnumber.setText(assign.getRollNumber());
         //   holder.tv_percentage.setText(df.format(Double.parseDouble(assign.getPercentage())));
         //   Log.d("percentage : ",assign.getPercentage());
            String val = assign.getPercentage();
            if(!assign.getPercentage().equals(""))
            {
                String percentage = assign.getPercentage().replace("%","");
                holder.tv_percentage.setText(df.format(Double.parseDouble(percentage)) +"%");
                holder.progressAttendence.setProgress((int)Double.parseDouble(percentage));
            }

         /*   holder.sub_name.setText(assign.getStr_sub_name());
            holder.teacher_name.setText(assign.getStr_teacher_name());*/
            //  holder.lesson.setText(assign.getStr_lesson());

        }

        @Override
        public int getItemCount() {
            return rollNumber_List.size();
        }


    }

    public void refreshData() {
        bindComponentData();
    }

    public void refreshComponent() {
        recyclerView_layout.setVisibility(View.VISIBLE);

        mViewPager.setVisibility(View.GONE);
        objSlidingTabLayout_mentor.setVisibility(View.GONE);
    }



    private void getStudenetAttendenceFromserverForFaculty(String rollNumber) {

       /* progressDialog = ProgressDialog.show(context,
                getContext().getResources().getString(R.string.app_name),
                "Please wait..", false, false);
        progressDialog.setCancelable(false);*/
     //   ProgressUtil.showProgressBar(context, fralmeRootlay, R.id.progressBar);
      //  progressBar_cyclic.setVisibility(View.VISIBLE);

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        /*RequestBody params = new FormEncodingBuilder()
                .build();*/
        JSONObject json1= new JSONObject();
        try {
            json1.put("roll_number",rollNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody params = RequestBody.create(JSON, json1.toString());
        /*Request request = new Request.Builder()
                .build();
*/
   /*     RequestBody params = new FormEncodingBuilder()
                .add("roll_number", IISERApp.get_session(IISERApp.SESSION_USERNAME))
                .build();*/

        IISERApp.log(LOG_TAG, "insert_user_data(), params -> "+params.toString());

        Bundle bundle = new Bundle();
        DTOService dtoService = IISERApp.post_server_call(IISERApp.url_get_stud_attendence, params);
        bundle.putString(IISERApp.BUNDLE_RESPONSE_CODE, dtoService.getStr_response_code() + "");
        bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "1");
        if (dtoService.getStr_response_code() == 200) {

            try {
               /* if (progressDialog != null)
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();*/
             //   progressBar_cyclic.setVisibility(View.INVISIBLE);
                IISERApp.log(LOG_TAG, "Response is " + dtoService.getStr_response_body());

                JSONObject json = new JSONObject(dtoService.getStr_response_body());
                if (json.getString("response_status").equalsIgnoreCase("1")) {

                    String json_roll_number = json.getString("roll_number");
                    //  IISERApp.set_session(IISERApp.SESSION_USER_TYPE, json_user_type);
                    TABLE_ATTENDENCE_MASTER.deleteAllRecord();
                    TABLE_STUDENT_ATTENDENCE.deleteAllRecord();
                    JSONArray courseArray = json.getJSONArray("course_info");
                     String tot_no_classes="",attended_classes="",average_attendance="";
                    for (int i = 0; i < courseArray.length();i++)
                    {
                        JSONObject jsonObj = courseArray.getJSONObject(i);
                        String coursecode = jsonObj.getString("course_code");
                         tot_no_classes = jsonObj.getString("tot_no_classes");
                         attended_classes = jsonObj.getString("attended_classes");
                         average_attendance = jsonObj.getString("average_attendance");
                        TABLE_ATTENDENCE_MASTER.insertAttendenceMaster(coursecode,tot_no_classes,attended_classes,average_attendance);
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

                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");

                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "1");

                    if(tab_title.equals("MENTOR PhD"))
                    {
                        populateViewPager();
                        tv_rollNo.setText("Roll No : "+rollNumber);
                      //  Acti_attendence.populateViewPager("mentor");
                       /* Fragment childFragment = new MentorPhdFragment();
                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    //    transaction.addToBackStack("MentorPhdFragment");
                        transaction.replace(R.id.frame_container, childFragment).commit();*/
                    }else {
                        recyclerView_layout.setVisibility(View.GONE);
                        calenderLayout.setVisibility(View.VISIBLE);
                        backButton.setVisibility(View.VISIBLE);
                     //   Acti_attendence.mViewPager.setVisibility(View.GONE);
                        showCalenderview(tot_no_classes,attended_classes,average_attendance,tab_title);
                    }



                    //  broadcast(context, "ActivityLogin", bundle);

                    //  updateMyActivity(context, "2", IISERApp.BUNDLE_RESPONSE_MESSAGE,"ActivityLogin");
                /* Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                   intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_EXAM_SCHEDULE);
                   context.startService(intent1);*/

                    /*Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                    intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_NOTICE);
                    intent1.putExtra("Activity_name", "ActivityLogin");
                    context.startService(intent1);*/

                } else {
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_MESSAGE, dtoService.getStr_response_mesaage() + "");
                    bundle.putString(IISERApp.BUNDLE_RESPONSE_STATUS, "0");
                    //broadcast(context, "ActivityLogin", bundle);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
         //   progressBar_cyclic.setVisibility(View.INVISIBLE);
          /* if (progressDialog != null)
                if (progressDialog.isShowing())
                    progressDialog.dismiss();*/
            bundle.putString(IISERApp.BUNDLE_EXCEPTION, "\n" + "ErrorCode:" + dtoService.getStr_exception());
        }

      //  broadcast(context, "ActivityLogin", bundle);

    }


    void populateViewPager()
    {
        //  if (objSlidingTabLayout == null) {
        try {
         //   mViewPager = (ViewPager) view.findViewById(R.id.viewpager);

            backButton_mentor.setVisibility(View.VISIBLE);
            recyclerView_layout.setVisibility(View.GONE);
            Acti_attendence.objSlidingTabLayout.setVisibility(View.GONE);
            mViewPager.setVisibility(View.VISIBLE);
            objSlidingTabLayout_mentor.setVisibility(View.VISIBLE);
            mentor_VP_linearlayout.setVisibility(View.VISIBLE);
//mViewPager.setOnPageChangeListener(this);


            LinkedHashMap<String, String> lhm=null;
            IISERApp.log(LOG_TAG,"in else if of session ");
            lhm = TABLE_ATTENDENCE_MASTER.get_Faculty_stud_courseCode();


            int lhmLength = lhm.size();

            IISERApp.log(LOG_TAG, "Course_lhm:" + lhm);


            if (lhmLength > 0) {

                Set<String> keys = lhm.keySet();
                listTabs.clear();
                for (String key : keys) {
                    IISERApp.log(LOG_TAG, "key:" + key);
                    listTabs.add(new PagerItem(
                            key, // Title
                            0, // Indicator color
                            0 // Divider color
                    ));
                }

            }


            objViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
            mViewPager.setAdapter(objViewPagerAdapter);

        //    objSlidingTabLayout_mentor = (SlidingTabLayout) view.findViewById(R.id.tabs_mentor);
            objSlidingTabLayout_mentor.setViewPager(mViewPager);
            objSlidingTabLayout_mentor.set_selected_text_color(0);
            int item = mViewPager.getCurrentItem(); // get

           // IISERApp.set_session(IISERApp.SESSION_TAB_TITLE, String.valueOf(listTabs.get(item).getTitle()));

            objSlidingTabLayout_mentor.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    Log.d("inonPageScrolled","");

                }

                @Override
                public void onPageSelected(int position) {
                    Log.d("onPageSelected", "");

                    CharSequence title = listTabs.get(position).getTitle();
                    Log.d("onPageSelected ", String.valueOf(title));
                 //   IISERApp.set_session(IISERApp.SESSION_TAB_TITLE, String.valueOf(title));
                 //   String tabTitel = IISERApp.get_session((IISERApp.SESSION_TAB_TITLE));
                    Log.d("tabtitleinfrag ", String.valueOf(title));
                    objSlidingTabLayout_mentor.set_selected_text_color(position);




                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    Log.d("onPageScrollStateChan","");
                }
            });
            // objSlidingTabLayout.set_current_tab(RVFApp.get_Intsession(RVFApp.SESSION_CURRENT_TAB));
            objSlidingTabLayout_mentor.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

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


    class PagerItem {
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
            /*Fragment fragment;
            if(mTitle.toString().equals("MENTOR PhD"))
            {
               fragment = MentorPhdFragment.newInstance(mTitle.toString());
            }else {
                fragment = FragmentFacultyAttendence.newInstance(mTitle.toString());
            }
            return fragment;*/

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

    private void showCalenderview(String tot_no_classes,String attented_classes,String avg_percentage ,String tabtile) {
        tv_totalNoClasses.setText(tot_no_classes);
        tv_total_present.setText(attented_classes);
        tv_total_percentage.setText(avg_percentage);
        HashSet<Date> events = new HashSet<>();
        events.add(new Date());
       // String tabTitel = IISERApp.get_session((IISERApp.SESSION_TAB_TITLE));
        Log.d("tabtitleinfrag ",tabtile);
        presentStudArraylist = TABLE_STUDENT_ATTENDENCE.getPresentDatesForStud(tabtile);

        cv.updateCalendar(presentStudArraylist,tabtile);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
