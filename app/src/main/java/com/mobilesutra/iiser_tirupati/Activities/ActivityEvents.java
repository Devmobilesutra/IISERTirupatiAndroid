package com.mobilesutra.iiser_tirupati.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Database.TABLE_EVENT;
import com.mobilesutra.iiser_tirupati.Error_Activity.ExceptionHandler;
import com.mobilesutra.iiser_tirupati.Model.DTO_Event;
import com.mobilesutra.iiser_tirupati.R;
import com.mobilesutra.iiser_tirupati.background.IISERIntentService;
import com.mobilesutra.iiser_tirupati.banner.Animations.DescriptionAnimation;
import com.mobilesutra.iiser_tirupati.banner.SliderLayout;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.BaseSliderView;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.TextSliderView;
import com.mobilesutra.iiser_tirupati.banner.Tricks.ViewPagerEx;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by kalyani on 22/04/2016.
 */
public class ActivityEvents extends Activity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    Context context = null;
    RecyclerView recycler_view = null;
    EventAdapter recyclerAdapter = null;
    LinearLayoutManager linearLayoutManager = null;

    SliderLayout mDemoSlider = null;
    // FloatingActionButton fab_alarm = null;
    FloatingActionButton fab_refresh=null;

    String LOG_TAG = "ActivityEvents", filename = "";
    int event_list_count = 0;
    View view = null;
    ProgressDialog dialog = null;
    //private int permission_count = 1;
    String pdfUrl;
    TextView add_process=null;
    TextView user_name=null;


    boolean loadingFinished = true;
    boolean redirect = false;
    ProgressDialog pDialog = null;



    List<LinkedHashMap<String, String>> event_list = new ArrayList<>();
    public List<DTO_Event> DTOEventList = new ArrayList<>();
    private List<LinkedHashMap<String, String>> DTOEvent;
    private View imageView;
    int permission_count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        //AlertDialog.Builder builder = new AlertDialog.Builder(this);



        //Toast.makeText(ActivityEvents.this, "Please Switch On Your Storage Permissions For Downloading Pdf ...", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.fragment_five);

        context = this;
        getIntentData();
        initComponentData();
        displayBanner();
        initComponentListeners();


      /*  final ScrollView main = (ScrollView) findViewById(R.id.scrollView123);
        main.post(new Runnable() {
            public void run() {
                main.scrollTo(0, 0);
            }
        });*/
        IISERApp.log(LOG_TAG, "SESSION_EVENT_FLAG:" + IISERApp.get_session(IISERApp.SESSION_EVENT_FLAG));
        if (IISERApp.get_session(IISERApp.SESSION_EVENT_FLAG).equalsIgnoreCase("")) {
            if (((IISERApp) getApplication()).isInternetAvailable()) {

                Intent intent1 = new Intent(Intent.ACTION_SYNC, null, context, IISERIntentService.class);
                intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_EVENT);
                // dialog = ProgressDialog.show(context, null, null);
                context.startService(intent1);

            }
            else
            {
                Toast.makeText(ActivityEvents.this, "No Internet available", Toast.LENGTH_LONG).show();
                // Snackbar.make(recycler_view, "No Internet available...", Snackbar.LENGTH_LONG).show();
            }
        } else {
            bindComponentData();
        }
    }

    /* protected void onRestoreInstanceState(Bundle state) {
         super.onRestoreInstanceState(state);

         // Retrieve list state and list/item positions
         if(state != null)
             event_list = state.getParcelable(event_list);
     }
 */
    private void getIntentData() {
    }

    private void initComponentData() {
        DTOEventList = new ArrayList<>();

        add_process=(TextView)findViewById(R.id.add_process);
        add_process.setVisibility(View.GONE);

        recycler_view = (RecyclerView) findViewById(R.id.recycler_event_view);
        //fab_alarm = (FloatingActionButton) findViewById(R.id.fab_alarm);
        fab_refresh=(FloatingActionButton)findViewById(R.id.fab);
        //  DTOEventList = new ArrayList<DTO_Event>();
        linearLayoutManager = new LinearLayoutManager(context);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setHasFixedSize(true);

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


    private void initComponentListeners() {


        fab_refresh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityEvents.this, "Please wait...Refreshing Data.", Toast.LENGTH_LONG).show();
                refresh_data();




            }
        });

      /* fab_alarm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "You are creating an alarm event...", Toast.LENGTH_LONG).show();

               *//* Calendar cal = Calendar.getInstance();
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra("beginTime", cal.getTimeInMillis());
                intent.putExtra("allDay", false);

                intent.putExtra("rrule", "FREQ=DAILY");
                intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
                intent.putExtra("title", "A Test DTO_Event from android app");
                startActivity(intent);*//*
            }
        });*/
    }



    private void bindComponentData() {


        List<DTO_Event> DTOEventList = new ArrayList<>();
        /*if (view != null) {*/
        event_list = TABLE_EVENT.get_event_list();
        IISERApp.log(LOG_TAG, "event_list:" + event_list);
        event_list_count = event_list.size();
        for (int i = 0; i < event_list_count; i++) {
            String event_id = event_list.get(i).get("event_id").toString();
            String event_title = event_list.get(i).get("event_title").toString();
            String event_description = event_list.get(i).get("event_description").toString();
            String event_date = event_list.get(i).get("event_date").toString();
            String event_pdf_link = event_list.get(i).get("pdf_link").toString();
            String event_venue = event_list.get(i).get("venue").toString();

            String day = IISERApp.get_day_from_date(event_date);
            String month = IISERApp.get_month_from_date(event_date);

            DTO_Event DTOEvent = new DTO_Event(event_id, month, day, event_title, event_description, event_date, event_pdf_link,
                    event_venue);



            DTOEventList.add(DTOEvent);
        }
        // }
      /*  DTO_Event DTOEvent = new DTO_Event("", "APR", "21", "Teacher Training Programme for School Teachers", "", "Dates:21-22 April",
                "", "IISER Pune");
        DTOEventList.add(DTOEvent);

        DTOEvent = new DTO_Event("", "JUL", "4", "Conference on Metapletic groups", "", "Dates:4-9 July", "",
                "Madhava Hall,3rd floor,IISER Pune");
        DTOEventList.add(DTOEvent);

        DTOEvent = new DTO_Event("", "JUL", "10", "44th National Seminar on Crystallography (NSC44)", "",
                "Dates:10-13 July", "", "Lecture Hall Complex at IISER Pune and NCL Auditorium");
        DTOEventList.add(DTOEvent);

        DTOEvent = new DTO_Event("", "DES", "15", "Indian Strings Meeting(ISM)", "", "Dates:15-21 DES", "", "IISER Pune");
        DTOEventList.add(DTOEvent);*/


        recyclerAdapter = new EventAdapter(DTOEventList);
        recycler_view.setAdapter(recyclerAdapter);

        recyclerAdapter.notifyDataSetChanged();

        Date date_curr = null,result_date=null;



        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        IISERApp.log(LOG_TAG, "IN FOR" + DTOEventList.size());
        for(int i=0 ; i<DTOEventList.size() ; i++) {
            IISERApp.log(LOG_TAG, "IN FOR LOOP: ");
            DTO_Event event1 = DTOEventList.get(i);
            IISERApp.log(LOG_TAG, "IN for value  of I: ");
            String event_date = event1.getStr_event_date();

            try
            {

                result_date = targetFormat.parse(event_date);

//                        System.out.println(date);
                date_curr = new Date();
                IISERApp.log(LOG_TAG, "CURRENT DATE :" + date_curr);
                IISERApp.log(LOG_TAG, "EVENT DATE :" + result_date);
                int diff = date_curr.compareTo(result_date);
                IISERApp.log(LOG_TAG, "date_curr.compareTo(result_date)" + diff);
                int diffrence2 = result_date.compareTo(date_curr);
                IISERApp.log(LOG_TAG, "result_date.compareTo(date_curr) :" + diffrence2);

                if (diff  == 0) {
                    IISERApp.log(LOG_TAG, "IN IF: ");

                    //recycler_view.getLayoutManager().scrollToPosition(i);
                    //  recycler_view.getLayoutManager().scrollToPosition(i);
                    recycler_view.scrollToPosition(i-1);
                    break;
                } else
                if ( diff < 0) {
                    IISERApp.log(LOG_TAG, "IN ID ELSW LOOP: ");
                    //recycler_view.getLayoutManager().scrollToPosition(i);
                    recycler_view.scrollToPosition(i);
                    //recycler_view.getLayoutManager().scrollToPosition(i);

                    IISERApp.log(LOG_TAG, "IN ID ELSW LOOPvalue  of I: ");
                    break;





/*
                    if ( diff <= 0) {
                        IISERApp.log(LOG_TAG, "IN ID ELSW LOOP: ");
                        //recycler_view.getLayoutManager().scrollToPosition(i);
                        recycler_view.scrollToPosition(i);
                        //recycler_view.getLayoutManager().scrollToPosition(i);

                        IISERApp.log(LOG_TAG, "IN ID ELSW LOOPvalue  of I: ");
                        break;*/

                }
            }
            catch (ParseException e) {
                // TODO Auto-generated catch block
                // Toast.makeText(MainActivity.this,"in exception1",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }











        /*IISERApp.log(LOG_TAG, "view:" + view);
        if (view != null) {
            if (recyclerAdapter == null) {
                IISERApp.log(LOG_TAG,"recyclerAdapter:null"+recyclerAdapter);
                recyclerAdapter = new EventAdapter(DTOEventList);
                recycler_view.setAdapter(recyclerAdapter);
            } else {
                IISERApp.log(LOG_TAG,"recyclerAdapter:not null"+recyclerAdapter);
                IISERApp.log(LOG_TAG,"DTOEventList size clear:"+DTOEventList.size());
                recyclerAdapter.DTOEventList.clear();
                IISERApp.log(LOG_TAG, "DTOEventList size after clear" +DTOEventList.size());
                recyclerAdapter.DTOEventList.addAll(DTOEventList);
                IISERApp.log(LOG_TAG, "DTOEventList size after addition" + DTOEventList.size());
                recyclerAdapter.notifyDataSetChanged();
            }
        }*/

    }

    public void displayBanner()
    {
        //final ViewFlipper MyViewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);


        mDemoSlider = (SliderLayout) findViewById(R.id.sample_output);
        mDemoSlider.setFocusable(true);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(3000);
        mDemoSlider.addOnPageChangeListener(this);
       /* LinkedHashMap<String, String> lhm = ShopApp.db.getFrontBanner();
        int length = lhm.size();
        if (length == 0) {*/
        int bannerArray[] = {R.drawable.banner, R.drawable.banner_1, R.drawable.banner_3};

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

        private List<DTO_Event> DTOEventList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView event_title, event_date, event_venue, event_month, event_day, event_pdf_link;
            public LinearLayout lnrLayout;
            public String str_title="";
            public String str_date="";
            FloatingActionButton fab_alarm=null;


            public MyViewHolder(View view) {
                super(view);
                event_month = (TextView) view.findViewById(R.id.txt_month);
                event_day = (TextView) view.findViewById(R.id.txt_day);
                event_title = (TextView) view.findViewById(R.id.txt_event_header);

                // str_title =(TextView)view.findViewById(R.id.txt_event_header);

                event_date = (TextView) view.findViewById(R.id.txt_event_organiser);
                event_venue = (TextView) view.findViewById(R.id.txt_event_venue);
                event_pdf_link = (TextView) view.findViewById(R.id.txt_event_pdf_link);
                lnrLayout = (LinearLayout) view.findViewById(R.id.lnrlayout_event_cardview);
                fab_alarm = (FloatingActionButton)view.findViewById(R.id.fab_alarm);


            }
        }


        public EventAdapter(List<DTO_Event> DTOEventList) {
            this.DTOEventList = DTOEventList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_list_row_event, parent, false);

           /* fab_alarm.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", false);

        intent.putExtra("rrule", "FREQ=DAILY");
        intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
        intent.putExtra("title", "A Test DTO_Event from android app");
        startActivity(intent);
    }
});*/
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("FragmentFive", "clicked on item");


                    filename = "";
                    TextView txt_pdf_link = (TextView) view.findViewById(R.id.txt_event_pdf_link);
                    IISERApp.log(LOG_TAG, "pdf_link:" + txt_pdf_link.getText().toString());
                    pdfUrl = txt_pdf_link.getText().toString();


                     Intent  i = new Intent(ActivityEvents.this,EventPdf.class);
                     i.putExtra("pdfurl", pdfUrl);
                     startActivity(i);


                    {
                        IISERApp.log(LOG_TAG, "In load pdf" + pdfUrl);
                    }

                }
            });
            return new MyViewHolder(itemView);

        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final DTO_Event DTOEvent = DTOEventList.get(position);
            holder.event_month.setText(DTOEvent.getStr_event_month());
            holder.event_day.setText(DTOEvent.getStr_event_day());
            holder.event_title.setText(DTOEvent.getStr_event_title());
            Date dt_name = null;
            //String dte_ster = ().toString();

/*           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");

            // sdf.
            *//*try {
                dt_name = (Date) sdf.parse(DTOEvent.getStr_event_date().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }*//*
            //IISERApp.log(LOG_TAG, "Date is:- " + DTOEvent.getStr_event_date());
            final String date_event=DTOEvent.getStr_event_date();

            Date dt1= null;
            try {
                dt1 = sdf.parse(date_event);
                IISERApp.log(LOG_TAG, "Parse Date is:- " + dt1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            *//*SimpleDateFormat format2=new SimpleDateFormat("yyyy");
            final int finalYear=Integer.parseInt(format2.format(dt1));*//*

            SimpleDateFormat format2=new SimpleDateFormat("yyyy-mm-dd");
            //final long finalYear=Long.parseLong(format2.format(dt1));
            final long finalYear= dt1.getTime();

            SimpleDateFormat format3=new SimpleDateFormat("mm");
            final int finalMonth=Integer.parseInt(format3.format(dt1));

            SimpleDateFormat format4=new SimpleDateFormat("dd");
            final int finalDate=Integer.parseInt(format4.format(dt1));
*/
            //IISERApp.log(LOG_TAG,"finalDay :"+finalDay);


            holder.event_date.setText(DTOEvent.getStr_event_date());
            holder.event_venue.setText(DTOEvent.getStr_event_venue());
            holder.event_pdf_link.setText(DTOEvent.getStr_event_pdf_link());
            String str_title=holder.event_title.getText().toString();


            //final Date temp_date = dt_name;
            holder.fab_alarm.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ActivityEvents.this, "Your event is created successfully...", Toast.LENGTH_LONG).show();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                    final String date_event=DTOEvent.getStr_event_date();

                    Date dt21= new Date();
                    try {
                        dt21 = sdf.parse(DTOEvent.getStr_event_date().toString()+ " 09:00");
                        IISERApp.log(LOG_TAG, "Date is:- " + DTOEvent.getStr_event_date() + " 09:00");
                        IISERApp.log(LOG_TAG, "Parse Date is:- " + dt21);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    final long finalYear= dt21.getTime();

                    long eventID = -1;
                    try {
                        String eventUriString = "content://com.android.calendar/events";
                        ContentValues eventValues = new ContentValues();
                        eventValues.put("calendar_id", 1); // id, We need to choose from
                        // our mobile for primary its 1
                        eventValues.put("title",  DTOEvent.getStr_event_title());
                        //eventValues.put("description", desc);
                        //eventValues.put("eventLocation", place);

                        //long endDate = startDate + 1000 * 10 * 10; // For next 10min
                        //String event_date=(String)
                        IISERApp.log(LOG_TAG,"EVENT DATE IS:"+finalYear);
                        eventValues.put("dtstart", finalYear);
                        IISERApp.log(LOG_TAG, "EVENT DATE IS:" + new Date(finalYear));
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

                        Context curActivity=null;
                        Uri eventUri = getApplicationContext()
                                .getContentResolver()
                                .insert(Uri.parse(eventUriString), eventValues);
                        eventID = Long.parseLong(eventUri.getLastPathSegment());


                        /***************** Event: Reminder(with alert) Adding reminder to event ***********        ********/

                        String reminderUriString = "content://com.android.calendar/reminders";
                        ContentValues reminderValues = new ContentValues();
                        reminderValues.put("event_id", eventID);
                        // reminderValues.put("minutes",  24 * 60); // Default value of the
                        // system. Minutes is a integer
                        reminderValues.put("minutes", 0);
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
            return DTOEventList.size();
        }


    }

    public void Loadpdf(String pdfurl)
    {
        // TODO Auto-generated method stub

        // boolean deleted = file.delete();
        filename = pdfurl.substring(pdfurl.lastIndexOf('/') + 1);
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/pdf/" + filename);
        if (file.exists()) {
            IISERApp.log(LOG_TAG, "filename in loadpdf if:" + filename);
            /*if(dialog!=null && dialog.isShowing())
                dialog.dismiss();*/


            showPdf();
        } else {
            IISERApp.log(LOG_TAG, "filename in loadpdf else :" + filename);

            if (((IISERApp) getApplication()).isInternetAvailable() == true) {

                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(dialog!=null && dialog.isShowing())
                        dialog.dismiss();
                    if (getPermissionCount() > 0)
                        check_app_persmission();
                    else
                        new DownloadFile().execute(pdfurl, filename);

                } else {*/
                new DownloadFile().execute(pdfurl, filename);
                //}

            } else {
                ((IISERApp) getApplication()).getdialog_checkinternet(context);

            }
        }

    }



    public void showPdf1(String path){
        setContentView(R.layout.activity_webview);
        WebView webView = (WebView) findViewById(R.id.nyc_poi_webview);     //new WebView(getApplicationContext());
        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        String googleLink = "http://docs.google.com/gview?embedded=true&url=";
        IISERApp.log(LOG_TAG, "showPdf() PATH : " + googleLink + "" + path);
        webView.loadUrl(googleLink + path);

        ttps://docs.google.com/viewerng/viewer?url=http://yourfile.pdf
        webView.setWebViewClient(new WebViewClient() {



           /* @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                if (!loadingFinished) {
                    redirect = true;
                }

                loadingFinished = false;
                view.loadUrl(urlNewString);
                return true;
            }*/

            @Override
            public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                loadingFinished = false;
                //SHOW LOADING IF IT ISNT ALREADY VISIBLE
                if (pDialog != null)
                    if (pDialog.isShowing())
                        pDialog.dismiss();

                pDialog = ProgressDialog.show(context, getResources().getString(R.string.app_name),
                        "Loading...", false, false);
                pDialog.setCancelable(false);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(!redirect){
                    loadingFinished = true;
                }

                if(loadingFinished && !redirect){
                    //HIDE LOADING IT HAS FINISHED
                   pDialog.dismiss();

                } else{
                    redirect = false;
                }
            }
        });
    }




    public void showPdf()
    {

        File pdfFile = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/pdf/" + filename);

        String filepath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/pdf/" + filename;
        try {

            if (pdfFile.exists()) {

                if(dialog!=null && dialog.isShowing())
                    dialog.dismiss();

                Uri path = Uri.fromFile(pdfFile);
                IISERApp.log(LOG_TAG, "pdf file in showpdf:" + pdfFile);

                /*if (IISERApp.hasMarshMallow()) {
                    Intent objIntent = new Intent(Intent.ACTION_VIEW);

                    objIntent.setDataAndType(path, "application/pdf");

                    objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(objIntent);

                } *//*else if (IISERApp.hasNoghat()) {
                    Intent objIntent = new Intent(Intent.ACTION_VIEW);

                    objIntent.setDataAndType(path, "application/pdf");

                    objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(objIntent);

                }
*/
              //  else {

                    /*IISERApp.log(LOG_TAG, "pdf file in showpdf:" + pdfFile);
                    Intent intent = new Intent(context, ActivityPDFReader.class);
                    intent.putExtra("PdfUrl", filepath);
                    IISERApp.log(LOG_TAG, "pdf file in showpdf:" + pdfFile);
                    startActivity(intent);
                    IISERApp.log(LOG_TAG, "pdf file in showpdf:" + pdfFile);*/


                    if (pdfFile.exists()) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri uri = Uri.fromFile(pdfFile);
                        intent.setDataAndType(uri, "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(ActivityEvents.this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(ActivityEvents.this, "No pdf file to view", Toast.LENGTH_LONG).show();
                    }
               // }
            } else {

                Toast.makeText(ActivityEvents.this, "File NotFound",

                        Toast.LENGTH_SHORT).show();

            }

        } catch (ActivityNotFoundException e) {

            Toast.makeText(
                    ActivityEvents.this,

                    "No Pdf Viewer Application Found.Please Download Any Pdf Viewer Application From Google Play Store",
                    Toast.LENGTH_SHORT)

                    .show();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public class DownloadFile extends AsyncTask<String, Void, Void> {

        protected void onPreExecute() {

             /*dialog = ProgressDialog.show(Holiday_List.this,
             "Downloading....",
             "downloading file", true, false);
             progressBar.setVisibility(View.VISIBLE);
              webView.setWebViewClient(new myWebClient());*/
        }

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0]; // ->
            // http://maven.apache.org/maven-1.x/maven.pdf
            String fileName1 = strings[1]; // -> maven.pdf
            Log.d("HolidayList", "fileUrl:" + fileUrl);
            Log.d("HolidayList", "fileName1:" + fileName1);
            String extStorageDirectory = Environment
                    .getExternalStorageDirectory().toString();
            IISERApp.log(LOG_TAG, "extStorageDirectory" + extStorageDirectory);
            File folder = new File(extStorageDirectory, "pdf");
            folder.mkdir();

            File pdfFile = new File(folder, fileName1);
            IISERApp.set_session("pdfFile", pdfFile.toString());
            if (pdfFile.exists()) {
                IISERApp.log(LOG_TAG, "EXISTS PDF" + pdfFile);
            } else {
                IISERApp.log(LOG_TAG, "PDF DOES NOT EXISTS " + pdfFile);
            }
            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                Log.d("HolidayList", "IOException");
                e.printStackTrace();
            }

            int res = 0;
            try {
                if (((IISERApp) getApplication()).isInternetAvailable() == true) {

                    String pdfurl = fileUrl.replaceAll(" ", "%20");
                    Log.d("HolidayList", "URL without space:" + pdfurl);
                    //res = PDFDownloader.downloadFile(fileUrl.replaceAll(" ", "%20"), pdfFile);
                    res = PDFDownloader.downloadFile(pdfurl, pdfFile);

                     Log.d("HolidayList", "Ress:" + res);
                    //  onProgressUpdate("" + res);
                } else {
                    ((IISERApp) getApplication())
                            .getdialog_checkinternet(context);

                }
            } catch (Exception e) {
                res = 0;
                // getdialog_internetslow();
                Log.d("HolidayList", " catch Res:" + res);
            }
            Log.d("HolidayList", "Res:" + res);

            onProgressUpdate("" + res);
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            // dialog.dismiss();
            if (progress[0].equals("0")) {
                if(dialog!=null && dialog.isShowing())
                    dialog.dismiss();
                ActivityEvents.this.runOnUiThread(new Runnable() {
                    public void run() {

                        ((IISERApp) getApplication()).getdialog_internetslow(context);

                        // // Toast.makeText(Holiday_List.this,
                        // // "Your internet connection is slow..",
                        // Toast.LENGTH_LONG)
                        // // .show();

                    }
                });
            } else {
                //if(dialog!=null && dialog.isShowing())
                // dialog.dismiss();
                Log.d("HolidayList", "Enter in else part");
                showPdf();
                IISERApp.log(LOG_TAG, "PDF IS SHOWED");
            }

        }

        protected void onPostExecute(Long result) {

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
    public void onResume() {
        super.onResume();



        IISERApp.log(LOG_TAG, "Awak onresume-->");
        context.registerReceiver(mMessageReceiver, new IntentFilter(LOG_TAG));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getPermissionCount() > 0)
                check_app_permission();
            //else
            // startActivity(callIntent);

        } else {
            // startActivity(callIntent);
        }
    }

    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            IISERApp.log(LOG_TAG, "messege recieved");
            Bundle bundle = intent.getExtras();
            IISERApp.log_bundle(bundle);
            // bindComponentData();
           /* if (bundle.containsKey(IISERApp.BUNDLE_RESPONSE_CODE)) {
                if (bundle.getString(IISERApp.BUNDLE_RESPONSE_CODE).equalsIgnoreCase("200")) {
                *//*Intent intent1 = new Intent(context,ActivityEvents.class);
                startActivity(intent1);
                finish();*//*
                    bindComponentData();
                    Snackbar.make(recycler_view, bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE), Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(recycler_view, bundle.getString(IISERApp.BUNDLE_EXCEPTION), Snackbar.LENGTH_LONG).show();
                }
            } else {
                bindComponentData();
            }
*/
            if (bundle.getString(IISERApp.BUNDLE_RESPONSE_CODE).equalsIgnoreCase("200")) {

                //prgresbar.setVisibility(View.GONE);
                if (bundle.getString(IISERApp.BUNDLE_RESPONSE_STATUS).equalsIgnoreCase("1")) {
                    IISERApp.log(LOG_TAG, "in if condition of response stastus 1");
                    // dialog.setMessage(bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE));
                    /*if (dialog.isShowing()) {
                        dialog.dismiss();*/
                    IISERApp.log(LOG_TAG, "in if condition of response stastus 1 if dialog showing..");
                    // Snackbar.make(btn_login, bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE), Snackbar.LENGTH_LONG).show();
                    bindComponentData();
                    //    }
                } else if (bundle.getString(IISERApp.BUNDLE_RESPONSE_STATUS).equalsIgnoreCase("0")) {
                    // dialog.setMessage(bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE));
                    //  dialog.dismiss();
                  /*  Snackbar.make(recycler_view, bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE), Snackbar.LENGTH_LONG).show();
*/
                } else {
                    //  dialog.setMessage(bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE));
                }
            } else {
                // prgresbar.setVisibility(View.GONE);
             /*   if (dialog.isShowing())
                    dialog.dismiss();*/
                // Snackbar.make(btn_apply, bundle.getString(IISERApp.BUNDLE_EXCEPTION), Snackbar.LENGTH_LONG).show();
            }

        }
    };

    public void refresh_data()
    {

        if (((IISERApp) getApplicationContext()).isInternetAvailable()) {
            IISERApp.log(LOG_TAG,"button is pressed for refreshing in notice ");
            //   prgress_dialog = ProgressDialog.show(context, null, null);
            Intent intent1 = new Intent(context, IISERIntentService.class);
            IISERApp.log(LOG_TAG, "App.INTENT_FLAG:" + IISERApp.INTENT_FLAG + " App.INTENT_FLAG_GET_EXAM:" + IISERApp.INTENT_FLAG_EVENT);
            intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_EVENT);
            intent1.putExtra("Activity_name", "ActivityEvents");
            context.startService(intent1);
        } else {
            ((IISERApp)  getApplicationContext()).isInternetAvailable();
        }
    }



    private void check_app_permission() {
        permission_count = 1;
        int permission_granted = PackageManager.PERMISSION_GRANTED;
        IISERApp.log(LOG_TAG, "PermissionGrantedCode->" + permission_granted);

        int calendar_permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR);
        IISERApp.log(LOG_TAG, "calendar_permission->" + calendar_permission);
        if (calendar_permission == permission_granted)
            permission_count -= 1;

        IISERApp.log(LOG_TAG, "check_app_permission PermissionCount->" + permission_count);

        if (permission_count > 0) {
            String permissionArray[] = new String[permission_count];

            for (int i = 0; i < permission_count; i++) {
                IISERApp.log(LOG_TAG, "i->" + i);

                if (calendar_permission != permission_granted) {
                    if (!Arrays.asList(permissionArray).contains(Manifest.permission.WRITE_CALENDAR)) {
                        permissionArray[i] = Manifest.permission.WRITE_CALENDAR;
                        IISERApp.log(LOG_TAG, "i->WRITE_CALENDAR");
                        // break;
                    }
                }
            }
            IISERApp.log(LOG_TAG, "PermissionArray->" + Arrays.deepToString(permissionArray));

            ActivityCompat.requestPermissions(ActivityEvents.this, permissionArray, permission_count);//requestPermissions(permissionArray, permission_count);
        }
    }

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

            IISERApp.log(LOG_TAG, "onRequestPermissionsResult PermissionCount->" + permission_count);
        }

        if (permission_count > 0) {

            Snackbar.make(imageView, "IISER needs permissions : " + str,
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
        int count = 1;
        int permission_granted = PackageManager.PERMISSION_GRANTED;
        int calendar_permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR);
        if (calendar_permission == permission_granted)
            count -= 1;
        return count;
    }
}
    /*public int getPermissionCount() {
        int count = 1;
        int permission_granted = PackageManager.PERMISSION_GRANTED;
        *//*int camera_permission = checkSelfPermission(Manifest.permission.CAMERA);
        if (camera_permission == permission_granted)
            count -= 1;*//*
        int storage_permission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (storage_permission == permission_granted)
            count -= 1;
            *//*
        int audio_permission = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
        if (audio_permission == permission_granted)
            count -= 1;*//*
        *//*int call_permission = checkSelfPermission(Manifest.permission.CALL_PHONE);
        if (call_permission == permission_granted)
            count -= 1;*//*
        IISERApp.log(LOG_TAG,"COUNT  IS"+count);
        return count;
    }

    private void check_app_persmission() {
        permission_count = 1;
        int permission_granted = PackageManager.PERMISSION_GRANTED;
        IISERApp.log(LOG_TAG, "PersmissionGrantedCode->" + permission_granted);

        *//*int audio_permission = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
        App.log(LOG_TAG, "AudioPermission->" + audio_permission);
        if(audio_permission == permission_granted)
            permission_count -= 1;
        App.log(LOG_TAG, "PermissionCount->" + permission_count);
*//*
        int storage_permission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        IISERApp.log(LOG_TAG, "StoragePermission->" + storage_permission);
        if(storage_permission == permission_granted)
            permission_count -= 1;
*//*
        int camera_permission = checkSelfPermission(Manifest.permission.CAMERA);
        App.log(LOG_TAG, "CameraPermission->" + camera_permission);
        if(camera_permission == permission_granted)
            permission_count -= 1;*//*

        *//*int call_permission = checkSelfPermission(Manifest.permission.CALL_PHONE);
        IISERApp.log(LOG_TAG, "CallPermission->" + call_permission);
        if (call_permission == permission_granted)
            permission_count -= 1;*//*
        IISERApp.log(LOG_TAG, "PermissionCount->" + permission_count);

        if (permission_count > 0) {
            String permissionArray[] = new String[permission_count];

            for (int i = 0; i < permission_count; i++) {
                IISERApp.log(LOG_TAG, "i->" + i);
                *//*if (audio_permission != permission_granted) {
                    if(!Arrays.asList(permissionArray).contains(Manifest.permission.RECORD_AUDIO)) {
                        permissionArray[i] = Manifest.permission.RECORD_AUDIO;
                        App.log(LOG_TAG,"i->RECORD_AUDIO");
                        //break;
                    }
                }

*//*
                if (storage_permission != permission_granted) {
                    if(!Arrays.asList(permissionArray).contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        permissionArray[i] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                        IISERApp.log(LOG_TAG,"i->WRITE_EXTERNAL_STORAGE");
                        // break;
                    }
                }
*//*
                if (camera_permission != permission_granted) {
                    if(!Arrays.asList(permissionArray).contains(Manifest.permission.CAMERA)) {
                        permissionArray[i] = Manifest.permission.CAMERA;
                        App.log(LOG_TAG,"i->CAMERA");
                        //break;
                    }
                }*//*

                *//*if (call_permission != permission_granted) {
                    if (!Arrays.asList(permissionArray).contains(Manifest.permission.CALL_PHONE)) {
                        permissionArray[i] = Manifest.permission.CALL_PHONE;
                        IISERApp.log(LOG_TAG, "i->CALL");
                        //break;
                    }
                }*//*

            }
            IISERApp.log(LOG_TAG, "PermissionArray->" + Arrays.deepToString(permissionArray));

            ActivityCompat.requestPermissions(ActivityEvents.this, permissionArray, permission_count);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        permission_count = permissions.length;
        IISERApp.log(LOG_TAG, "In onRequestPermissionsResult");
        IISERApp.log(LOG_TAG, "requestCode->" + requestCode);
        IISERApp.log(LOG_TAG, "permissions->" + Arrays.deepToString(permissions));
        int len = grantResults.length;


        int permission_granted = PackageManager.PERMISSION_GRANTED;
        IISERApp.log(LOG_TAG, "PersmissionGrantedCode->" + permission_granted);
        String str = "";
        for (int i = 0; i < len; i++) {

            *//*if(permissions[i].equalsIgnoreCase(Manifest.permission.CAMERA)) {
                int camera_permission = checkSelfPermission(Manifest.permission.CAMERA);
                App.log(LOG_TAG, "CameraPermission->" + camera_permission);
                if (camera_permission == permission_granted) {
                    permission_count -= 1;
                }
                else {
                    str += "Camera, ";
                }
            }*//*
            if(permissions[i].equalsIgnoreCase(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                int storage_permission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                IISERApp.log(LOG_TAG, "StoragePermission->" + storage_permission);
                if (storage_permission == permission_granted) {
                    permission_count -= 1;
                }
                else {
                    str += "Storage ";
                }
            }
            *//*if(permissions[i].equalsIgnoreCase(Manifest.permission.RECORD_AUDIO)) {
                int audio_permission = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
                App.log(LOG_TAG, "AudioPermission->" + audio_permission);
                if (audio_permission == permission_granted) {
                    permission_count -= 1;
                }
                else {
                    str+="AUDIO";
                }
            }*//*
            *//*if (permissions[i].equalsIgnoreCase(Manifest.permission.CALL_PHONE)) {
                int call_permission = checkSelfPermission(Manifest.permission.CALL_PHONE);
                IISERApp.log(LOG_TAG, "CallPermission->" + call_permission);
                if (call_permission == permission_granted) {
                    permission_count -= 1;
                } else {
                    str += "Call";
                }
            }*//*
            IISERApp.log(LOG_TAG, "PermissionCount->" + permission_count);
        }

        if (permission_count > 0) {
            Snackbar.make(recycler_view, "IISER App needs permissions : " + str,
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    String SCHEME = "package";
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts(SCHEME, getApplication().getPackageName().toString(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            }).show();
        } else {
            *//*Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + mobile_no));
            startActivity(callIntent);*//*

            new DownloadFile().execute(pdfUrl, filename);
        }
    }
    }
    */



