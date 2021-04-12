
package com.mobilesutra.iiser_tirupati.Fragment;


import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Database.TABLE_ACADMIC_CALENDER;
import com.mobilesutra.iiser_tirupati.Model.DTO_AcademicCalendar;
import com.mobilesutra.iiser_tirupati.R;
import com.mobilesutra.iiser_tirupati.banner.Animations.DescriptionAnimation;
import com.mobilesutra.iiser_tirupati.banner.SliderLayout;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.BaseSliderView;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.TextSliderView;
import com.mobilesutra.iiser_tirupati.banner.Tricks.ViewPagerEx;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Academic_calender extends android.support.v4.app.Fragment implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

//public class Fragment_Academic_calender extends Fragment implements BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener
//{


    Context context = null;
    RecyclerView recycler_view = null;
    public List<DTO_AcademicCalendar> DTOEventList = new ArrayList<>();
    EventAdapter recyclerAdapter = null;
    LinearLayoutManager linearLayoutManager = null;
    FloatingActionButton fab_alarm=null;


    List<LinkedHashMap<String, String>> student_calendar = new ArrayList<>();
    LinkedHashMap<String, String> student_calendar_lhm;

    SliderLayout mDemoSlider = null;
    View view = null;
    List<String> date = new ArrayList<>();



    private static final String ARG_PARAM_TITLE = "title", LOG_TAG = "Fragment_AcademicCalendar";

    public static Fragment_Academic_calender newInstance(String str_title) {
        Log.d("Fragment", "In newInstance");
        Fragment_Academic_calender fragment = new Fragment_Academic_calender();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM_TITLE, str_title);
        fragment.setArguments(bundle);
        return fragment;
    }

    public Fragment_Academic_calender() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.activity_fragment_profile, container, false);
        if (view == null) {
            view = inflater.inflate(R.layout.activity_fragment_notice, container, false);
            initComponentData(view);
            // bindComponentData();
            initComponentListeners();
            //displayBanner(view);
        }

        return view;
    }


    private void getIntentData() {
    }

    private void initComponentData(View view) {


        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_event_view);
        DTOEventList = new ArrayList<DTO_AcademicCalendar>();
        linearLayoutManager = new LinearLayoutManager(context);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setHasFixedSize(true);
       /* fab_alarm=(FloatingActionButton)findViewById(R.id.fab_alarm);
        fab_alarm.setVisibility(View.GONE);
*/
    }

    private void initComponentListeners()
    {

        DTOEventList= TABLE_ACADMIC_CALENDER.get_calender();
        recyclerAdapter = new EventAdapter(DTOEventList);
        IISERApp.log(LOG_TAG,"events are"+DTOEventList);
        recycler_view.setAdapter(recyclerAdapter);

       /* recyclerAdapter.notifyDataSetChanged();*/
        Date date_curr = null,result_date=null;

        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        IISERApp.log(LOG_TAG, "IN FOR" + DTOEventList.size());
        for(int i=0 ; i<DTOEventList.size() ; i++) {
            IISERApp.log(LOG_TAG, "IN FOR LOOP: ");
            DTO_AcademicCalendar acadmic = DTOEventList.get(i);
            IISERApp.log(LOG_TAG, "IN for value  of I: ");
            String event_date = acadmic.getStr_calendar_date();

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
                if ( diff <= 0) {
                    IISERApp.log(LOG_TAG, "IN ID ELSW LOOP: ");
                    //recycler_view.getLayoutManager().scrollToPosition(i);
                    recycler_view.scrollToPosition(i);
                    linearLayoutManager.scrollToPositionWithOffset(i, 0);
                    //recycler_view.getLayoutManager().scrollToPosition(i);

                    IISERApp.log(LOG_TAG, "IN ID ELSW LOOPvalue  of I: ");
                    break;
                }

                /*if (diff <= 0) {
                    IISERApp.log(LOG_TAG, "IN ID ELSW LOOP: ");
                    recycler_view.scrollToPosition(i);

                       //recycler_view.getLayoutManager().smoothScrollToPosition(recycler_view, null, i);
                    // recycler_view.getLayoutManager().scrollToPosition(10);
                    //  linearLayoutManager.scrollToPositionWithOffset(0,i);
                    IISERApp.log(LOG_TAG, "IN ID ELSW LOOPvalue  of I: ");
                    break;


                }*/

            }
            catch (ParseException e) {
                // TODO Auto-generated catch block
                // Toast.makeText(MainActivity.this,"in exception1",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
        recyclerAdapter.notifyDataSetChanged();
    }
    /*public int getDays(String begin) throws ParseException {
        long MILLIS_PER_DAY = 24 * 60 * 60 * 1000;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        long begin_new = dateFormat.parse(begin).getTime();
        long end = new Date(22-02-2017).getTime(); // 2nd date want to compare
        long diff = (end - begin) / (MILLIS_PER_DAY);
        return (int) diff;
    }*/
    private void bindComponentData() {
        if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("student")) {
            if (student_calendar.size() == 0) {
                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-01-03");
                student_calendar_lhm.put("day", "Sunday");
                student_calendar_lhm.put("title", "Reporting to the campus for Spring 2016");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-01-04");
                student_calendar_lhm.put("day", "Monday");
                student_calendar_lhm.put("title", "Registration for Spring 2016,First day of Instruction");
                student_calendar.add(student_calendar_lhm);

                IISERApp.log(LOG_TAG, "student_calendar: " + student_calendar);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-01-11");
                student_calendar_lhm.put("day", "Monday");
                student_calendar_lhm.put("title", "Last day for add / drop of courses");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-02-22");
                student_calendar_lhm.put("day", "Monday");
                student_calendar_lhm.put("title", "Mid Semester Examination");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-03-28");
                student_calendar_lhm.put("day", "Monday");
                student_calendar_lhm.put("title", "Last day for submission of MS thesis");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-04-06");
                student_calendar_lhm.put("day", "Wednesday");
                student_calendar_lhm.put("title", "Last day for submission of Fifth year Project Proposals");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-04-19");
                student_calendar_lhm.put("day", "Tuesday");
                student_calendar_lhm.put("title", "Last day of Instruction,Spring 2016");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-04-22");
                student_calendar_lhm.put("day", "Fiday");
                student_calendar_lhm.put("title", "End Semester Examinations");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-04-30");
                student_calendar_lhm.put("day", "Saturday");
                student_calendar_lhm.put("title", "Spring 2016 ends");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-05-02");
                student_calendar_lhm.put("day", "Monday");
                student_calendar_lhm.put("title", "Presentation / Viva of Fifth year Projects");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-05-12");
                student_calendar_lhm.put("day", "Thursday");
                student_calendar_lhm.put("title", "Announcement of Grades");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-05-19");
                student_calendar_lhm.put("day", "Thursday");
                student_calendar_lhm.put("title", "Last day for correction in grades");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-05-23");
                student_calendar_lhm.put("day", "Monday");
                student_calendar_lhm.put("title", "Last day for registration for re-exam");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-06-02");
                student_calendar_lhm.put("day", "Thursday");
                student_calendar_lhm.put("title", "Re-examinations");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-06-13");
                student_calendar_lhm.put("day", "Monday");
                student_calendar_lhm.put("title", "Announcement of grades after re-exam");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-07-31");
                student_calendar_lhm.put("day", "Sunday");
                student_calendar_lhm.put("title", "Reporting of students after summer vacation.");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-08-01");
                student_calendar_lhm.put("day", "Monday");
                student_calendar_lhm.put("title", "Registration for Fall 2016,First day of Instruction");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-08-08");
                student_calendar_lhm.put("day", "Monday");
                student_calendar_lhm.put("title", "Last day for add / drop of courses");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-09-19");
                student_calendar_lhm.put("day", "Monday");
                student_calendar_lhm.put("title", "Mid Semester Examination");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-10-08");
                student_calendar_lhm.put("day", "Saturday");
                student_calendar_lhm.put("title", "Festival break");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-11-21");
                student_calendar_lhm.put("day", "Monday");
                student_calendar_lhm.put("title", "Last day of Instruction");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-11-23");
                student_calendar_lhm.put("day", "Wednesday");
                student_calendar_lhm.put("title", "End Semester Examinations");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-11-30");
                student_calendar_lhm.put("day", "Wednesday");
                student_calendar_lhm.put("title", "Fall semester ends");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-12-12");
                student_calendar_lhm.put("day", "Monday");
                student_calendar_lhm.put("title", "Announcement of Grades");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-12-19");
                student_calendar_lhm.put("day", "Monday");
                student_calendar_lhm.put("title", "Last day for reporting correction in grades");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-12-21");
                student_calendar_lhm.put("day", "Wednesday");
                student_calendar_lhm.put("title", "Last day for registration for re-exam");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-12-28");
                student_calendar_lhm.put("day", "Wednesday");
                student_calendar_lhm.put("title", "Re-examinations");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2017-01-04");
                student_calendar_lhm.put("day", "Wednesday");
                student_calendar_lhm.put("title", "Announcement of grades after re-exams");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-01-26");
                student_calendar_lhm.put("day", "Tuesday");
                student_calendar_lhm.put("title", "Republic Day");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-03-24");
                student_calendar_lhm.put("day", "Thursday");
                student_calendar_lhm.put("title", "Holi");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-03-25");
                student_calendar_lhm.put("day", "Friday");
                student_calendar_lhm.put("title", "Good Friday");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-04-08");
                student_calendar_lhm.put("day", "Wednesday");
                student_calendar_lhm.put("title", "Gudi Padava");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-04-20");
                student_calendar_lhm.put("day", "Wednesday");
                student_calendar_lhm.put("title", "Mahavir Jayanti");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-08-15");
                student_calendar_lhm.put("day", "Monday");
                student_calendar_lhm.put("title", "Independence Day");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-09-05");
                student_calendar_lhm.put("day", "Monday");
                student_calendar_lhm.put("title", "Ganesh Chaturthi");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-09-12");
                student_calendar_lhm.put("day", "Monday");
                student_calendar_lhm.put("title", "Idu'l Zuha(Bakrid)");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-10-02");
                student_calendar_lhm.put("day", "Saturday");
                student_calendar_lhm.put("title", "Mahatma Gandhi's Birthday");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-10-11");
                student_calendar_lhm.put("day", "Thuesday");
                student_calendar_lhm.put("title", "Dussera");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-10-12");
                student_calendar_lhm.put("day", "Wednesday");
                student_calendar_lhm.put("title", "Muharam");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-10-30");
                student_calendar_lhm.put("day", "Sunday");
                student_calendar_lhm.put("title", "Diwali");
                student_calendar.add(student_calendar_lhm);

                student_calendar_lhm = new LinkedHashMap<>();
                student_calendar_lhm.put("date", "2016-11-14");
                student_calendar_lhm.put("day", "Monday");
                student_calendar_lhm.put("title", "Guru Nanak's Birthday");
                student_calendar.add(student_calendar_lhm);

                IISERApp.log(LOG_TAG, "student_calendar: " + student_calendar);
                for (int j = 0; j < student_calendar.size(); j++) {
           /* DTO_Event DTOEvent = new DTO_Event("", "JAN", "3", "Reporting to the campus for Spring 2016", "", "Description", "", "");
            DTOEventList.add(DTOEvent);*/

                    String str_calendar_id = String.valueOf(j);
                    String str_calendar_date = student_calendar.get(j).get("date");
                    String str_calendar_weekday = student_calendar.get(j).get("day");
                    String str_calendar_title = student_calendar.get(j).get("title");


                    String str_calendar_month = IISERApp.get_month_from_date(str_calendar_date);
                    String str_calendar_weekdate = IISERApp.get_day_from_date(str_calendar_date);


                    DTO_AcademicCalendar DTOAcademicCalendar = new DTO_AcademicCalendar(str_calendar_id, str_calendar_date,
                            str_calendar_month, str_calendar_weekday,
                            str_calendar_weekdate, str_calendar_title);
                    DTOEventList.add(DTOAcademicCalendar);
                }

            } else {
                for (int j = 0; j < student_calendar.size(); j++) {
           /* DTO_Event DTOEvent = new DTO_Event("", "JAN", "3", "Reporting to the campus for Spring 2016", "", "Description", "", "");
            DTOEventList.add(DTOEvent);*/

                    String str_calendar_id = String.valueOf(j);
                    String str_calendar_date = student_calendar.get(j).get("date");
                    String str_calendar_weekday = student_calendar.get(j).get("day");
                    String str_calendar_title = student_calendar.get(j).get("title");


                    String str_calendar_month = IISERApp.get_month_from_date(str_calendar_date);
                    String str_calendar_weekdate = IISERApp.get_day_from_date(str_calendar_date);

                    DTO_AcademicCalendar DTOAcademicCalendar = new DTO_AcademicCalendar(str_calendar_id, str_calendar_date,
                            str_calendar_month, str_calendar_weekday,
                            str_calendar_weekdate, str_calendar_title);
                    DTOEventList.add(DTOAcademicCalendar);
                }
            }
        }

       /* DTO_Event DTOEvent = new DTO_Event("", "JAN", "3", "Reporting to the campus for Spring 2016", "", "Description", "", "");
        DTOEventList.add(DTOEvent);

        DTOEvent = new DTO_Event("", "JAN", "4", "Registration for Spring 2016,First day of Institution", "", "Description", "", "");
        DTOEventList.add(DTOEvent);

        DTOEvent = new DTO_Event("", "JAN", "11", "Last day for add/drop of courses", "", "Description", "", "");
        DTOEventList.add(DTOEvent);

        DTOEvent = new DTO_Event("", "MAR", "28", "Last day for submission of MS thesis", "", "Description", "", "");
        DTOEventList.add(DTOEvent);

        DTOEvent = new DTO_Event("", "APR", "6", "Last day for submission of Fifth year Project Proposals", "", "Description", "", "");
        DTOEventList.add(DTOEvent);

        DTOEvent = new DTO_Event("", "APR", "30", "Spring 2016 ends", "", "Description", "", "");
        DTOEventList.add(DTOEvent);*/

//

        recyclerAdapter = new EventAdapter(DTOEventList);
        recycler_view.setAdapter(recyclerAdapter);

        recyclerAdapter.notifyDataSetChanged();

    }

    public void displayBanner(View view) {
        //final ViewFlipper MyViewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);


        mDemoSlider = (SliderLayout) view.findViewById(R.id.sample_output);
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

        private List<DTO_AcademicCalendar> DTOEventList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView event_title, event_weekday, event_venue, event_month, event_day;
            public LinearLayout lnrLayout;


            public MyViewHolder(View view) {
                super(view);
                event_month = (TextView) view.findViewById(R.id.txt_month);
                event_day = (TextView) view.findViewById(R.id.txt_day);
                event_title = (TextView) view.findViewById(R.id.txt_event_header);
                event_weekday = (TextView) view.findViewById(R.id.txt_event_organiser);
                // event_venue = (TextView) view.findViewById(R.id.txt_event_venue);
                lnrLayout = (LinearLayout) view.findViewById(R.id.lnrlayout_event_cardview);
                fab_alarm=(FloatingActionButton)view.findViewById(R.id.fab_alarm);
                fab_alarm.setVisibility(View.GONE);

            }
        }


        public EventAdapter(List<DTO_AcademicCalendar> DTOEventList) {
            this.DTOEventList = DTOEventList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_list_row_event, parent, false);

           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("FragmentFive", "clicked on item");
                    if (IISERApp.hasLollipop()) {
                        Intent intent = new Intent(context, ActivityEventDetails.class);
                        startActivity(intent);
                    } else {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.tutorialspoint.com/android/android_tutorial.pdf"));
                        startActivity(browserIntent);
                    }

                }
            });*/
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            IISERApp.log(LOG_TAG,"IN beforeonBindViewHolder position: position"+position);
            DTO_AcademicCalendar DTOEvent = DTOEventList.get(position);
            IISERApp.log(LOG_TAG,"IN affter onBindViewHolder position: position"+position);
            holder.event_month.setText(DTOEvent.getStr_calendar_month());
            holder.event_day.setText(DTOEvent.getStr_calendar_weekdate());
            holder.event_title.setText(DTOEvent.getStr_calendar_title());
            holder.event_weekday.setText(DTOEvent.getStr_calendar_weekday());

            if (DTOEvent.getStr_holiday().equalsIgnoreCase("Yes")) {
                holder.event_month.setTextColor(Color.RED);
                holder.event_day.setTextColor(Color.RED);
                holder.event_title.setTextColor(Color.GRAY);
                holder.event_weekday.setTextColor(Color.GRAY);

            } else {
                holder.event_month.setTextColor(Color.GRAY);
                holder.event_day.setTextColor(Color.GRAY);
                holder.event_title.setTextColor(Color.GRAY);
                holder.event_weekday.setTextColor(Color.GRAY);
               /* holder.event_month.setText(DTOEvent.getStr_calendar_month());
                holder.event_day.setText(DTOEvent.getStr_calendar_weekdate());
                holder.event_title.setText(DTOEvent.getStr_calendar_title());
                holder.event_weekday.setText(DTOEvent.getStr_calendar_weekday());*/
          /*  holder.event_month.setText(DTOEvent.getStr_calendar_month());
            holder.event_day.setText(DTOEvent.getStr_calendar_weekdate());
            holder.event_title.setText(DTOEvent.getStr_calendar_title());
            holder.event_weekday.setText(DTOEvent.getStr_calendar_weekday());*/
            }
        }

        @Override
        public int getItemCount() {
            return DTOEventList.size();
        }

    }
}

