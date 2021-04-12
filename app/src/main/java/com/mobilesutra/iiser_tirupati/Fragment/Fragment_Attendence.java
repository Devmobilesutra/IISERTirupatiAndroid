package com.mobilesutra.iiser_tirupati.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mobilesutra.iiser_tirupati.Activities.ActivityAttendence;
import com.mobilesutra.iiser_tirupati.Activities.CalendarView;
import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Database.TABLE_ATTENDENCE_MASTER;
import com.mobilesutra.iiser_tirupati.Database.TABLE_STUDENT_ATTENDENCE;
import com.mobilesutra.iiser_tirupati.Model.DTO_Stud_Attendence;
import com.mobilesutra.iiser_tirupati.R;
import com.mobilesutra.iiser_tirupati.banner.SliderLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Attendence extends Fragment {
    Context context = null;
    RecyclerView recycler_view = null;

   // EventAdapter recyclerAdapter = null;
    LinearLayoutManager linearLayoutManager = null;
    List<LinkedHashMap<String, String>> attendence_No_percentage = new ArrayList<>();
    int attendence_No_percentage_count = 0;
    String LOG_TAG = "Fragment_Asssign", tab_title = "", filename = "";
    ProgressDialog dialog = null;

    SliderLayout mDemoSlider = null;
    View view = null;

    private static final String ARG_PARAM_TITLE = "title";
    private static final String ARG_TAB_TITLE = "Mentor";
    private TextView tv_totalNoClasses;
    private TextView tv_total_present;
    private TextView tv_total_percentage;
    public CalendarView cv;
    private ArrayList<DTO_Stud_Attendence> presentStudArraylist;
    private Button backButton;
    private String mentor_tab_title;

    public static Fragment_Attendence newInstance(String str_title) {
        Fragment_Attendence fragment = new Fragment_Attendence();
        Log.d("Fragment", "In newInstance");
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM_TITLE, str_title);
        fragment.setArguments(bundle);
        return fragment;
    }

    public Fragment_Attendence() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity().getApplicationContext();
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_five, container, false);
        if (view == null) {
            view = inflater.inflate(R.layout.fragment__attendence, container, false);

            Bundle args = getArguments();
            IISERApp.log(LOG_TAG, "tab title next:" + args.getCharSequence(ARG_PARAM_TITLE).toString());
            tab_title = args.getCharSequence(ARG_PARAM_TITLE).toString();
          //  mentor_tab_title = args.getCharSequence(ARG_TAB_TITLE).toString();
            LOG_TAG = LOG_TAG + tab_title;

            initComponentData(view);
            initComponentListeners();
            bindComponentData();

        }
        return view;
    }



    private void initComponentData(View view) {

        tv_totalNoClasses = (TextView) view.findViewById(R.id.tv_totalNo_classes);
        tv_total_present = (TextView) view.findViewById(R.id.tv_total_present);
        tv_total_percentage = (TextView) view.findViewById(R.id.tv_total_percentage);
        cv = (CalendarView) view.findViewById(R.id.calendar_view);
        backButton = (Button) view.findViewById(R.id.btn_back);

     //   backButton = (Button) view.findViewById(R.id.btn_back);

        /*if (mentor_tab_title.equalsIgnoreCase("MENTOR PhD"))
        {
           backButton.setVisibility(View.VISIBLE);
        }else
        {
            backButton.setVisibility(View.GONE);
        }
*/
    }



    private void bindComponentData()
    {

        if (view != null) {
           // IISERApp.set_session(IISERApp.SESSION_TAB_TITLE, tab_title);
           // String tabTitel = IISERApp.get_session((IISERApp.SESSION_TAB_TITLE));
          //  Log.d("tabtitleinfrag ",tabTitel);
            attendence_No_percentage = TABLE_ATTENDENCE_MASTER.get_attendence_percentage(tab_title);
            IISERApp.log(LOG_TAG, "assignment_list: " + attendence_No_percentage);
            attendence_No_percentage_count = attendence_No_percentage.size();
          for (int i = 0; i < attendence_No_percentage_count; i++)
            {

                String tot_no_classes = attendence_No_percentage.get(i).get("tot_no_classes").toString();
                String attented_classes = attendence_No_percentage.get(i).get("attended_classes").toString();
                String avg_percentage = attendence_No_percentage.get(i).get("avg_percentage").toString();

                tv_totalNoClasses.setText(tot_no_classes);
                tv_total_present.setText(attented_classes);
                tv_total_percentage.setText(avg_percentage);
            }

            HashSet<Date> events = new HashSet<>();
            events.add(new Date());
            presentStudArraylist = TABLE_STUDENT_ATTENDENCE.getPresentDatesForStud(tab_title);
            cv.updateCalendar(presentStudArraylist,tab_title);

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

                // activitytabhostafterlogin
                /*FragmentFacultyAttendence f = new FragmentFacultyAttendence();
                if(f != null){
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.setCustomAnimations(android.R.animator.fade_in,
                            android.R.animator.fade_out);
                    ft.hide(f);
                    ft.commit();

                }*/


               /* FragmentManager mFragmentMgr= getFragmentManager();
                FragmentTransaction mTransaction = mFragmentMgr.beginTransaction();
                Fragment childFragment =mFragmentMgr.findFragmentByTag("Fragment_Attendence");
                mTransaction.remove(childFragment);
                mTransaction.commit();*/

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


    public void refreshData() {
        bindComponentData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
}
