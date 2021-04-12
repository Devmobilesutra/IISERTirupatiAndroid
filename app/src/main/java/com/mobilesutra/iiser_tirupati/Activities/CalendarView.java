package com.mobilesutra.iiser_tirupati.Activities;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Database.TABLE_STUDENT_ATTENDENCE;
import com.mobilesutra.iiser_tirupati.Fragment.Fragment_Attendence;
import com.mobilesutra.iiser_tirupati.Model.DTO_Stud_Attendence;
import com.mobilesutra.iiser_tirupati.R;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by a7med on 28/06/2015.
 */
public class CalendarView extends LinearLayout {
  //  private  ArrayList<DTO_Stud_Attendence> presentyArayStud;
    // for logging

    public String LOG_TAG = "CalendarView";
    private static final String LOGTAG = "Calendar View";
  //  ArrayList<DtoGetDailyMilkRecords> dtoGetDailyMilkRecordses = null;
    // how many days to show, defaults to six weeks, 42 days
    private static final int DAYS_COUNT = 35;

    // default date format
    private static final String DATE_FORMAT = "MMM yyyy";

    // date format
    private String dateFormat;

    // current displayed month
    private Calendar currentDate = Calendar.getInstance();
    Date currentdateForNxtPrevBtn = new Date();

    //event handling
    private EventHandler eventHandler = null;
    private EventHandlerMonthPrev eventHandlerMonth = null;
    EventHandlerMonthNext eventHandlerMonthNext = null;

    // internal components
    private LinearLayout header;
    private ImageView btnPrev;
    private ImageView btnNext;
    public  TextView txtDate;
    public GridView grid;

    // seasons' rainbow
    /*int[] rainbow = new int[] {
            R.color.summer,
			R.color.fall,
			R.color.winter,
			R.color.spring
	};
*/
    // month-season association (northern hemisphere, sorry australia :)
    int[] monthSeason = new int[]{2, 2, 3, 3, 3, 0, 0, 0, 1, 1, 1, 2};

    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    /**
     * Load control xml layout
     */
    private void initControl(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_calendar, this);

        loadDateFormat(attrs);
        assignUiElements();
        assignClickHandlers();

        updateCalendar();
        // refreshDays();

    }


/*    public void refreshDays() {

        dtoGetDailyMilkRecordses = TABLE_DAILY_MILK_RECORDS.getMilkRecords();
        // adapterDataObject.printAll();
        for (int n = 0; n < 35; n++) {
            if (dtoGetDailyMilkRecordses.get(n).getDAY().equalsIgnoreCase((n + 1) + "")) {
                if (dtoGetDailyMilkRecordses.get(n).getMILK_TAKEN().equalsIgnoreCase("yes")) {
                    Log.e("MILK TAKEN FOR DAY ", "" + n + 1);

                } else {
                    Log.e("MILK NOT TAKEN FOR DAY ", "" + n + 1);
                }
            }
        }


    }*/


    private void loadDateFormat(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

        try {
            // try to load provided date format, and fallback to default otherwise
            dateFormat = ta.getString(R.styleable.CalendarView_dateFormat);
            if (dateFormat == null)
                dateFormat = DATE_FORMAT;
        } finally {
            ta.recycle();
        }
        //   dtoGetDailyMilkRecordses = TABLE_DAILY_MILK_RECORDS.getMilkRecords();
    }

    private void assignUiElements() {
        // layout is inflated, assign local variables to components
        header = (LinearLayout) findViewById(R.id.calendar_header);
        btnPrev = (ImageView) findViewById(R.id.calendar_prev_button);
        btnNext = (ImageView) findViewById(R.id.calendar_next_button);
        txtDate = (TextView) findViewById(R.id.calendar_date_display);
        txtDate.setPadding(2, 2, 2, 2);
        grid = (GridView) findViewById(R.id.calendar_grid);
    }

    private void assignClickHandlers() {
        // add one month and refresh UI
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, 1);
                currentDate.getTime();
                currentdateForNxtPrevBtn =  currentDate.getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-yyyy");
          //      MyApplication.log(LOG_TAG, "date is = " + simpleDateFormat.format(currentDate.getTime()));
                eventHandlerMonthNext.onDayPressNext(simpleDateFormat.format(currentDate.getTime()));

                //     MyApplication.set_session(MyApplication.SESSION_NEXT_MONTH, simpleDateFormat.format(currentDate.getTime()));
               // updateCalendar();
            }
        });

        // subtract one month and refresh UI

        btnPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, -1);
                currentDate.getTime();
                currentdateForNxtPrevBtn =  currentDate.getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-yyyy");
                eventHandlerMonth.onDayPressNew(simpleDateFormat.format(currentDate.getTime()));
             //   MyApplication.log(LOG_TAG, "date is = " + simpleDateFormat.format(currentDate.getTime()));
                //    MyApplication.set_session(MyApplication.SESSION_PREV_MONTH, simpleDateFormat.format(currentDate.getTime()));
              //  updateCalendar();
            }
        });


        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                eventHandler.onDayPress((Date) adapterView.getItemAtPosition(i));
            }
        });


    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar() {
        updateCalendar(null,"");
    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar(ArrayList<DTO_Stud_Attendence> events,String title) {
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) currentDate.clone();

        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        // fill cells
        while (cells.size() < DAYS_COUNT) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // update grid
        grid.setAdapter(new CalendarAdapter(getContext(), cells, events,title));

        // update title
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Log.d("current datec ",sdf.format(currentDate.getTime()));
        txtDate.setText(sdf.format(currentDate.getTime()));

        // set header color according to current season
        int month = currentDate.get(Calendar.MONTH);
        int season = monthSeason[month];
        //int color = rainbow[season];

        header.setBackgroundColor(getResources().getColor(R.color.edittext_background));
    }



    private class CalendarAdapter extends ArrayAdapter<Date> {
        // days with events
        private ArrayList<DTO_Stud_Attendence> presentyArayStud;
String tabTitel ;

        // for view inflation
        private LayoutInflater inflater;

        public CalendarAdapter(Context context, ArrayList<Date> days, ArrayList<DTO_Stud_Attendence> eventDays,String Title) {
            super(context, R.layout.control_calendar_day, days);
            this.tabTitel = Title;
            if(eventDays != null)
            {
                if(eventDays.size() > 0)
                {
                    this.presentyArayStud = eventDays;
                }else {
                   // String tabTitel = IISERApp.get_session((IISERApp.SESSION_TAB_TITLE));
                    Log.d("tabtitle ",tabTitel);
                    presentyArayStud = TABLE_STUDENT_ATTENDENCE.getPresentDatesForStud(tabTitel);
                }
            }else {
              //  String tabTitel = IISERApp.get_session((IISERApp.SESSION_TAB_TITLE));
                Log.d("tabtitle ",tabTitel);
                presentyArayStud = TABLE_STUDENT_ATTENDENCE.getPresentDatesForStud(tabTitel);
            }

            inflater = LayoutInflater.from(context);
          //  Fragment_Attendence frag = new Fragment_Attendence();

        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            // day in question
           // currentdateForNxtPrevBtn =  currentDate.getTime();
            Date date = getItem(position);
            int day = date.getDate();
            int month = date.getMonth();
            int year = date.getYear();

            DateFormat dateFormatForDay = new SimpleDateFormat("EEEE");
            String weekday = dateFormatForDay.format(date);
            Log.d("week day : ",weekday);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String req_date = dateFormat.format(date);

            // today
            Date today = new Date();

            // inflate item if it does not exist yet
            if (view == null)
                view = inflater.inflate(R.layout.control_calendar_day, parent, false);

            TextView text = view.findViewById(R.id.tx_dates);

            // if this day has an event, specify event image
            //view.setBackgroundResource(0);
          //  text.setBackgroundResource(R.drawable.calender_cell);
           /* text.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            text.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);*/

          //  ((TextView) view).setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));


            // clear styling
            text.setTypeface(null, Typeface.NORMAL);
            text.setTextColor(Color.BLACK);





            for (int i = 0; i < presentyArayStud.size(); i++) {

                if (req_date.equalsIgnoreCase(presentyArayStud.get(i).getDate())) {
                    if(presentyArayStud.get(i).getStud_present().equals("1") && presentyArayStud.get(i).getLec_present().equals("1"))
                    {
                        text.setTypeface(null, Typeface.BOLD);
                        text.setTextColor(getResources().getColor(R.color.green));
                     //   text.setBackgroundResource(R.drawable.calender_cell);
                    }else if(presentyArayStud.get(i).getStud_present().equals("0") && presentyArayStud.get(i).getLec_present().equals("1"))
                    {
                        text.setTypeface(null, Typeface.BOLD);
                        text.setTextColor(getResources().getColor(R.color.red));
                     //   text.setBackgroundResource(R.drawable.calender_cell);
                    }

                }
            /*    MyApplication.log(LOG_TAG, "in for i=" + i);
                MyApplication.log(LOG_TAG," req date->"+req_date);
                MyApplication.log(LOG_TAG," dtoGetDailyMilkRecordses.get(i).getMILK_DATE())-->>"+dtoGetDailyMilkRecordses.get(i).getMILK_DATE());
                MyApplication.log(LOG_TAG," dtoGetDailyMilkRecordses.get(i).getMILK_TAKEN()-->>>"+dtoGetDailyMilkRecordses.get(i).getMILK_TAKEN());
*/
           /*     if (req_date.equalsIgnoreCase(presentyArayStud.get(i).getDate())) {
                    if (dtoGetDailyMilkRecordses.get(i).getMILK_TAKEN().equalsIgnoreCase("NO")) {
                        //    MyApplication.log(LOG_TAG, "in for if againg if");
                        ((TextView) view).setTypeface(null, Typeface.BOLD);
                        ((TextView) view).setTextColor(getResources().getColor(R.color.green));
                        ((TextView) view).setBackgroundResource(R.drawable.calender_cell);
                    } else if (dtoGetDailyMilkRecordses.get(i).getMILK_TAKEN().equalsIgnoreCase("YES")) {
                        float buffMilk = dtoGetDailyMilkRecordses.get(i).getBUFFALO_MILK();
                        float cowMilk = dtoGetDailyMilkRecordses.get(i).getCOW_MILK();
                        if(buffMilk > Float.parseFloat(MyApplication.get_session(MyApplication.SESSION_BUFFALO)) ||
                                cowMilk > Float.parseFloat(MyApplication.get_session(MyApplication.SESSION_COW))){
                            ((TextView) view).setTextColor(getResources().getColor(R.color.red));
                            ((TextView) view).setBackgroundResource(R.drawable.calender_cell);
                        }
                        if(buffMilk < Float.parseFloat(MyApplication.get_session(MyApplication.SESSION_BUFFALO)) ||
                                cowMilk < Float.parseFloat(MyApplication.get_session(MyApplication.SESSION_COW))){
                            ((TextView) view).setTextColor(getResources().getColor(R.color.red));
                            ((TextView) view).setBackgroundResource(R.drawable.calender_cell);
                        }
                    }
                }*/


            }

            if (month != currentdateForNxtPrevBtn.getMonth() || year != currentdateForNxtPrevBtn.getYear()) {
                // if this day is outside current month, grey it out
                text.setTextColor(getResources().getColor(R.color.greyed_out));
            //    text.setBackgroundResource(R.drawable.calender_cell);
            } else if (day == today.getDate() && month == today.getMonth() && year == today.getYear()) {
                // if it is today, set it to blue/bold
                text.setTypeface(null, Typeface.BOLD);
                text.setTextColor(getResources().getColor(R.color.button_pressed));
                /* ((TextView) view).setBackgroundResource(R.drawable.calender_cell);*/

            }else if(day > today.getDate() && month == today.getMonth() && year == today.getYear())
            {

                text.setTextColor(getResources().getColor(R.color.greyed_out));
            //    text.setBackgroundResource(R.drawable.calender_cell);

            }else if(month > today.getMonth() && year == today.getYear())
            {
                text.setTextColor(getResources().getColor(R.color.greyed_out));
            //    text.setBackgroundResource(R.drawable.calender_cell);
            }else if(weekday.equals("Sunday"))
            {
                text.setTextColor(getResources().getColor(R.color.red));
           //     text.setBackgroundResource(R.drawable.calender_cell);
            }

           /* if (month != today.getMonth() || year != today.getYear()) {
                // if this day is outside current month, grey it out
                ((TextView) view).setTextColor(getResources().getColor(R.color.greyed_out));
                ((TextView) view).setBackgroundResource(R.drawable.calender_cell);
            } else if (day == today.getDate()) {
                // if it is today, set it to blue/bold
                ((TextView) view).setTypeface(null, Typeface.BOLD);
                ((TextView) view).setTextColor(getResources().getColor(R.color.button_pressed));

            }*/

            // set text
            text.setText(String.valueOf(date.getDate()));
            return view;
        }
    }



    /**
     * Assign event handler to be passed needed events
     */
    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public void setEventHandlerMonthPrev(EventHandlerMonthPrev eventHandlerMonth) {
        this.eventHandlerMonth = eventHandlerMonth;
    }

    public void setEventHandlerMonthNext(EventHandlerMonthNext eventHandlerMonthNext) {
        this.eventHandlerMonthNext = eventHandlerMonthNext;
    }

    /**
     * This interface defines what events to be reported to
     * the outside world
     */
    public interface EventHandler {
        //		void onDayLongPress(Date date);
        void onDayPress(Date date);
    }

    public interface EventHandlerMonthPrev {
        //		void onDayLongPress(Date date);
        void onDayPressNew(String date);
    }

    public interface EventHandlerMonthNext {
        //		void onDayLongPress(Date date);
        void onDayPressNext(String date);
    }
}
