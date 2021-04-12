package com.mobilesutra.iiser_tirupati.Activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Database.TABLE_COURSE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_EXAM_SCHEDULE;
import com.mobilesutra.iiser_tirupati.Error_Activity.ExceptionHandler;
import com.mobilesutra.iiser_tirupati.Model.DTO_ExamSchedule;
import com.mobilesutra.iiser_tirupati.R;
import com.mobilesutra.iiser_tirupati.background.IISERIntentService;
import com.mobilesutra.iiser_tirupati.banner.Animations.DescriptionAnimation;
import com.mobilesutra.iiser_tirupati.banner.SliderLayout;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.BaseSliderView;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.TextSliderView;
import com.mobilesutra.iiser_tirupati.banner.Tricks.ViewPagerEx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by kalyani on 26/04/2016.
 */
public class ActivityExamSchedule extends Activity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    Context context = null;
    RecyclerView recycler_view = null;
    List<DTO_ExamSchedule> assign_List = null;
    EventAdapter recyclerAdapter = null;
    LinearLayoutManager linearLayoutManager = null;


    SliderLayout mDemoSlider = null;
    FloatingActionButton fab_refresh = null;

    String LOG_TAG = "ActivityExamSchedule";
    int examschedule_list_count = 0;
    View view = null;
    TextView add_process=null;
    TextView user_name=null;

    List<LinkedHashMap<String, String>> exam_schedule_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_fragment_exam_schedule);

        context = this;
        final ScrollView main = (ScrollView) findViewById(R.id.scrollView123);
        main.post(new Runnable() {
            public void run() {
                main.scrollTo(0, 0);
            }
        });


        getIntentData();
        initComponents();
        initComponentData();
        initComponentsListeners();
        displayBanner();
        bindComponentData();

       /* if (((IISERApp) getApplication()).isInternetAvailable()) {
            Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
            intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_EXAM_SCHEDULE);
            context.startService(intent1);
        } else {
            Snackbar.make(recycler_view, "No Internet available...", Snackbar.LENGTH_LONG).show();
        }
*/
    }

    private void initComponentData() {
        fab_refresh = (FloatingActionButton) findViewById(R.id.fab);
        add_process=(TextView)findViewById(R.id.add_process);
        add_process.setVisibility(View.GONE);

        user_name=(TextView)findViewById(R.id.user_name);
        IISERApp.log(LOG_TAG,"session value is"+IISERApp.get_session(IISERApp.SESSION_LOGIN_FLAG));
        String user = IISERApp.get_session((IISERApp.SESSION_STUDENT_NAME));
        String sem_name = IISERApp.get_session((IISERApp.SESSION_SEMESTER_NAME));

        if((IISERApp.get_session(IISERApp.SESSION_LOGIN_FLAG).equalsIgnoreCase("Y"))) {

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

    private void getIntentData() {

    }

    private void initComponents() {
        recycler_view = (RecyclerView) findViewById(R.id.recycler_event_view);
        // assign_List = new ArrayList<DTO_ExamSchedule>();
        linearLayoutManager = new LinearLayoutManager(context);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setHasFixedSize(true);

    }

    private void initComponentsListeners() {
        fab_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityExamSchedule.this, "Please wait...Refreshing Data.", Toast.LENGTH_LONG).show();
                refresh_data();
            }
        });
    }

    private void bindComponentData() {

        assign_List = new ArrayList<>();
        /*if (view != null) {*/
        exam_schedule_list = TABLE_EXAM_SCHEDULE.get_exam_schedule_list();
        //exam_schedule_list=
        assign_List.clear();
        IISERApp.log(LOG_TAG, "exam_schedule_list:" + exam_schedule_list);          //list is getting blank
        examschedule_list_count = exam_schedule_list.size();
        IISERApp.log("LOG_TAG", "COUNT OF EXAM SECH LIST IS:" + exam_schedule_list);


/*
        if(exam_schedule_list.size()==0)
        {

            //Toast.makeText(ActivityExamSchedule.this, "Please select courses", Toast.LENGTH_SHORT).show();
        }
        else {*/
        for (int i = 0; i < examschedule_list_count; i++) {
            String exam_id = exam_schedule_list.get(i).get("exam_id").toString();

            Log.d("", "exam_id_bindComponentData: "+ exam_id);
            String exam_title = exam_schedule_list.get(i).get("exam_title").toString();
            String course_id = exam_schedule_list.get(i).get("course_id").toString();
            String cec_member = exam_schedule_list.get(i).get("cec_member").toString();
            String exam_date = exam_schedule_list.get(i).get("exam_date").toString();
            String exam_time = exam_schedule_list.get(i).get("exam_time").toString();
            String exam_weekday = exam_schedule_list.get(i).get("exam_day").toString();
            String exam_venue = exam_schedule_list.get(i).get("venue").toString();
            String day = IISERApp.get_day_from_date(exam_date);
            String month = IISERApp.get_month_from_date(exam_date);

            DTO_ExamSchedule assignment = new DTO_ExamSchedule(exam_id, day, month, exam_title, course_id, exam_time,
                    exam_date, exam_weekday, cec_member, exam_venue);
            //exam_schedule_list.add(exam_schedule_list)
            assign_List.add(assignment);
            //assignment.add(assign_List);
            // exam_schedule_list.add(assignment);
        }

        // }
        /*for (int i = 0; i < 10; i++) {
            DTO_ExamSchedule assignment = new DTO_ExamSchedule("", "22", "FEB", "Mid Semester Examination-Spring 2016", "BIO 321", "10.00-12.00 pm",
                    "2016-02-22", "Monday", "Mr.Prakash", "LHC 203");
            assign_List.add(assignment);

        }
*/
        recyclerAdapter = new EventAdapter(assign_List);
        recycler_view.setAdapter(recyclerAdapter);

        recyclerAdapter.notifyDataSetChanged();

       /* IISERApp.log(LOG_TAG, "view:" + view);
        if (view != null) {
            if (recyclerAdapter == null) {
                IISERApp.log(LOG_TAG,"recyclerAdapter:null"+recyclerAdapter);
                recyclerAdapter = new EventAdapter(assign_List);
                recycler_view.setAdapter(recyclerAdapter);
            } else {
                IISERApp.log(LOG_TAG,"recyclerAdapter:not null"+recyclerAdapter);
                IISERApp.log(LOG_TAG,"assign_List size clear:"+assign_List.size());
                recyclerAdapter.assign_List.clear();
                IISERApp.log(LOG_TAG, "assign_List size after clear" +assign_List.size());
                recyclerAdapter.assign_List.addAll(assign_List);
                IISERApp.log(LOG_TAG, "assign_List size after addition" + assign_List.size());
                recyclerAdapter.notifyDataSetChanged();
            }
        }*/
    }

    public void displayBanner() {
        //final ViewFlipper MyViewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);


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
       /* } else {*/

          /*  Set<String> keys = lhm.keySet();
            for (String key : keys) {
                TextSliderView textSliderView = new TextSliderView(getContext());
                // initialize a SliderLayout
                textSliderView
                        .description("")
                        .image(lhm.get(key))
                        .setScaleType(BaseSliderView.ScaleType.CenterInside)
                        .setOnSliderClickListener(this);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra", key);

                mDemoSlider.addSlider(textSliderView);
            }*/
          /*  for (int i = 0; i < length; i++) {

                TextSliderView textSliderView = new TextSliderView(this);
                // initialize a SliderLayout
                textSliderView
                        .description("")
                        .image(lhm.get("" + i))
                        .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                        .setOnSliderClickListener(this);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra", lhm.get("" + i));

                mDemoSlider.addSlider(textSliderView);
            }*/
        // }


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

        private List<DTO_ExamSchedule> assign_List;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView exam_date, exam_month, exam_title, exam_time, sub_name, supervisor_name, venue;
            public LinearLayout lnrLayout;
            FloatingActionButton fab_alarm1=null;



            public MyViewHolder(View view) {
                super(view);
                exam_date = (TextView) view.findViewById(R.id.txt_exam_date);
                exam_month = (TextView) view.findViewById(R.id.txt_exam_month);
                exam_title = (TextView) view.findViewById(R.id.txt_exam_title);
                exam_time = (TextView) view.findViewById(R.id.txt_exam_time);
                sub_name = (TextView) view.findViewById(R.id.txt_sub_name);
                //  supervisor_name= (TextView) view.findViewById(R.id.txt_supervisor_name);
                venue = (TextView) view.findViewById(R.id.txt_venue);
                lnrLayout = (LinearLayout) view.findViewById(R.id.lnrlayout_event_cardview);
                fab_alarm1 = (FloatingActionButton)view.findViewById(R.id.fab_alarm1);

            }
        }


        public EventAdapter(List<DTO_ExamSchedule> assign_List) {
            this.assign_List = assign_List;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_exam_schedule_row, parent, false);

          /*  itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(getContext(),ActivityExamScheduleDetails.class);
                    startActivity(intent);
                }
            });*/
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final DTO_ExamSchedule assign = assign_List.get(position);
            holder.exam_date.setText(assign.getStr_exam_day());
            holder.exam_month.setText(assign.getStr_exam_month());
            holder.exam_title.setText(assign.getStr_exam_title());
            holder.exam_time.setText(assign.getStr_exam_time());
            holder.sub_name.setText(assign.getStr_exam_sub_name());
            //  holder.supervisor_name.setText(assign.getStr_exam_supervisr_name());
            holder.venue.setText(assign.getStr_exam_venue());



            holder.fab_alarm1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ActivityExamSchedule.this, "Your event is created successfully...", Toast.LENGTH_LONG).show();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                    final String date_event = assign.getStr_exam_date().toString();

                    Date dt21 = new Date();
                    try {
                        dt21 = sdf.parse(assign.getStr_exam_date().toString() + " 09:00");
                        IISERApp.log(LOG_TAG, "Date is:- " + assign.getStr_exam_date().toString() + " 09:00");
                        IISERApp.log(LOG_TAG, "Parse Date is:- " + dt21);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    final long finalYear = dt21.getTime();

                    long eventID = -1;
                    try {
                        String eventUriString = "content://com.android.calendar/events";
                        ContentValues eventValues = new ContentValues();
                        eventValues.put("calendar_id", 1); // id, We need to choose from
                        // our mobile for primary its 1
                        eventValues.put("title", assign.getStr_exam_title());
                        //eventValues.put("description", desc);
                        //eventValues.put("eventLocation", place);

                        //long endDate = startDate + 1000 * 10 * 10; // For next 10min
                        //String event_date=(String)
                        IISERApp.log(LOG_TAG, "EVENT DATE IS:" + finalYear);
                        eventValues.put("dtstart", finalYear);
                        IISERApp.log(LOG_TAG, "EVENT DATE IS aa:" + new Date(finalYear));
                        eventValues.put("dtend", finalYear);

                        // values.put("allDay", 1); //If it is bithday alarm or such
                        // kind (which should remind me for whole day) 0 for false, 1
                        // for trueDs", status); // This information is
                        // sufficient for most
                        // entries tentative (0),
                        // confirmed (1) or canceled
                        // (2):
                        eventValues.put("eventTimezone", "UTC/GMT +5:30");
 /*
  * Comment below visibility and transparency column to avoid
  * java.lang.IllegalArgumentException column visibility is invalid
  * error
  */
                        // eventValues.put("allDay", 1);
                        // eventValues.put("visibility", 0); // visibility to default (0),
                        // confidential (1), private
                        // (2), or public (3):
                        // eventValues.put("transparency", 0); // You can control whether
                        // an event consumes time
                        // opaque (0) or transparent (1).

                        eventValues.put("hasAlarm", 1); // 0 for false, 1 for true

                        Context curActivity = null;
                        Uri eventUri = getApplicationContext()
                                .getContentResolver()
                                .insert(Uri.parse(eventUriString), eventValues);
                        eventID = Long.parseLong(eventUri.getLastPathSegment());


                        /***************** Event: Reminder(with alert) Adding reminder to event ***********        ********/

                        String reminderUriString = "content://com.android.calendar/reminders";
                        ContentValues reminderValues = new ContentValues();
                        reminderValues.put("event_id", eventID);
                        //reminderValues.put("minutes", 24 * 60);
                        reminderValues.put("minutes",0); // Default value of the
                        // system. Minutes is a integer
                        reminderValues.put("method", 1); // Alert Methods: Default(0),
                        // Alert(1), Email(2),SMS(3)

                        Uri reminderUri = getApplicationContext()
                                .getContentResolver()
                                .insert(Uri.parse(reminderUriString), reminderValues);


                    } catch (Exception ex) {
                        IISERApp.log(LOG_TAG, "Error in adding event on calendar" + ex.getMessage());
                    }





















                   /* Calendar cal = Calendar.getInstance();
                    cal.set(finalYear, finalMonth - 1, finalDate);
                   //cal.setTime(DTOEvent.getStr_event_date());
                    Intent intent = new Intent(Intent.ACTION_EDIT);
                    intent.setType("vnd.android.cursor.item/event");
                    intent.putExtra("beginTime", cal.getTimeInMillis());
                    intent.putExtra("allDay", false);
                    intent.putExtra("hasAlarm", 0);
                   *//* intent.putExtra(CalendarContract.Events.HAS_ALARM, false);
                    intent.putExtra(CalendarContract.Reminders.MINUTES,60);*//*

                   // intent.putExtra("allDay", false);
                   // intent.putExtra("rrule", "FREQ=DAILY;COUNT=1");
                    intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
                    intent.putExtra("title", DTOEvent.getStr_event_title());
                   *//* intent.putExtra("startDate",DTOEvent.getStr_event_date());*//*

                   *//* if(temp_date!=null)
                        intent.putExtra("startDate", temp);*//*

                    startActivity(intent);*/
                }
            });
        }

        @Override
        public int getItemCount() {
            return assign_List.size();
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
        refresh_data();

        IISERApp.log(LOG_TAG, "Awak onresume-->");
        context.registerReceiver(mMessageReceiver, new IntentFilter(LOG_TAG));
       /* if(exam_schedule_list.size()==0)
        {
            Toast.makeText(ActivityExamSchedule.this, "Please select courses", Toast.LENGTH_SHORT).show();
        }*/

        int count = TABLE_COURSE.get_selected_course_list();
       //if (IISERApp.get_session(IISERApp.SESSION_USERNAME).equalsIgnoreCase("student")) {
        if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("student")){
           if (count == 0)

           {
               // Toast.makeText(ActivityExamSchedule.this, "Courses are selected...", Toast.LENGTH_SHORT).show();
               //exam_schedule_list.clear();
               //  IISERApp.log("Shashi123", "before messege recieved"+exam_schedule_list);
               //bindComponentData();
               assign_List.clear();
               recyclerAdapter.notifyDataSetChanged();
               IISERApp.log("123", "after messege recieved" + assign_List);

               Toast.makeText(ActivityExamSchedule.this, "Please select courses...", Toast.LENGTH_SHORT).show();
           } else {
           /* recyclerAdapter = new EventAdapter(assign_List);
            recycler_view.setAdapter(recyclerAdapter);
*/
               // Toast.makeText(ActivityExamSchedule.this, "cOURSES ARE SEECTED.", Toast.LENGTH_SHORT).show();
               bindComponentData();
               //Toast.makeText(ActivityExamSchedule.this, "Please select courses...", Toast.LENGTH_SHORT).show();
           }
       }
        else
       {

       }




       /* if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("faculty")){
            if (count == 0)

            {
                // Toast.makeText(ActivityExamSchedule.this, "Courses are selected...", Toast.LENGTH_SHORT).show();
                //exam_schedule_list.clear();
                //  IISERApp.log("Shashi123", "before messege recieved"+exam_schedule_list);
                //bindComponentData();
                assign_List.clear();
                recyclerAdapter.notifyDataSetChanged();
                IISERApp.log("123", "after messege recieved" + assign_List);

                Toast.makeText(ActivityExamSchedule.this, "Please select courses...", Toast.LENGTH_SHORT).show();
            } else {
           *//* recyclerAdapter = new EventAdapter(assign_List);
            recycler_view.setAdapter(recyclerAdapter);
*//*
                // Toast.makeText(ActivityExamSchedule.this, "cOURSES ARE SEECTED.", Toast.LENGTH_SHORT).show();
                bindComponentData();
                //Toast.makeText(ActivityExamSchedule.this, "Please select courses...", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {

        }
*/


    }

    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            IISERApp.log(LOG_TAG, "messege recieved");
            Bundle bundle = intent.getExtras();
            IISERApp.log_bundle(bundle);
            /*Fragment_ExamSchedule first = (Fragment_ExamSchedule) objViewPagerAdapter.instantiateItem(mViewPager, 1);
            first.refreshData();*/

           // Fragment_ExamSchedule first= (Fragment_ExamSchedule)


            /*if (bundle.getString(IISERApp.BUNDLE_RESPONSE_CODE).equalsIgnoreCase("200")) {
              *//*  Intent intent1 = new Intent(context, ActivityExamSchedule.class);
                startActivity(intent1);
                finish();*//*
               *//* getIntentData();
                initComponents();
                initComponentsListeners();
                displayBanner();*//*
                bindComponentData();
                Snackbar.make(recycler_view, bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE), Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(recycler_view, bundle.getString(IISERApp.BUNDLE_EXCEPTION), Snackbar.LENGTH_LONG).show();
            }
*/
            bindComponentData();
        }
    };

    public void refresh_data() {
        if (((IISERApp) getApplicationContext()).isInternetAvailable()) {
            IISERApp.log(LOG_TAG, "button is pressed for refreshing in EXAM_SCHEDULE ");
            //   prgress_dialog = ProgressDialog.show(context, null, null);
            Intent intent1 = new Intent(context, IISERIntentService.class);
            IISERApp.log(LOG_TAG, "App.INTENT_FLAG:" + IISERApp.INTENT_FLAG + " App.INTENT_FLAG_GET_EXAM:" + IISERApp.INTENT_FLAG_EXAM_SCHEDULE);
            intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_EXAM_SCHEDULE);
            intent1.putExtra("Activity_name", "ActivityExamSchedule");
            context.startService(intent1);
        } else {
            ((IISERApp) getApplicationContext()).isInternetAvailable();
        }

    }
}