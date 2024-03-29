package com.mobilesutra.iiser_tirupati.Activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilesutra.iiser_tirupati.Config.IISERApp;
import com.mobilesutra.iiser_tirupati.Database.TABLE_COURSE;
import com.mobilesutra.iiser_tirupati.Database.TABLE_TIMETABLE;
import com.mobilesutra.iiser_tirupati.Error_Activity.ExceptionHandler;
import com.mobilesutra.iiser_tirupati.Fragment.Fragment_TimeTable;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by kalyani on 26/04/2016.
 */
public class ActivityTimeTable extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    Context context = null;
    RecyclerView recycler_view = null;
    public List<DTO_Courses_offered> course_List = new ArrayList<>();
    List<LinkedHashMap<String, String>> assign_time_table = new ArrayList<>();
    EventAdapter recyclerAdapter = null;
    LinearLayoutManager linearLayoutManager = null;
    Fragment_TimeTable first = null;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    FloatingActionButton fab = null;
    TextView add_process = null,add_phd = null,add_iphd = null,
            bs_ms_sem_two = null,bs_ms_sem_four = null,bs_ms_sem_six = null,bs_ms_sem_eight = null;
    TextView user_name = null;
    Button add_courses = null;
    //TextView add_course1 = null;

    Button showPopupBtn, closePopupBtn;
    PopupWindow popupWindow;
    LinearLayout linearLayout1;

    FloatingActionButton fab_refresh = null;
    private List<PagerItem> listTabs = new ArrayList<PagerItem>();
    private SlidingTabLayout objSlidingTabLayout = null;
    ViewPagerAdapter objViewPagerAdapter = null;
    ViewPager mViewPager = null;

    SliderLayout mDemoSlider = null;
    private WebView webView1;
    String LOG_TAG = "ActivityTimeTable";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.fragment_three);

        context = this;
        getIntentData();
        initComponentData();
        initComponentListeners();
        displayBanner();
        bindComponentData();

        populateViewPager();

    }

    private void getIntentData() {
    }

    private void initComponentData() {
        fab_refresh = (FloatingActionButton) findViewById(R.id.fab);
        add_process = (TextView) findViewById(R.id.add_process);
        add_phd = (TextView) findViewById(R.id.add_process1);
        add_iphd= (TextView) findViewById(R.id.add_iphd);
        bs_ms_sem_two= (TextView) findViewById(R.id.bs_ms_sem_two);
        bs_ms_sem_four= (TextView) findViewById(R.id.bs_ms_sem_four);
        bs_ms_sem_six= (TextView) findViewById(R.id.bs_ms_sem_six);
        bs_ms_sem_eight= (TextView) findViewById(R.id.bs_ms_sem_eight);

      //  webView1 =(WebView)findViewById(R.id.webView1);

        add_courses = (Button) findViewById(R.id.add_course);
      //  add_course1 = (TextView) findViewById(R.id.add_course1);
        // add_process.setVisibility(View.GONE);
        add_process.setText("TIMETABLE");
        //add_process.setBackgroundColor();

        user_name = (TextView) findViewById(R.id.user_name);
        IISERApp.log(LOG_TAG, "session value is" + IISERApp.get_session(IISERApp.SESSION_LOGIN_FLAG));
        String user = IISERApp.get_session((IISERApp.SESSION_STUDENT_NAME));

        String sem_name = IISERApp.get_session((IISERApp.SESSION_SEMESTER_NAME));
        if ((IISERApp.get_session(IISERApp.SESSION_LOGIN_FLAG).equalsIgnoreCase("Y"))) {


            // String sem = IISERApp.get_session((IISERApp.SESSION_SEMESTER_NAME ));

            //user_name.setText(user);

            if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("student")) {
                user_name.setText(user + " (sem- " + sem_name + ")");

             //   populateViewPager();

            } else if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("faculty")) {
                user_name.setText(user + " (" + "Faculty" + ")");

              //  populateViewPager();

            } else if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("supervisor")) {
                user_name.setText(user + " (" + "Invigilator" + ")");

              //  add_course1.setVisibility(View.VISIBLE);
               // add_process1.setVisibility(View.VISIBLE);

                //   mViewPager.setVisibility(View.GONE);
            }
        } else if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("student")) {
            user_name.setText(user + " (sem- " + sem_name + ")");
        } else if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("faculty")) {
            user_name.setText(user + " (" + "Faculty" + ")");
        } else if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("supervisor")) {
            user_name.setText(user + " (" + "Invigilator" + ")");
        }

    }


    private void initComponentListeners() {
        fab_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityTimeTable.this, "Please wait...Refreshing Data.", Toast.LENGTH_LONG).show();
                refresh_data();

            }
        });


        add_phd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                String str_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "Privacypolicy.pdf";
                File fileImage = new File(str_path);

                Log.d("", "onClick: file name is  "+fileImage.exists());

                if (fileImage.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.fromFile(fileImage);
                    intent.setDataAndType(uri, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(ActivityTimeTable.this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
                    }
                } else {

                    Uri uri = Uri.parse("http://www.mobilesutra.com/Privacypolicy.pdf");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    startActivity(intent);
                }



               /* setContentView(R.layout.activity_webview);
                WebView webView = (WebView) findViewById(R.id.nyc_poi_webview);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl("https://www.google.com/");*/


             /*   String url = "http://www.mobilesutra.com/Privacypolicy.pdf";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);*/



            }
        });


    }

    private void bindComponentData() {

    }


    void populateViewPager() {
        if (objSlidingTabLayout == null)
            IISERApp.log(LOG_TAG, "sliding tablayout is null");
        {
            try {
                mViewPager = (ViewPager) findViewById(R.id.viewpager);
                IISERApp.log(LOG_TAG, "Viewpager is" + mViewPager);
                // mViewPager.setOffscreenPageLimit(1);
                listTabs.add(new PagerItem("Monday", 0, 0));
                listTabs.add(new PagerItem("Tuesday", 0, 0));
                listTabs.add(new PagerItem("Wednesday", 0, 0));
                listTabs.add(new PagerItem("Thursday", 0, 0));
                listTabs.add(new PagerItem("Friday", 0, 0));
                listTabs.add(new PagerItem("Saturday", 0, 0));
                IISERApp.log(LOG_TAG, "listTabs add PagerItem" + listTabs);

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
         * @return A new {@link android.support.v4.app.Fragment} to be displayed by a {@link ViewPager}
         */
        android.support.v4.app.Fragment createFragment() {
            Log.d("Fragment", "In createFragment");
            if (mTitle.toString().equalsIgnoreCase("Monday"))
                return Fragment_TimeTable.newInstance(mTitle.toString());
            else if (mTitle.toString().equalsIgnoreCase("Tuesday"))
                return Fragment_TimeTable.newInstance(mTitle.toString());
            else if (mTitle.toString().equalsIgnoreCase("Wednesday"))
                return Fragment_TimeTable.newInstance(mTitle.toString());
            else if (mTitle.toString().equalsIgnoreCase("Thursday"))
                return Fragment_TimeTable.newInstance(mTitle.toString());
            else if (mTitle.toString().equalsIgnoreCase("Friday"))
                return Fragment_TimeTable.newInstance(mTitle.toString());
            else if (mTitle.toString().equalsIgnoreCase("Saturday"))
                return Fragment_TimeTable.newInstance(mTitle.toString());
            else
                return null;
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
        public android.support.v4.app.Fragment getItem(int i) {
            return listTabs.get(i).createFragment();
        }

        @Override
        public int getCount() {
            return listTabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return listTabs.get(position).getTitle();
        }

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
            IISERApp.log(LOG_TAG, "Timetable Banner array" + bannerArray);
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
//            if(course_List.size()==0)
//            {
//                Toast.makeText(ActivityTimeTable.this, "Please select courses", Toast.LENGTH_SHORT).show();
//            }

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_course_list_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            DTO_Courses_offered assign = course_List.get(position);
            Log.d("Fragment", "course name:" + assign.getStr_course_name());
            //if( int assign = 0)
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
        public int getItemCount() {
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
    public void onResume() {
        super.onResume();


        IISERApp.log(LOG_TAG, "Awak onresume-->");
        context.registerReceiver(mMessageReceiver, new IntentFilter(LOG_TAG));
        int count = TABLE_COURSE.get_selected_course_list();
        if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("student")) {
            if (count == 0)

            {
                add_courses.setVisibility(View.VISIBLE);

                TABLE_TIMETABLE.delete_tbl_timetable_sync();




                add_courses.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add_courses.setVisibility(View.GONE);
                        Intent i = new Intent(ActivityTimeTable.this, ActivityProfile.class);
                        startActivity(i);

                    }
                });
                Toast.makeText(ActivityTimeTable.this, "Please select courses...", Toast.LENGTH_SHORT).show();
            }
        }


        else if (IISERApp.get_session(IISERApp.SESSION_USER_TYPE).equalsIgnoreCase("supervisor")) {

           // webView1.setVisibility(View.VISIBLE);

            objSlidingTabLayout.setVisibility(View.GONE);
            mViewPager.setVisibility(View.GONE);
            add_phd.setVisibility(View.VISIBLE);
            add_iphd.setVisibility(View.VISIBLE);
            bs_ms_sem_two.setVisibility(View.VISIBLE);
            bs_ms_sem_four.setVisibility(View.VISIBLE);
            bs_ms_sem_six.setVisibility(View.VISIBLE);
            bs_ms_sem_eight.setVisibility(View.VISIBLE);
        }





        else {
             add_courses.setVisibility(View.GONE);
            // first.refreshData();

        }


    }





    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            IISERApp.log(LOG_TAG, "messege recieved");
            Bundle bundle = intent.getExtras();
            IISERApp.log_bundle(bundle);
            add_courses.setVisibility(View.GONE);

           /* if(bundle.getString(IISERApp.BUNDLE_RESPONSE_CODE).equalsIgnoreCase("200"))
            {
                Intent intent1 = new Intent(context,ActivityTimeTable.class);
                startActivity(intent1);
                finish();
                Snackbar.make(recycler_view, bundle.getString(IISERApp.BUNDLE_RESPONSE_MESSAGE), Snackbar.LENGTH_LONG).show();
            }
            else
            {
                Snackbar.make(recycler_view, bundle.getString(IISERApp.BUNDLE_EXCEPTION), Snackbar.LENGTH_LONG).show();
            }*/
            int size = listTabs.size();
            IISERApp.log(LOG_TAG, "size_of_tabs is:" + size);
            for (int i = 0; i < size; i++) {
                first = (Fragment_TimeTable) objViewPagerAdapter.instantiateItem(mViewPager, i);
                first.refreshData();

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
            first = (Fragment_TimeTable) objViewPagerAdapter.instantiateItem(mViewPager, i);
            first.refreshData();
        }

    }


    public void refresh_data() {

        if (((IISERApp) getApplicationContext()).isInternetAvailable()) {
            IISERApp.log(LOG_TAG, "button is pressed for refreshing in notice ");
            //   prgress_dialog = ProgressDialog.show(context, null, null);
            Intent intent1 = new Intent(context, IISERIntentService.class);
            IISERApp.log(LOG_TAG, "App.INTENT_FLAG:" + IISERApp.INTENT_FLAG + " App.INTENT_FLAG_GET_EXAM:" + IISERApp.INTENT_FLAG_TIMETABLE);
            intent1.putExtra(IISERApp.INTENT_FLAG, IISERApp.INTENT_FLAG_TIMETABLE);
            intent1.putExtra("Activity_name", "ActivityTimeTable");
            context.startService(intent1);
        } else {
            ((IISERApp) getApplicationContext()).isInternetAvailable();
        }
    }









}


