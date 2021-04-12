package com.mobilesutra.iiser_tirupati.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Database.TABLE_ATTENDENCE_MASTER;
import com.mobilesutra.iiser_tirupati.Database.TABLE_COURSE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_TIMETABLE;
import com.mobilesutra.iiser_tirupati.Fragment.FragmentFacultyAttendence;
import com.mobilesutra.iiser_tirupati.Fragment.Fragment_Attendence;
import com.mobilesutra.iiser_tirupati.Fragment.MentorPhdFragment;
import com.mobilesutra.iiser_tirupati.Model.DTO_Courses_offered;
import com.mobilesutra.iiser_tirupati.R;
import com.mobilesutra.iiser_tirupati.Tabs.SlidingTabLayout;
import com.mobilesutra.iiser_tirupati.background.IISERIntentService;
import com.mobilesutra.iiser_tirupati.banner.Animations.DescriptionAnimation;
import com.mobilesutra.iiser_tirupati.banner.SliderLayout;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.BaseSliderView;
import com.mobilesutra.iiser_tirupati.banner.SliderTypes.TextSliderView;
import com.mobilesutra.iiser_tirupati.banner.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;


public class ActivityAttendenceFaculty extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener{

    Context context = null;
    RecyclerView recycler_view = null;
    public List<DTO_Courses_offered> course_List = new ArrayList<>();
   // ActivityAssignment.EventAdapter recyclerAdapter = null;
    LinearLayoutManager linearLayoutManager = null;
    FragmentFacultyAttendence first = null;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    FloatingActionButton fab_refresh = null, fab_add_assingment = null;

    private List<PagerItem> listTabs = new ArrayList<PagerItem>();
  public  SlidingTabLayout objSlidingTabLayout = null;
    ViewPagerAdapter objViewPagerAdapter = null;
   public   ViewPager mViewPager = null;
   public TextView add_process = null;
    TextView user_name = null;

    List<LinkedHashMap<String, String>> assign_list = new ArrayList<>();
    SliderLayout mDemoSlider = null;
    private GoogleApiClient client;
    String LOG_TAG = "ActivityAttendenceFaculty";
    private FragmentManager Frag_attendence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_three_new);


        context = this;
        getIntentData();
        initComponentData();
        initComponentListeners();
        displayBanner();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
                Toast.makeText(ActivityAttendenceFaculty.this, "Please wait...Refreshing Data.", Toast.LENGTH_LONG).show();

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

    public void refresh_data() {

        if (((IISERApp) getApplicationContext()).isInternetAvailable()) {
            IISERApp.log(LOG_TAG, "button is pressed for refreshing in notice ");
            //   prgress_dialog = ProgressDialog.show(context, null, null);
            Intent intent1 = new Intent(context, IISERIntentService.class);
            IISERApp.log(LOG_TAG, "App.INTENT_FLAG:" + IISERApp.INTENT_FLAG + " App.INTENT_FLAG_GET_EXAM:" + IISERApp.INTENT_FLAG_ASSIGNMENT);
            intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_FACULTY_ATTENDENCE);
            intent1.putExtra("Activity_name", "ActivityAttendenceFaculty");
            context.startService(intent1);
        } else {
            ((IISERApp) getApplicationContext()).isInternetAvailable();
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

   /* public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

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

           *//* Log.d("Fragment","position of view:"+viewType);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("Fragment","position of view:"+view.getTag());
                    Intent intent = new Intent(getContext(), ActivityCourseDetails.class);
                    intent.putExtra("position",String.valueOf(view.getTag()));
                    startActivity(intent);


                }
            });*//*
            return new EventAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(EventAdapter.MyViewHolder holder, final int position)
        {
            DTO_Courses_offered assign = course_List.get(position);
            Log.d("Fragment", "course name:" + assign.getStr_course_name());
            holder.course_name.setText(assign.getStr_course_name());


         *//*   holder.lnrLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("Fragment", "position of view:" + position);
                    Intent intent = new Intent(context, ActivityCourseDetails.class);
                    intent.putExtra("position", String.valueOf(position));
                    startActivity(intent);
                }
            });*//*
        }

        @Override
        public int getItemCount()

        {
            return course_List.size();
        }


    }*/

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
        /*Toast.makeText(ActivityAttendenceFaculty.this, "Please wait..refreshing data.", Toast.LENGTH_SHORT).show();
        refresh_data();
        Toast.makeText(ActivityAttendenceFaculty.this, "Data refreshed successfully.", Toast.LENGTH_SHORT).show();
*/
        IISERApp.log(LOG_TAG, "Awak onresume-->");
        context.registerReceiver(mMessageReceiver, new IntentFilter(LOG_TAG));

        IISERApp.log("XYZ", " onResumeFragments():");
        IISERApp.set_session("onRES", "one");
        int size = listTabs.size();
        IISERApp.log(LOG_TAG, "size_of_tabs is:" + size);
        for (int i = 0; i < size; i++) {
            first = (FragmentFacultyAttendence) objViewPagerAdapter.instantiateItem(mViewPager, i);
            first.refreshData();
        }

   /*     if (IISERApp.get_session(IISERApp.SESSION_ASSIGNMENT_DOCUMENT_FLAG).equalsIgnoreCase("Y"))
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
                Toast.makeText(ActivityAttendenceFaculty.this, "Please select courses...", Toast.LENGTH_SHORT).show();
            }
        } else {

            IISERApp.log("XYZ", " onResumeFragments():");
            IISERApp.set_session("onRES", "one");
            int size = listTabs.size();
            IISERApp.log(LOG_TAG, "size_of_tabs is:" + size);
            for (int i = 0; i < size; i++) {
                first = (FragmentFacultyAttendence) objViewPagerAdapter.instantiateItem(mViewPager, i);
                first.refreshData();
            }
        }*/
        // this.onCreate(null);

    }

  public  void populateViewPager(String val)
    {
        //  if (objSlidingTabLayout == null) {
        try {
            mViewPager = (ViewPager) findViewById(R.id.viewpager);

//mViewPager.setOnPageChangeListener(this);

            // mViewPager.setOffscreenPageLimit(1);
            LinkedHashMap<String, String> lhm=null;
            /*if(IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("student")) {
                IISERApp.log(LOG_TAG,"in  if of session ");
                lhm = TABLE_COURSE.get_selected_course();
            }
            else if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("faculty"))
            {
                IISERApp.log(LOG_TAG,"in else if of session ");
                lhm = TABLE_TIMETABLE.get_faculty_course();
            }*/

            if(val.equalsIgnoreCase("faculty"))
            {
                lhm = TABLE_TIMETABLE.get_faculty_course();
            }else if(val.equalsIgnoreCase("mentor"))
            {
                lhm = TABLE_ATTENDENCE_MASTER.get_Faculty_stud_courseCode();
            }

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
                            0 ,val// Divider color
                    ));
                }
                listTabs.add(new PagerItem("MENTOR PhD",0,0,val));
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
            int item = mViewPager.getCurrentItem(); // get
        //    Toast.makeText(getApplicationContext(),"item is "+listTabs.get(item).getTitle(),Toast.LENGTH_SHORT).show();
          //  IISERApp.set_session(IISERApp.SESSION_TAB_TITLE, String.valueOf(listTabs.get(item).getTitle()));

            objSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                    objSlidingTabLayout.set_selected_text_color(position);
                  add_process.setText("ATTENDANCE");
                    objSlidingTabLayout.setVisibility(View.VISIBLE);

                 //   FragmentFacultyAttendence.recyclerView_layout.setVisibility(View.VISIBLE);
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentFacultyAttendence f = new FragmentFacultyAttendence();
                    if(f != null){
                        f.refreshData();
                     //   f.refreshComponent();

                        /*if(f.recyclerView_layout != null)
                        {
                            f.recyclerView_layout.setVisibility(View.VISIBLE);
                            int item = mViewPager.getCurrentItem();
                            listTabs.get(item).createFragment();
                        }*/

                    }

                   /* Fragment frg = null;
                    FragmentFacultyAttendence f1 = (FragmentFacultyAttendence) fm.findFragmentByTag("FragmentFacultyAttendence");
                    if(f1 != null)
                    {
                        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.detach(f1);
                        ft.attach(f1);
                        ft.commit();
                    }
*/

                  //  recyclerView_layout.setVisibility(View.VISIBLE);
                  /*  if (String.valueOf(title).equals("MENTOR PhD"))
                    {
                        //objSlidingTabLayout.setVisibility(View.GONE);
                    }else {
                        objSlidingTabLayout.setVisibility(View.VISIBLE);
                    }*/



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
        String mentor;

        PagerItem(CharSequence title, int indicatorColor, int dividerColor,String val) {
            mTitle = title;
            mIndicatorColor = indicatorColor;
            mDividerColor = dividerColor;
            mentor = val;
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
            return FragmentFacultyAttendence.newInstance(mTitle.toString(),mentor);


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


    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            IISERApp.log(LOG_TAG, "messege recieved");
            Bundle bundle = intent.getExtras();

            int size = listTabs.size();
            for (int i = 0; i < size; i++) {
                first = (FragmentFacultyAttendence) objViewPagerAdapter.instantiateItem(mViewPager, i);
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
            first = (FragmentFacultyAttendence) objViewPagerAdapter.instantiateItem(mViewPager, i);
           first.refreshData();
        }

      //  listTabs.clear();
        populateViewPager("faculty");
    }
}
